package com.rackian.controllers;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertPanel {

    private String message;
    private AlertType alertType;

    public AlertPanel() {
    }

    public AlertPanel(String message, AlertType alertType) {
        this.message = message;
        this.alertType = alertType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AlertType getAlertType() {
        return alertType;
    }

    public void setAlertType(AlertType alertType) {
        this.alertType = alertType;
    }

    public void show() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert;
                alert = new Alert(alertType, message);
                alert.setHeaderText(null);
                alert.setResizable(false);
                alert.getDialogPane().setPadding(new Insets(10,15,10,15));
                alert.show();
            }
        });
    }

}
