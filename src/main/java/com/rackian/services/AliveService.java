package com.rackian.services;

import com.rackian.Main;
import com.rackian.controllers.ChatController;
import com.rackian.models.User;
import com.sun.org.apache.xpath.internal.SourceTree;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class AliveService implements Runnable {

    List<User> onlineUsers;

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

        ServerSocket serverSocket;
        Socket socket;
        InputStream is;
        ObjectInputStream ois;

        System.out.println("Inicio de servicio de comprobación de estado.");
        try {
            serverSocket = new ServerSocket(port);

            while (true) {
                socket = serverSocket.accept();
                serverSocket.setSoTimeout(2000);

                is = socket.getInputStream();
                ois = new ObjectInputStream(is);
                if ((boolean)ois.readObject()) {
                    onlineUsers = (List<User>) ois.readObject();
                    chatController.setContacts(onlineUsers);
                    chatController.updateOnlineContacts();
                    System.out.println("Cambios en los contactos");
                } else {
                    System.out.println("Sin cambios.");
                }

                socket.close();
            }

        } catch (IOException e) {

            // MANDO AL LOGIN POR CONEXIÓN PERDIDA
            System.out.println("Conexión perdida");

            Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    try {
                    FXMLLoader fxml = new FXMLLoader(getClass().getClassLoader().getResource("login.fxml"));
                    Pane rootPane = (Pane) fxml.load();
                    Scene scene = new Scene(rootPane);
                    Main.stage.setScene(scene);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}