package com.rackian.services;

import com.rackian.Main;
import com.rackian.controllers.ChatController;
import com.rackian.controllers.MessageController;
import com.rackian.models.Message;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ReceiveMessageService implements Runnable {

    private Pane messagePane;

    public ReceiveMessageService(Pane messagePane) {

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
        Message message;


        serverSocket = new ServerSocket(Main.PORT_RECEIVE_MESSAGES);

        while (true) {
            MessageController mc;
            socket = serverSocket.accept();
            is = socket.getInputStream();
            ois = new ObjectInputStream(is);

            // RECIBO EL MENSAJE Y CIERRO LA CONEXION
            message = (Message)ois.readObject();
            socket.close();

            // AGREGO EL MENSAJE A LA LISTA
            ChatController.getMessages().add(message);

            // CREO EL MENSAJE EN EL PANEL SI ES EL USUARIO SELECCIONADO
            if (message.getUserOri().compareTo(ChatController.getUserDest()) == 0) {
                mc = new MessageController();
                mc.setParent(messagePane);
                mc.setMessage(message);
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

}
