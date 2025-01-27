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
    private String currentClientUsername;
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

    public void displayClientMessage(String message, String username) {
        Platform.runLater(() -> {
            messageList.getItems().add(message); // Display the message in the admin chat
            currentClientUsername = username;   // Update the current client for admin replies
        });
    }

    @FXML
    private void sendMessage() {
        String message = messageInput.getText();
        if (!message.isEmpty() && currentClientUsername != null) {
            String adminMessage = "Admin: " + message;

            // Display the admin's message in the GUI
            messageList.getItems().add(adminMessage);

            // Send the message only to the specific client
            new Thread(() -> {
                Information clientInfo = ServerMain.clientList.get(currentClientUsername);
                if (clientInfo != null) {
                    clientInfo.netConnection.write(adminMessage);
                } else {
                    System.err.println("No client found with username: " + currentClientUsername);
                }
            }).start();

            messageInput.clear();
        }
    }
}