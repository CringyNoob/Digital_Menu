package com.example.digital_menu;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.*;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.digital_menu.ServerMain.clientList;

public class AdminController {
    @FXML private TableView<Order> orderTable;
    @FXML private TableColumn<Order, Integer> orderIdColumn;
    @FXML private TableColumn<Order, Integer> userIdColumn;
    @FXML private TableColumn<Order, String> orderItemsColumn;
    @FXML private TableColumn<Order, Timestamp> orderAtColumn;
    @FXML private ListView<String> messageList;
    @FXML private TextField messageInput;

    private ObservableList<Order> orders = FXCollections.observableArrayList();

    public void initialize() {
        // Setup Table Columns
        orderIdColumn.setCellValueFactory(cellData -> cellData.getValue().orderIdProperty().asObject());
        userIdColumn.setCellValueFactory(cellData -> cellData.getValue().userIdProperty().asObject());
        orderItemsColumn.setCellValueFactory(cellData -> cellData.getValue().orderItemsProperty());
        orderAtColumn.setCellValueFactory(cellData -> cellData.getValue().orderAtProperty());

        orderTable.setItems(orders);



        // Start updating orders and messages in real-time
        startOrderUpdates();
        startMessageUpdates();
    }

    private void startOrderUpdates() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> updateOrders());
            }
        }, 0, 5000); // Fetch updates every 5 seconds
    }

    private void updateOrders() {
        try (Connection conn = DatabaseUtil.getConnection()) {
            String query = "SELECT * FROM orders";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            orders.clear();
            while (rs.next()) {
                orders.add(new Order(
                        rs.getInt("order_id"),
                        rs.getInt("user_id"),
                        rs.getString("order_items"),
                        rs.getTimestamp("order_at")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void startMessageUpdates() {
        // Start a background thread for handling incoming messages
        new Thread(() -> {
            while (true) {
                synchronized (ServerMain.clientList) {
                    for (Information info : ServerMain.clientList.values()) {
                        try {
                            // Read incoming messages
                            Object obj = info.netConnection.read();
                            if (obj instanceof Data) {
                                Data data = (Data) obj;
                                String message = data.message;

                                // Update the admin GUI on the JavaFX Application Thread
                                Platform.runLater(() -> messageList.getItems().add(message));
                            }
                        } catch (Exception e) {
                            System.err.println("Error receiving message: " + e.getMessage());
                        }
                    }
                }

                // Sleep briefly to prevent excessive CPU usage
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.err.println("Message update thread interrupted: " + e.getMessage());
                }
            }
        }).start();
    }

    @FXML
    private void sendMessage() {
        String message = messageInput.getText();
        if (!message.isEmpty()) {
            Data data = new Data();
            data.message = "Admin: " + message;

            // Update the admin GUI
            messageList.getItems().add(data.message);

            // Send the message to all connected clients
            new Thread(() -> {
                synchronized (ServerMain.clientList) {
                    for (Information info : ServerMain.clientList.values()) {
                        info.netConnection.write(data);
                    }
                }
            }).start();

            messageInput.clear();
        }
    }
}



