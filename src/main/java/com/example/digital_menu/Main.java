package com.example.digital_menu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.ListView;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setTitle("Digital Menu");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void startClient(String username) {
        try {
            // Initialize NetworkConnection
            NetworkConnection nc = new NetworkConnection("localhost", 12345);
            nc.write(username);

            // Create a ListView for chat messages
            ListView<String> messageList = new ListView<>();

            // Start Reader and Writer threads
            Thread readerThread = new Thread(new Reader(nc, messageList));
            Thread writerThread = new Thread(new Writer(nc));

            readerThread.start();
            writerThread.start();

            readerThread.join();
            writerThread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
