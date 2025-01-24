package com.example.digital_menu;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenuController {

    @FXML private Label userNameLabel;
    @FXML private FlowPane menuContainer;

    private int userId;
    private String userName;

    private final List<String> orderList = new ArrayList<>();

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        if (userNameLabel != null) {
            userNameLabel.setText("Welcome, " + userName);
        }
    }

    @FXML
    public void initialize() {
        loadMenuItems();
    }

    private void loadMenuItems() {
        try (Connection conn = DatabaseUtil.getConnection()) {
            String query = "SELECT item_name, item_category, item_price, item_img FROM menu";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String itemName = rs.getString("item_name");
                String category = rs.getString("item_category");
                double price = rs.getDouble("item_price");
                String imgPath = rs.getString("item_img");

                // Create a card for each menu item
                VBox card = createMenuItemCard(itemName, category, price, imgPath);
                menuContainer.getChildren().add(card);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load menu items: " + e.getMessage());
        }
    }

    private VBox createMenuItemCard(String name, String category, double price, String imgPath) {
        VBox card = new VBox(10);
        card.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-padding: 10; -fx-alignment: center; -fx-background-color: #f9f9f9; -fx-border-width: 1;");

        ImageView imageView = new ImageView();
        try {
            Image image = new Image(getClass().getResourceAsStream("/images/" + imgPath));
            imageView.setImage(image);
        } catch (Exception e) {
            imageView.setImage(new Image(getClass().getResourceAsStream("/images/default.png"))); // Default image
        }
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);

        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label categoryLabel = new Label("Category: " + category);
        Label priceLabel = new Label(String.format("Price: $%.2f", price));

        Button addButton = new Button("+");
        addButton.setOnAction(e -> addToOrderList(name));

        card.getChildren().addAll(imageView, nameLabel, categoryLabel, priceLabel, addButton);
        return card;
    }

    private void addToOrderList(String itemName) {
        orderList.add(itemName);
        showAlert("Success", itemName + " added to order list.");
    }

    @FXML
    public void showOrderList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("order_list.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Order List");

            OrderListController controller = loader.getController();
            controller.setOrderList(orderList);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load order list: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
