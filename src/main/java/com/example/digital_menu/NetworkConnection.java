package com.example.digital_menu;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NetworkConnection {
    Socket socket;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    String username;

    public NetworkConnection(Socket sock) throws IOException {
        socket=sock;
        oos=new ObjectOutputStream(socket.getOutputStream());
        ois=new ObjectInputStream(socket.getInputStream());
    }

    public NetworkConnection(String ip,int port) throws IOException{
        socket=new Socket(ip, port);
        oos=new ObjectOutputStream(socket.getOutputStream());
        ois=new ObjectInputStream(socket.getInputStream());
    }

    public void write(Object obj) {
        try {
            oos.writeObject(obj);
            System.out.println("Sent: " + obj);
        } catch (IOException ex) {
            System.err.println("Failed to write: " + ex.getMessage());
        }
    }

    public Object read() {
        try {
            Object obj = ois.readObject();
            System.out.println("Received: " + obj);
            return obj;
        } catch (Exception ex) {
            System.err.println("Failed to read: " + ex.getMessage());
            return null;
        }
    }



    public Socket getSocket() {
        return socket;
    }
}