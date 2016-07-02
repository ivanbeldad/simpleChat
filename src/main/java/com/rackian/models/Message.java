package com.rackian.models;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {

    private String nick;
    private String message;
    private LocalDateTime time;
    private String ip;

    public Message() {
    }

    public Message(String nick, String message, LocalDateTime time) {
        this.nick = nick;
        this.message = message;
        this.time = time;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getTimeString() {
        String time;
        time = "";

        if(this.time.getHour() < 10) {
            time += "0";
        }
        time += this.time.getHour();

        time += ":";

        if (this.time.getMinute() < 10) {
            time += "0";
        }
        time += this.time.getMinute();

        return time;
    }

}
