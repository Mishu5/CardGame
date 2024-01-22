package com.example.kierki.client;

import com.example.kierki.server.game.Player;
import com.example.kierki.server.game.cards.Card;
import com.example.kierki.shared.transfer.GameEnvironment;
import javafx.application.Platform;
import javafx.scene.control.Button;

import java.util.List;

/**
 * Updates view in gui based on data in GameEnvironment
 */

public class GuiViewUpdater {

    private KierkiController controller;

    public GuiViewUpdater() {
    }

    public void setController(KierkiController controller) {
        this.controller = controller;
    }

    public void updateView(GameEnvironment gameEnvironment) {

        Platform.runLater(() -> {
            String username = UserName.getUserName();
            List<Button> buttons = controller.getButtons();
            List<Player> players = gameEnvironment.getPlayers();
            Player currentPlayer = getPlayerByName(username, players);
            //resetting view
            resetView();
            //adding cards in hand
            int i = 0;
            for (Card card : currentPlayer.getHand()) {
                buttons.get(i).setDisable(false);
                buttons.get(i).setText(card.toNameString().replace("11", "J").replace("12", "D").replace("13", "K").replace("14", "A"));
                buttons.get(i).setUserData(card.toButtonUserData());
                i++;
            }
            //cards on table
            for (Card currentCard : gameEnvironment.getTable()) {
                switch (currentCard.getPlacedByPlayerId()) {
                    case 0:
                        controller.getPlayerLabel1().setText(currentCard.toNameString().replace("11", "J").replace("12", "D").replace("13", "K").replace("14", "A"));
                        break;
                    case 1:
                        controller.getPlayerLabel2().setText(currentCard.toNameString().replace("11", "J").replace("12", "D").replace("13", "K").replace("14", "A"));
                        break;
                    case 2:
                        controller.getPlayerLabel3().setText(currentCard.toNameString().replace("11", "J").replace("12", "D").replace("13", "K").replace("14", "A"));
                        break;
                    case 3:
                        controller.getPlayerLabel4().setText(currentCard.toNameString().replace("11", "J").replace("12", "D").replace("13", "K").replace("14", "A"));
                        break;
                }
            }
            //scores and nicks
            controller.getP1().setText(username);
            controller.getPlayer1score().setText(String.valueOf(currentPlayer.getScore()));
            int currentPlayerIndex = currentPlayer.getPlayerIndexInLobby();
            for ( i = 1; i < gameEnvironment.getPlayers().size(); i++) {
                int index = (currentPlayerIndex - i + gameEnvironment.getPlayers().size()) % gameEnvironment.getPlayers().size();
                Player tempPlayer = gameEnvironment.getPlayers().get(index);

                switch(i){
                    case 1:
                        controller.getP2().setText(tempPlayer.getUserName());
                        controller.getPlayer2score().setText(String.valueOf(tempPlayer.getScore()));
                        break;
                    case 2:
                        controller.getP3().setText(tempPlayer.getUserName());
                        controller.getPlayer3score().setText(String.valueOf(tempPlayer.getScore()));
                        break;
                    case 3:
                        controller.getP4().setText(tempPlayer.getUserName());
                        controller.getPlayer4score().setText(String.valueOf(tempPlayer.getScore()));
                        break;
                }
            }
            controller.getCurrentPlayer().setText(gameEnvironment.getCurrentPlayer());
            controller.getHandIndex().setText("Aktualne rozdanie: " + gameEnvironment.getHandIndex());
        });

    }

    private Player getPlayerByName(String name, List<Player> players) {
        Player result = null;

        for (Player player : players) {
            if (player.getUserName().equals(name)) {
                result = player;
                break;
            }
        }

        return result;
    }

    /**
     * Sets gui to default values on launch to prepare them for new inputs
     */
    private void resetView() {
        List<Button> buttons = controller.getButtons();
        int iterator = 1;
        for (Button button : buttons) {
            button.setDisable(true);
            button.setText("Karta " + iterator);
            iterator++;
        }
        controller.getPlayerLabel1().setText("Gracz 1");
        controller.getPlayerLabel2().setText("Gracz 2");
        controller.getPlayerLabel3().setText("Gracz 3");
        controller.getPlayerLabel4().setText("Gracz 4");
    }

}
