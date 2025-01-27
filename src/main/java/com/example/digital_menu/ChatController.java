package com.example.digital_menu;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class ChatController {
    @FXML private ListView<String> messageList;
    @FXML private TextField messageInput;

    private NetworkConnection networkConnection;

    public void setNetworkConnection(NetworkConnection nc) {
        this.networkConnection = nc;

        // Start a thread to continuously read incoming messages
        new Thread(() -> {
            while (true) {
                String message = networkConnection.read();
                if (message != null) {
                    // Update the client chat on the JavaFX Application Thread
                    Platform.runLater(() -> messageList.getItems().add(message));
                }

                // Avoid excessive CPU usage
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.err.println("Client message reading thread interrupted: " + e.getMessage());
                }
            }
        }).start();
    }

    @FXML
    private void sendMessage() {
        String message = messageInput.getText();
        if (!message.isEmpty()) {
            networkConnection.write(message); // Send message to the server
            messageInput.clear();
        }
    }
}
