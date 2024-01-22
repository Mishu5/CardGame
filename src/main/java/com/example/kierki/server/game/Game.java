package com.example.kierki.server.game;

import com.example.kierki.server.game.cards.Card;
import com.example.kierki.server.game.cards.Suit;
import com.example.kierki.server.userhandling.ClientHandler;
import com.example.kierki.shared.transfer.GameEnvironment;

import java.util.*;

/**
 * Class that hold information about game:
 * players and all game statuses
 */

public class Game{

    private boolean stared;
    private final List<ClientHandler> players;
    private int handNumber;//rozdanie
    private int currentPlayerIndex;
    private int index;
    private int playersMoved;
    private final List<Card> table;
    private Card firstCardOfRound;
    private int roundNumber;//runda

    public Game() {
        players = new ArrayList<>();
        index = 0;
        playersMoved = 0;
        table = new ArrayList<>();
        handNumber = 1;
        roundNumber = 1;
    }

    /**
     * Adds player to current game
     * @param player
     */
    public void addPlayer(ClientHandler player) {
        player.getPlayer().setPlayerIndexInLobby(index++);
        players.add(player);
    }

    /**
     * Starts game, sets access for users to make moves,
     * deals randomly generated deck and designates first player to move
     */
    public void startGame() {
        dealCards();
        Random random = new Random();
        currentPlayerIndex = random.nextInt(4);
        stared = true;
    }

    public boolean isStared() {
        return stared;
    }

