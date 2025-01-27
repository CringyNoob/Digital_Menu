package com.example.digital_menu;

import java.util.HashMap;

public class CreateConnection implements Runnable {
    private final HashMap<String, Information> clientList;
    private final NetworkConnection nc;

    public CreateConnection(HashMap<String, Information> cList, NetworkConnection nConnection) {
        clientList = cList;
        nc = nConnection;
    }

    @Override
    public void run() {
        try {
            // Read the first object (expected to be a Data object)
            Object obj = nc.read();

            if (obj instanceof Data) {
                Data data = (Data) obj;
                String username = data.message;

                System.out.println("User: " + username + " connected");

                // Add the client to the client list
                clientList.put(username, new Information(username, nc));

                // Start a thread to handle communication with the client
                new Thread(new ReaderWriterServer(username, nc, clientList)).start();
            } else {
                System.err.println("Received unsupported object type: " + obj.getClass());
            }
        } catch (Exception e) {
            System.err.println("Error in CreateConnection: " + e.getMessage());
        }
    }
}
