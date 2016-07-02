package com.rackian.models;

import java.io.Serializable;

public class User implements Serializable, Comparable<User> {

    private String email;
    private String nick;
    private String password;
    private String ip;
    private boolean online;

    public User() {
    }

    public User(String email, String nick, String ip, boolean online) {
        this.email = email;
        this.nick = nick;
        this.ip = ip;
        this.online = online;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int compareTo(User o) {
        return this.getEmail().compareTo(o.getEmail());
    }

}
