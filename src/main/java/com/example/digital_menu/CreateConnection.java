package com.example.digital_menu;

import java.util.HashMap;

public class CreateConnection implements Runnable {
    private final HashMap<String, Information> clientList;
    private final NetworkConnection nc;

    public CreateConnection(HashMap<String, Information> clientList, NetworkConnection nc) {
        this.clientList = clientList;
        this.nc = nc;
    }

    @Override
    public void run() {
        try {
            // Read the username from the client
            String username = nc.read();
            if (username != null) {
                System.out.println("User: " + username + " connected");

                // Add the client to the client list
                clientList.put(username, new Information(username, nc));

                // Start a thread to handle communication with the client
                new Thread(new ReaderWriterServer(username, nc, clientList)).start();
            } else {
                System.err.println("Failed to read username during connection setup.");
            }
        } catch (Exception e) {
            System.err.println("Error in CreateConnection: " + e.getMessage());
        }
    }
}
