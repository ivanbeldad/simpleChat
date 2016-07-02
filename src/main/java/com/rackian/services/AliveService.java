package com.rackian.services;

import com.rackian.Main;
import com.rackian.models.User;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class AliveService implements Runnable {
/*
    public void reportOn() {

        try {

            Socket socket;
            OutputStream outputStream;
            ObjectOutputStream objectOutputStream;

            User user;
            user = new User();
            user.setNick("Akimah");

            System.out.println("Informando conexion");
            socket = new Socket(Main.SERVER_IP, Main.PORT_ALIVE);
            outputStream = socket.getOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);

            objectOutputStream.writeObject(user);

            objectOutputStream.close();
            outputStream.close();
            socket.close();

        } catch (Exception ex) {
        }

        listen();

    }

    private void listen (){

        Thread listenThread;
        Runnable listenRunnable;

        listenRunnable = new Listen();

        listenThread = new Thread(listenRunnable);
        listenThread.setDaemon(true);
        listenThread.start();

    }
*/
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

        try {
            serverSocket = new ServerSocket(port);

            while (true) {
                socket = serverSocket.accept();
                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

/*
class Listen implements Runnable {

    @Override
    public void run() {
        try {
            listening();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listening() throws IOException {

        ServerSocket serverSocket;
        Socket socket;

        serverSocket = new ServerSocket(10001);

        while (true) {
            socket = serverSocket.accept();
        }

    }

}
*/