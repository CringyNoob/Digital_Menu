// Updated OrderListController.java
package com.example.digital_menu;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderListController {

    @FXML private ListView<HBox> orderListView;
    @FXML private Button confirmOrderButton;
    @FXML private Label totalItemsLabel;

    private final List<String> orderList = new ArrayList<>();
    private int userId;

    public void setOrderList(List<String> items, int userId) {
        this.userId = userId;
        orderList.addAll(items);
        updateOrderListView();
    }

    private void updateOrderListView() {
        orderListView.getItems().clear();

        for (String item : orderList) {
            HBox itemBox = new HBox(10);
            Label itemName = new Label(item);
            itemName.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
            Button removeButton = new Button("Remove");

            removeButton.setOnAction(e -> {
                orderList.remove(item);
                updateOrderListView();
            });

            itemBox.getChildren().addAll(itemName, removeButton);
            orderListView.getItems().add(itemBox);
        }

        totalItemsLabel.setText("Total Items: " + orderList.size());
    }

    @FXML
    public void confirmOrder() {
        if (orderList.isEmpty()) {
            showAlert("Error", "Order list is empty!");
            return;
        }

        String orderItems = orderList.stream().collect(Collectors.joining(", "));

        try (Connection conn = DatabaseUtil.getConnection()) {
            String query = "INSERT INTO orders (user_id, order_items) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            stmt.setString(2, orderItems);
            stmt.executeUpdate();

            showAlert("Success", "Order placed successfully!");
            orderList.clear();
            updateOrderListView();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to place order: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
