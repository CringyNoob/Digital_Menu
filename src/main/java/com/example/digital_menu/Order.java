package com.example.digital_menu;

import javafx.beans.property.*;

import java.sql.Timestamp;

public class Order {
    private final IntegerProperty orderId;
    private final IntegerProperty userId;
    private final StringProperty orderItems;
    private final ObjectProperty<Timestamp> orderAt;

    public Order(int orderId, int userId, String orderItems, Timestamp orderAt) {
        this.orderId = new SimpleIntegerProperty(orderId);
        this.userId = new SimpleIntegerProperty(userId);
        this.orderItems = new SimpleStringProperty(orderItems);
        this.orderAt = new SimpleObjectProperty<>(orderAt);
    }

    public IntegerProperty orderIdProperty() {
        return orderId;
    }

    public IntegerProperty userIdProperty() {
        return userId;
    }

    public StringProperty orderItemsProperty() {
        return orderItems;
    }

    public ObjectProperty<Timestamp> orderAtProperty() {
        return orderAt;
    }
}
