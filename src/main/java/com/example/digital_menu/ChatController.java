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

        // Start a thread to read incoming messages
        new Thread(() -> {
            while (true) {
                String message = networkConnection.read();
                if (message != null) {
                    Platform.runLater(() -> messageList.getItems().add(message));
                }
            }
        }).start();
    }

    @FXML
    private void sendMessage() {
        String message = messageInput.getText();
        if (!message.isEmpty()) {
            networkConnection.write(message);
            messageInput.clear();
        }
    }
}
