package com.rackian.services;

import com.rackian.Main;
import com.rackian.controllers.ChatController;
import com.rackian.controllers.MessageController;
import com.rackian.models.Message;
import com.rackian.models.User;
import com.sun.org.apache.xpath.internal.SourceTree;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class AliveService implements Runnable {

    List<User> onlineUsers;
    List<Message> newMessages;

    private int port;
    private ChatController chatController;

    public AliveService() {
    }

    public AliveService(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ChatController getChatController() {
        return chatController;
    }

    public void setChatController(ChatController chatController) {
        this.chatController = chatController;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000); // WAIT TO SERVICE ON SERVER START
            ping();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void ping() {

        Socket socket;
        InputStream is;
        ObjectInputStream ois;
        String ip;
        InetSocketAddress address;

        ip = Main.SERVER_IP;
        address = new InetSocketAddress(ip, port);

        System.out.println("Inicio de servicio de comprobación de estado. Puerto: " + port);

        try {

            while (true) {

                socket = new Socket();
                socket.setSoTimeout(1500);
                socket.connect(address);
                is = socket.getInputStream();
                ois = new ObjectInputStream(is);
                updateContacts(ois);
                updateMessages(ois);
                socket.close();
                Thread.sleep(500);

            }

        } catch (Exception e) {
            e.printStackTrace();
            goLoginErr();
        }

    }

    private void updateContacts(ObjectInputStream ois) throws IOException, ClassNotFoundException {

        if ((boolean)ois.readObject()) {
            onlineUsers = (List<User>) ois.readObject();
            chatController.setContacts(onlineUsers);
            chatController.updateOnlineContacts();
            //System.out.println("Cambios en los contactos");
        } else {
            //System.out.println("Sin cambios en los contactos.");
        }

    }

    private void updateMessages(ObjectInputStream ois) throws IOException, ClassNotFoundException {

        if ((boolean)ois.readObject()) {
            newMessages = (List<Message>) ois.readObject();
            newMessages.forEach(e -> System.out.println("Mensaje recibido: " + e.getMessage()));
            newMessages.forEach(m -> ChatController.getMessages().add(m));
            addMessagesToPane(newMessages);
        } else {
            //System.out.println("Sin cambios en los mensajes.");
        }

    }

    private void goLoginErr() {
        // MANDO AL LOGIN POR CONEXIÓN PERDIDA
        System.out.println("Conexión perdida");

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    FXMLLoader fxml = new FXMLLoader();
                    fxml.setLocation(getClass().getClassLoader().getResource("login.fxml"));
                    Pane rootPane = (Pane) fxml.load();
                    Scene scene = new Scene(rootPane);
                    Main.stage.setScene(scene);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    private void addMessagesToPane(List<Message> messages){

        if (messages == null) return;
        //if (ChatController.getUserDest() == null) return;

        for (Message message:messages) {

            if (ChatController.getUserDest() != null) {
                if (message.getUserOri().compareTo(ChatController.getUserDest()) == 0) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                getChatController().createMessagePanel(message, MessageController.MESSAGE_RECEIVED);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }

        }

    }

}