package com.rackian.controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.rackian.Main;
import com.rackian.models.Message;
import com.rackian.models.User;
import com.rackian.services.AliveService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

public class LoginController {

    private User user;
    private List<User> contacts;
    private List<Message> messages;

    @FXML
    private JFXTextField email;
    @FXML
    private JFXPasswordField password;
    @FXML
    private Label lAdvice;

    public LoginController() {
        this.user = new User();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    private void handleLogin() throws Exception {

        if (noErrors()) {

            User user;
            user = new User();
            user.setEmail(email.getText());
            user.setPassword(password.getText());

            sendLogin(user);

        }

    }

    @FXML
    private void handleLoginKey(KeyEvent keyCode) throws Exception {

        if (keyCode.getCode().equals(KeyCode.ENTER)) {
            handleLogin();
        }

    }

    @FXML
    private void handleGoRegister() throws IOException {

        FXMLLoader loader;
        Pane rootPane;
        loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("register.fxml"));
        rootPane = (Pane) loader.load();
        Scene scene = new Scene(rootPane);
        Stage stage = (Stage) email.getScene().getWindow();
        stage.setScene(scene);

    }

    private boolean noErrors() {
        if (email.getText().replaceAll(" ", "").equals("")) {
            email.requestFocus();
            email.setFocusColor(Paint.valueOf("#FF5252"));
            return false;
        }
        if (password.getText().replaceAll(" ", "").equals("")) {
            password.requestFocus();
            password.setFocusColor(Paint.valueOf("#FF5252"));
            return false;
        }
        return true;
    }

    private void sendLogin(User user) throws IOException, ClassNotFoundException {

        SendLogin sendLogin;
        sendLogin = new SendLogin(user);
        Thread thread;
        thread = new Thread(sendLogin);
        thread.start();

    }

    private void showLoginError() {

        Runnable labelAppear;
        labelAppear = new Runnable() {
            @Override
            public void run() {
                try {
                    while (lAdvice.getOpacity() < 0.9) {
                        Thread.sleep(10);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                lAdvice.setOpacity(lAdvice.getOpacity()+0.05);
                            }
                        });
                    }
                    Thread.sleep(1500);
                    while (lAdvice.getOpacity() > 0.0) {
                        Thread.sleep(10);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                lAdvice.setOpacity(lAdvice.getOpacity()-0.05);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        new Thread(labelAppear).start();

    }

    private class SendLogin implements Runnable {

        User user;

        public SendLogin(User user) {
            this.user = user;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        @Override
        public void run() {
            try {
                sendLogin();
            } catch (IOException ex) {
                AlertPanel alertPanel;
                alertPanel = new AlertPanel();
                alertPanel.setAlertType(Alert.AlertType.ERROR);
                alertPanel.setMessage("Error de conexión.");
                alertPanel.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void sendLogin() throws Exception {

            InetSocketAddress address;
            Socket socket;
            OutputStream os;
            InputStream is;
            ObjectOutputStream oos;
            ObjectInputStream ois;
            boolean auth;

            address = new InetSocketAddress(Main.SERVER_IP, Main.PORT_LOGIN);
            socket = new Socket();
            socket.connect(address, 1000);
            os = socket.getOutputStream();
            oos = new ObjectOutputStream(os);
            is = socket.getInputStream();
            ois = new ObjectInputStream(is);

            oos.writeObject(user);
            auth = (boolean) ois.readObject();

            if (auth) {
                System.out.println("Acceso autorizado");
                // READ USER
                user = (User) ois.readObject();
                // READ CONTACTS
                contacts = (List<User>) ois.readObject();
                for (int i = 0; i < contacts.size(); i++) {
                    if (user.compareTo(contacts.get(i)) == 0) {
                        contacts.remove(i);
                        break;
                    }
                }
                // READ MESSAGES
                messages = (List<Message>) ois.readObject();

                // GO TO CHAT APP
                ChatController chatController = new ChatController();
                chatController.setUser(user);
                chatController.setContacts(contacts);
                chatController.setMessages(messages);

                // START SERVICE
                AliveService aliveService;
                aliveService = new AliveService();
                aliveService.setChatController(chatController);
                aliveService.setPort(Main.PORT_ALIVE);
                Main.pool.execute(aliveService);

                // GO TO CHAT APP
                Stage stage = (Stage) email.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getClassLoader().getResource("chat.fxml"));
                loader.setController(chatController);
                Scene scene = new Scene(loader.load());
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        stage.setScene(scene);
                    }
                });

            } else {
                showLoginError();
                System.out.println("Acceso no autorizado");
            }

        }
    }

}

