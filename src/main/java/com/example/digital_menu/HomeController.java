// Corrected HomeController.java
package com.example.digital_menu;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class HomeController {
    @FXML private VBox menuItemsContainer;
    @FXML private VBox cartContainer;
    @FXML private ComboBox<Integer> tableNumberComboBox;
    @FXML private Button orderButton;
    @FXML private Label userNameLabel;
    @FXML private GridPane popularGrid;
    @FXML private GridPane burgersGrid;
    @FXML private GridPane pizzasGrid;
    @FXML private GridPane sidesGrid;
    @FXML private GridPane dessertGrid;
    @FXML private GridPane beverageGrid;
    @FXML private Label totalPriceLabel;

    private int userId;
    private String userName;
    private final Map<String, Integer> cart = new HashMap<>();
    private final Map<String, Double> prices = new HashMap<>();
    private double totalPrice = 0.0;

    public void initialize() {
        initializeTableNumbers();
        populateMenu();
        setupCartContainer();
    }

    private void setupCartContainer() {
        totalPriceLabel = new Label("Total: $0.00");
        totalPriceLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        cartContainer.getChildren().add(totalPriceLabel);
    }

    private void populateMenu() {
        addMenuItem(popularGrid, "Classic Cheeseburger", "classic_burger.jpg", 8.99, 0, 0);
    }

    private void addMenuItem(GridPane grid, String name, String imagePath, double price, int row, int col) {
        prices.put(name, price);

        VBox itemBox = new VBox(10);
        itemBox.setAlignment(Pos.CENTER);
        itemBox.setPrefWidth(150);
        itemBox.getStyleClass().add("menu-item");

        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/com/example/digital_menu/img/" + imagePath)));
        imageView.setFitWidth(120);

        Label nameLabel = new Label(name);
        Label priceLabel = new Label(String.format("$%.2f", price));

        Button addButton = new Button("Add +");
        addButton.setOnAction(e -> addToCart(name, price));

        itemBox.getChildren().addAll(imageView, nameLabel, priceLabel, addButton);
        grid.add(itemBox, col, row);
    }

    private void initializeTableNumbers() {
        for (int i = 1; i <= 20; i++) {
            tableNumberComboBox.getItems().add(i);
        }
    }

    private void addToCart(String name, double price) {
        cart.put(name, cart.getOrDefault(name, 0) + 1);
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        if (userNameLabel != null) {
            userNameLabel.setText(userName);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
    @FXML
    public void placeOrder() {
        if (cart.isEmpty()) {
            showAlert("Error", "Your cart is empty!");
            return;
        }

        if (tableNumberComboBox.getValue() == null) {
            showAlert("Error", "Please select a table number!");
            return;
        }

        StringBuilder orderDetails = new StringBuilder();
        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            orderDetails.append(entry.getValue())
                    .append("x ")
                    .append(entry.getKey())
                    .append(", ");
        }

        try (Connection conn = DatabaseUtil.getConnection()) {
            String query = "INSERT INTO orders (user_id, table_number, order_items, total_price) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            stmt.setInt(2, tableNumberComboBox.getValue());
            stmt.setString(3, orderDetails.toString().trim());
            stmt.setDouble(4, totalPrice);
            stmt.executeUpdate();

            showAlert("Success", "Order placed successfully!");
            cart.clear();
            updateCartDisplay();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to place order: " + e.getMessage());
        }
    }

    private void updateCartDisplay() {
    }
}