    /**
     * Creates deck
     * @return created deck
     */
    private List<Card> initializeDeck() {
        List<Card> newDeck = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (int value = 2; value <= 14; value++) {
                newDeck.add(new Card(suit, value));
            }
        }
        return newDeck;
    }

    /**
     * Deals card to players
     */

    private void dealCards() {
        int cardPerPlayer = 13;
        List<Card> deck = initializeDeck();
        Collections.shuffle(deck);

        for (ClientHandler clientHandler : players) {
            Player tempPlayer = clientHandler.getPlayer();
            for (int i = 0; i < cardPerPlayer; i++) {
                tempPlayer.addToHand(deck.get(0));
                deck.remove(0);
            }
        }
    }

    public int getHandNumber() {
        return handNumber;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public List<Card> getTable() {
        return table;
    }

    public List<Player> getPlayers() {
        List<Player> result = new ArrayList<>();

        for (ClientHandler clientHandler : players) {
            result.add(clientHandler.getPlayer());
        }

        return result;
    }

    /**
     * @param clientHandlerId
     * @return Player class from List of ClientHandlers based on id
     */
    private Player getPlayerFromClientHandler(int clientHandlerId) {
        Player result = null;
        for (ClientHandler clientHandler : players) {
            if (clientHandler.getHandlerId() == clientHandlerId) {
                result = clientHandler.getPlayer();
                break;
            }
        }
        return result;
    }

    /**
     * Handles player move, calculates if player can place card or not
     * At the end checks if current play or hand is finished and assignes points to players
     * @param clientHandlerId
     * @param cardToPlay
     * @return
     */
    public boolean make_move(int clientHandlerId, String cardToPlay) {
        Player currentPlayer = getPlayerFromClientHandler(clientHandlerId);
        Card currentCard = currentPlayer.getCardToPlay(cardToPlay);

        if (currentPlayer.getPlayerIndexInLobby() != currentPlayerIndex || handNumber > 7) {
            return false;
        }

        //check deck and if can move
        boolean playerCanMove = checkIfPlayerCanMove(currentCard, currentPlayer);
        if (!playerCanMove) {
            return false;
        }

        //place Card
        currentCard.setPlacedByPlayerId(currentPlayer.getPlayerIndexInLobby());
        table.add(currentCard);
        currentPlayer.getHand().remove(currentCard);

        if (table.size() == 1) {
            firstCardOfRound = currentCard;
        }

        //check if end of round and add scores
        if (table.size() == 4) {
            finalizeRoundAndGiveScores();
        }

        currentPlayerIndex = (currentPlayerIndex - 1 + 4) % 4;
        return true;
    }

    /**
     * Checks if player can place card
     * @param cardPlayed card that player wants to place
     * @param player player that made move
     * @return ability to place card
     */
    private boolean checkIfPlayerCanMove(Card cardPlayed, Player player) {
        if (table.size() == 0) {
            return (handNumber != 2 && handNumber != 5 && handNumber != 7) || !checkIfPlayerHasSomethingElseThanHearts(player) || cardPlayed.getSuit() != Suit.HEARTS;
        } else {
            return cardPlayed.getSuit() == firstCardOfRound.getSuit() || (cardPlayed.getSuit() != firstCardOfRound.getSuit() && !checkIfPlayerHaveColorOnTable(player));
        }
    }

    private boolean checkIfPlayerHasSomethingElseThanHearts(Player player) {
        for (Card card : player.getHand()) {
            if (card.getSuit() != Suit.HEARTS) {
                return true;
            }
        }
        return false;
    }

    private boolean checkIfPlayerHaveColorOnTable(Player player) {
        for (Card playerCard : player.getHand()) {
            if (playerCard.getSuit() == firstCardOfRound.getSuit()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates object to transfer int DataContainer
     * Creates deep copy of data
     * @return
     */
    public GameEnvironment toGameEnvironment() {
        GameEnvironment result = new GameEnvironment();

        result.setStarted(stared);
        result.setHandIndex(handNumber);
        result.setCurrentPlayerIndex(currentPlayerIndex);
        List<Player> tempPlayerList = new ArrayList<>();
        for (ClientHandler player : players) {
            tempPlayerList.add(player.getPlayer().clone());
        }
        result.setPlayers(tempPlayerList);
        List<Card> tempTableList = new ArrayList<>();
        for (Card card : table) {
            tempTableList.add(card.clone());
        }
        result.setTable(tempTableList);
        return result;
    }

    /**
     * Assignees points to players and start next round or hand
     */
    private void finalizeRoundAndGiveScores() {
        //end of round
        int winningPlayerIndex = findWinningPlayer();
        Player winningPlayer = getPlayerById(winningPlayerIndex);
        int currentPlayerScore = winningPlayer.getScore();
        System.out.println("-----------PLAYER WON: " + winningPlayerIndex);
        switch (handNumber) {
            case 1:
                //No winning
                winningPlayer.setScore(currentPlayerScore - 20);
                break;
            case 2:
                //No hearts
                winningPlayer.setScore(currentPlayerScore - (20 * countHeartsInTable()));
                break;
            case 3:
                // No 12
                winningPlayer.setScore(currentPlayerScore - (60 * countValuesInTable(12)));
                break;
            case 4:
                // No 11
                winningPlayer.setScore(currentPlayerScore - (30 * countValuesInTable(11)));
                break;
            case 5:
                // No Kings of Hearts
                winningPlayer.setScore(currentPlayerScore - (150 * countKingOfHeartsInTable()));
                break;
            case 6:
                // No sixth and last Winning
                if (roundNumber == 6 || roundNumber == 13) {
                    winningPlayer.setScore(currentPlayerScore - 75);
                }
                break;
            case 7:
                winningPlayer.setScore(currentPlayerScore - 20);
                winningPlayer.setScore(currentPlayerScore - (20 * countHeartsInTable()));
                winningPlayer.setScore(currentPlayerScore - (60 * countValuesInTable(12)));
                winningPlayer.setScore(currentPlayerScore - (30 * countValuesInTable(11)));
                winningPlayer.setScore(currentPlayerScore - (150 * countKingOfHeartsInTable()));
                if (roundNumber == 6 || roundNumber == 13) {
                    winningPlayer.setScore(currentPlayerScore - 75);
                }
                break;
        }

        table.clear();
        roundNumber++;
        currentPlayerIndex = winningPlayerIndex + 1;

        //end of hand
        if (roundNumber > 13) {
            roundNumber = 1;
            handNumber++;
            for(ClientHandler player : players){
                player.getPlayer().getHand().clear();
            }
            dealCards();
            Random random = new Random();
            currentPlayerIndex = random.nextInt(4);
        }
    }

    public void skipHand(){
        Random random = new Random();
        handNumber++;
        for(ClientHandler player :players){
            player.getPlayer().getHand().clear();
            for(int i = roundNumber; i <= 13; i++ ) {
                int currentScore = player.getPlayer().getScore();
                int randomPoint = random.nextInt(131) + 20;
                player.getPlayer().setScore(currentScore - randomPoint);
            }
        }
        dealCards();
        roundNumber = 1;
        currentPlayerIndex = random.nextInt(4);
    }

    private int findWinningPlayer() {
        Card result = firstCardOfRound;

        for (Card cardInTable : table) {
            if (cardInTable.getValue() > result.getValue() && cardInTable.getSuit() == firstCardOfRound.getSuit()) {
                result = cardInTable;
            }
        }
        return result.getPlacedByPlayerId();
    }

    private Player getPlayerById(int id) {
        Player result = null;
        for (ClientHandler currentPlayer : players) {
            if (currentPlayer.getPlayer().getPlayerIndexInLobby() == id) {
                result = currentPlayer.getPlayer();
                break;
            }
        }
        return result;
    }

    private int countHeartsInTable() {
        int result = 0;
        for (Card card : table) {
            if (card.getSuit() == Suit.HEARTS) {
                result++;
            }
        }
        return result;
    }

    private int countValuesInTable(int value) {
        int result = 0;
        for (Card card : table) {
            if (card.getValue() == value) {
                result++;
            }
        }
        return result;
    }

    private int countKingOfHeartsInTable() {
        int result = 0;
        for (Card card : table) {
            if (card.getValue() == 13 && card.getSuit() == Suit.HEARTS) {
                result++;
            }
        }
        return result;
    }
}
