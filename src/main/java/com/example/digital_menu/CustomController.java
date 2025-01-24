package com.example.digital_menu;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CustomController {
    @FXML private ComboBox<String> sizeSelector;
    @FXML private ComboBox<String> sauceSelector;
    @FXML private ListView<String> toppingsSelector;
    @FXML private Label confirmationLabel;

    public void initialize() {
        sizeSelector.getItems().addAll("Small", "Medium", "Large");
        sauceSelector.getItems().addAll("Tomato", "Cream", "Pesto");
        toppingsSelector.getItems().addAll("Cheese", "Pepperoni", "Olives", "Mushrooms");
    }

    public void addToOrder() {
        String size = sizeSelector.getValue();
        String sauce = sauceSelector.getValue();
        String toppings = toppingsSelector.getSelectionModel().getSelectedItems().toString();

        if (size != null && sauce != null && !toppings.isEmpty()) {
            confirmationLabel.setText("Dish added to order!");
        } else {
            confirmationLabel.setText("Please select all options.");
        }
    }
}
