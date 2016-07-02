package com.rackian.controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.rackian.Main;
import com.rackian.models.User;
import com.rackian.services.AliveService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class LoginController {

    private User user;

    @FXML
    private JFXTextField email;
    @FXML
    private JFXPasswordField password;

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
            user = (User) ois.readObject();

            // GO TO CHAT APP
            ChatController controller = new ChatController();
            controller.setNick(user.getNick());
            Stage stage = (Stage) email.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("chat.fxml"));
            loader.setController(controller);
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);

            // START SERVICE
            Main.pool.execute(new AliveService(Main.PORT_ALIVE));
            System.out.println("Inicio de servicio de comprobación de estado.");

        } else {
            System.out.println("Acceso no autorizado");
        }

        socket.close();

    }

}
