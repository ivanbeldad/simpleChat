package com.rackian.controllers;

import com.rackian.models.Message;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import java.net.URL;
import java.util.ResourceBundle;

class MessageSentController implements Initializable {

    @FXML
    private Pane messageSent;
    @FXML
    private Label messageNickSent;
    @FXML
    private Label messageContentSent;
    @FXML
    private Label messageTimeSent;

    private Message message;

    protected MessageSentController(Message message) {
        this.message = message;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        messageNickSent.setText(message.getNick());
        messageContentSent.setText(message.getMessage());
        messageTimeSent.setText(message.getTimeString());

    }

}
