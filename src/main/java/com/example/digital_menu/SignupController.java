package com.example.digital_menu;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class SignupController {
    @FXML private TextField mobileField;
    @FXML private TextField nameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    public void handleSignup() {
        String mobile = mobileField.getText();
        String fullName = nameField.getText();
        String password = passwordField.getText();

        if (mobile.isEmpty() || fullName.isEmpty() || password.isEmpty()) {
            errorLabel.setText("All fields are required.");
            return;
        }

        try (Connection conn = DatabaseUtil.getConnection()) {
            String query = "INSERT INTO user (mobile, name, password) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, mobile);
            stmt.setString(2, fullName);
            stmt.setString(3, password);

            stmt.executeUpdate();
            showAlert("Success", "Sign up successful! Please log in.");
            goToLogin();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Error during signup.");
        }
    }

    public void goToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Scene loginScene = new Scene(loader.load());
            Stage stage = (Stage) mobileField.getScene().getWindow();
            stage.setScene(loginScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
