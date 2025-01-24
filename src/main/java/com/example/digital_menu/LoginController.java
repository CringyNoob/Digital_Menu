package com.example.digital_menu;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {
    @FXML private TextField mobileField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    public void handleLogin() {
        String mobile = mobileField.getText();
        String password = passwordField.getText();

        if (mobile.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        try (Connection conn = DatabaseUtil.getConnection()) {
            String query = "SELECT user_id, name FROM user WHERE mobile = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, mobile);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("user_id");
                String userName = rs.getString("name");
                loadMenuScene(userId, userName);
            } else {
                showAlert("Error", "Invalid mobile number or password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database error: " + e.getMessage());
        }
    }

    private void loadMenuScene(int userId, String userName) {
        try {
            // Load the menu.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menu.fxml"));
            Parent root = loader.load();

            // Access the MenuController to pass user data
            MenuController menuController = loader.getController();
            menuController.setUserId(userId);
            menuController.setUserName(userName);

            // Set the new scene in the current stage
            Stage stage = (Stage) mobileField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Digital Menu - Welcome " + userName);
            stage.show(); // Make sure to show the stage after setting the scene
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the menu scene: " + e.getMessage());
        }
    }

    @FXML
    public void goToSignup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("signup.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) mobileField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load signup page.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
