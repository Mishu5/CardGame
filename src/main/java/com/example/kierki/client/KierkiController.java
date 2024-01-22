package com.example.kierki.client;

import com.example.kierki.client.communication.DataSender;
import com.example.kierki.shared.transfer.CommandType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for gui
 */

public class KierkiController {

    private DataSender dataSender;
    public void setDataSender(DataSender dataSender) {
        this.dataSender = dataSender;
    }

    @FXML
    private Button button1;
    @FXML
    private Button button2;
    @FXML
    private Button button3;
    @FXML
    private Button button4;
    @FXML
    private Button button5;
    @FXML
    private Button button6;
    @FXML
    private Button button7;
    @FXML
    private Button button8;
    @FXML
    private Button button9;
    @FXML
    private Button button10;
    @FXML
    private Button button11;
    @FXML
    private Button button12;
    @FXML
    private Button button13;
    @FXML
    private List<Button> buttons;

    @FXML
    private Label player1;
    @FXML
    private Label player2;
    @FXML
    private Label player3;
    @FXML
    private Label player4;
    @FXML
    private Label p1;
    @FXML
    private Label p2;
    @FXML
    private Label p3;
    @FXML
    private Label p4;
    @FXML
    private Label player1score;
    @FXML
    private Label player2score;
    @FXML
    private Label player3score;
    @FXML
    private Label player4score;
    @FXML
    private Label currentPlayer;
    @FXML
    private Label handIndex;

    public void initialize() {
        buttons = new ArrayList<>();
        buttons.add(button1);
        buttons.add(button2);
        buttons.add(button3);
        buttons.add(button4);
        buttons.add(button5);
        buttons.add(button6);
        buttons.add(button7);
        buttons.add(button8);
        buttons.add(button9);
        buttons.add(button10);
        buttons.add(button11);
        buttons.add(button12);
        buttons.add(button13);
    }

    @FXML
    public void handleButtonClick(ActionEvent event){
        Button clickedButton = (Button) event.getSource();
        try {
            dataSender.sendCommand(CommandType.GAME_MOVEMENT, (String) clickedButton.getUserData());
        } catch (IOException ignore) {
        }

    }

    public List<Button> getButtons(){
        return buttons;
    }

    public Label getPlayerLabel1() {
        return player1;
    }

    public Label getPlayerLabel2() {
        return player2;
    }

    public Label getPlayerLabel3() {
        return player3;
    }

    public Label getPlayerLabel4() {
        return player4;
    }

    public Label getPlayer1score() {
        return player1score;
    }

    public Label getPlayer2score() {
        return player2score;
    }

    public Label getPlayer3score() {
        return player3score;
    }

    public Label getPlayer4score() {
        return player4score;
    }

    public Label getCurrentPlayer() {
        return currentPlayer;
    }

    public Label getP1() {
        return p1;
    }

    public Label getP2() {
        return p2;
    }

    public Label getP3() {
        return p3;
    }

    public Label getP4() {
        return p4;
    }

    public Label getHandIndex() {
        return handIndex;
    }
}