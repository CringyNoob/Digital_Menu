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
                Object obj = networkConnection.read();
                if (obj instanceof Data) {
                    Data data = (Data) obj;
                    Platform.runLater(() -> messageList.getItems().add(data.message));
                }
            }
        }).start();
    }

    @FXML
    private void sendMessage() {
        String message = messageInput.getText();
        if (!message.isEmpty()) {
            Data data = new Data();
            data.message = networkConnection.username + ": " + message;

            // Write the `Data` object to the server
            networkConnection.write(data);
            messageInput.clear();
        }
    }



    public ListView<String> getMessageList() {
        return messageList;
    }
}
