package com.example.digital_menu;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.List;

public class OrderListController {

    @FXML private ListView<String> orderListView;

    public void setOrderList(List<String> orderList) {
        orderListView.getItems().addAll(orderList);
    }
}
