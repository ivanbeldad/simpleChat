package com.rackian.controllers;

import com.rackian.models.Message;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class MessageController {

    static public final int MESSAGE_RECEIVED = 0;
    static public final int MESSAGE_SENT = 1;

    private int direction;
    private Message message;
    private Pane parent;

    public MessageController() {
    }

    public MessageController(Pane parent, Message message, int direction) {
        this.parent = parent;
        this.direction = direction;
        this.message = message;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Pane getParent() {
        return parent;
    }

    public void setParent(Pane parent) {
        this.parent = parent;
    }

    public void createMessage() throws IOException {

        FXMLLoader loader;
        Pane pane;

        loader = new FXMLLoader();

        if (direction == MESSAGE_RECEIVED) {
            Message tempMessage = new Message();
            tempMessage.setUserOri(message.getUserDest());
            tempMessage.setUserDest(message.getUserOri());
            tempMessage.setMessage(message.getMessage());
            tempMessage.setTime(message.getTime());
            tempMessage.setStatus(Message.STATUS_SENT);

            loader.setLocation(getClass().getClassLoader().getResource("messageReceived.fxml"));
            loader.setController(new MessageReceivedController(tempMessage));
        } else if (direction == MESSAGE_SENT) {
            loader.setLocation(getClass().getClassLoader().getResource("messageSent.fxml"));
            loader.setController(new MessageSentController(message));
        }

        pane = loader.load();
        parent.getChildren().add(pane);

    }

}
