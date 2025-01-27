package com.example.digital_menu;

import java.util.HashMap;

public class ReaderWriterServer implements Runnable {
    private final String username;
    private final NetworkConnection nc;
    private final HashMap<String, Information> clientList;

    public ReaderWriterServer(String username, NetworkConnection nc, HashMap<String, Information> clientList) {
        this.username = username;
        this.nc = nc;
        this.clientList = clientList;
    }

    private void broadcast(String message) {
        synchronized (clientList) {
            for (Information info : clientList.values()) {
                info.netConnection.write(message);
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            String message = nc.read();
            if (message != null) {
                System.out.println("Message from " + username + ": " + message);

                // Broadcast the message to all clients
                broadcast(username + ": " + message);
            } else {
                System.err.println("Message from client " + username + " is null.");
            }
        }
    }
}
