package com.rackian.controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.rackian.Main;
import com.rackian.models.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class RegisterController {

    private User user;

    @FXML
    private JFXTextField email;
    @FXML
    private JFXTextField nick;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXPasswordField repeatPassword;

    @FXML
    private void handleRegister() throws IOException, ClassNotFoundException {

        if (noErrors()) {

            user = new User();
            user.setEmail(email.getText());
            user.setNick(nick.getText());
            user.setPassword(password.getText());

            Socket socket;
            OutputStream os;
            ObjectOutputStream oos;
            InputStream is;
            ObjectInputStream ois;

            socket = new Socket(Main.SERVER_IP, Main.PORT_REGISTER);
            os = socket.getOutputStream();
            oos = new ObjectOutputStream(os);
            is = socket.getInputStream();
            ois = new ObjectInputStream(is);

            oos.writeObject(user);

            AlertPanel alertPanel;
            alertPanel = new AlertPanel();
            String message;
            Alert.AlertType alertType;

            if ((boolean) ois.readObject()) {
                System.out.println("Registrado con éxito");
                message = "Registrado con éxito. Vuelva a la página de login para iniciar sesión.";
                alertType = Alert.AlertType.INFORMATION;
            } else {
                System.out.println("No se ha podido registrar");
                message = "El correo ya está registrado.";
                alertType = Alert.AlertType.WARNING;
            }
            alertPanel.setMessage(message);
            alertPanel.setAlertType(alertType);
            alertPanel.show();
            socket.close();

        }
    }

    @FXML
    private void handleRegisterKey(KeyEvent keyEvent) throws Exception {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            handleRegister();
        }
    }

    @FXML
    private void handleGoLogin() throws IOException {

        FXMLLoader loader;
        Pane rootPane;
        loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("login.fxml"));
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
        if (repeatPassword.getText().replaceAll(" ", "").equals("")) {
            repeatPassword.requestFocus();
            repeatPassword.setFocusColor(Paint.valueOf("#FF5252"));
            return false;
        }
        if (!password.getText().equals(repeatPassword.getText())) {
            password.setText("");
            repeatPassword.setText("");
            password.requestFocus();
            return false;
        }
        return true;
    }

}
