package com.example.digital_menu;

import javafx.application.Platform;
import javafx.scene.control.ListView;

public class Reader implements Runnable {
    private final NetworkConnection netConnection;
    private final ListView<String> messageList;

    public Reader(NetworkConnection nc, ListView<String> messageList) {
        this.netConnection = nc;
        this.messageList = messageList;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Object obj = netConnection.read();
                if (obj instanceof Data) {
                    Data data = (Data) obj;
                    String message = data.message;

                    // Update chat window in JavaFX thread
                    Platform.runLater(() -> messageList.getItems().add(message));
                } else {
                    System.err.println("Unsupported object received: " + obj.getClass());
                }
            } catch (Exception e) {
                System.err.println("Error in Reader: " + e.getMessage());
            }
        }
    }
}
