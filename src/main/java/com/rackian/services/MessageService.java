package com.rackian.services;

import com.rackian.controllers.MessageController;
import com.rackian.models.Message;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MessageService implements Runnable {

    private Pane messagePane;

    public MessageService(Pane messagePane) {

        this.messagePane = messagePane;

    }

    @Override
    public void run() {

        try {
            connection();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void connection() throws Exception {

        ServerSocket serverSocket;
        Socket socket;
        InputStream is;
        ObjectInputStream ois;

        serverSocket = new ServerSocket(1122);

        while (true) {

            socket = serverSocket.accept();
            is = socket.getInputStream();
            ois = new ObjectInputStream(is);

            MessageController mc;
            mc = new MessageController();
            mc.setParent(messagePane);
            mc.setMessage((Message)ois.readObject());
            mc.setDirection(MessageController.MESSAGE_RECEIVED);

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        mc.createMessage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }

}
