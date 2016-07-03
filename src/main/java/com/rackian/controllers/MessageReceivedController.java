package com.rackian.controllers;

import com.rackian.models.Message;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import java.net.URL;
import java.util.ResourceBundle;

class MessageReceivedController implements Initializable {

    @FXML
    private Pane messageReceived;
    @FXML
    private Label messageNickReceived;
    @FXML
    private Label messageContentReceived;
    @FXML
    private Label messageTimeReceived;

    private Message message;

    public MessageReceivedController(Message message) {
        this.message = message;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        messageNickReceived.setText(message.getUserDest().getNick());
        messageContentReceived.setText(message.getMessage());
        messageTimeReceived.setText(message.getTimeString());

    }

}
