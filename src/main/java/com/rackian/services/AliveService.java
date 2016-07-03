package com.rackian.services;

import com.rackian.Main;
import com.rackian.models.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;


public class AliveService implements Runnable {

    List<User> onlineUsers;

    private int port;

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

    @Override
    public void run() {

        ServerSocket serverSocket;
        Socket socket;
        InputStream is;
        ObjectInputStream ois;


        try {
            serverSocket = new ServerSocket(port);

            while (true) {
                socket = serverSocket.accept();

                try {
                    is = socket.getInputStream();
                    ois = new ObjectInputStream(is);
                    onlineUsers = (List<User>) ois.readObject();
                    System.out.println("Nuevas actualizaciones");
                } catch (Exception ex) {

                }

                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}