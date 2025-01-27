package com.example.digital_menu;

import java.util.HashMap;

public class ReaderWriterServer implements Runnable {
    private final String username;
    private final NetworkConnection nc;
    private final HashMap<String, Information> clientList;

    public ReaderWriterServer(String username, NetworkConnection netConnection, HashMap<String, Information> clientList) {
        this.username = username;
        this.nc = netConnection;
        this.clientList = clientList;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Object obj = nc.read();
                if (obj instanceof Data) {
                    Data dataObj = (Data) obj;
                    System.out.println("Received from " + username + ": " + dataObj.message);

                    // Broadcast the message to all clients
                    broadcast(dataObj);
                } else {
                    System.err.println("Unsupported object received from " + username + ": " + obj.getClass());
                }
            } catch (Exception e) {
                System.err.println("Error in ReaderWriterServer: " + e.getMessage());
                break; // Exit the loop if there's a critical error
            }
        }
    }

    private void broadcast(Data data) {
        for (Information info : clientList.values()) {
            info.netConnection.write(data);
        }
    }


}
