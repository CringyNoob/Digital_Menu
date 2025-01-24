// Updated MenuController.java
package com.example.digital_menu;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

public class MenuController {

    @FXML private Label userNameLabel;
    @FXML private VBox menuContainer;
    @FXML private ListView<String> menuItemsListView;
    @FXML private Button logoutButton;

    private int userId;
    private String userName;

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
        // Debugging Output
        System.out.println("MenuController initialized");
        System.out.println("userNameLabel: " + userNameLabel);

        // Add placeholder menu items
        menuItemsListView.getItems().addAll("Pizza", "Burger", "Pasta", "Fries", "Salad");
    }

    @FXML
    public void logout() {
        System.out.println("User logged out");
        // Handle logout logic here, such as redirecting to the login screen.
    }
}
