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

    @Override
    public void run() {
        while (true) {
            String message = nc.read(); // Read a message from the client
            if (message != null) {
                String fullMessage = username + ": " + message;

                // Display the message in the server's terminal for debugging
                System.out.println("Message from " + username + ": " + message);

                // Pass the message to the admin for display
                ServerMain.adminController.displayClientMessage(fullMessage, username);
            } else {
                System.err.println("Message from client " + username + " is null.");
                break;
            }
        }
    }
}
