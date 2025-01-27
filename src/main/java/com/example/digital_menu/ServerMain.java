package com.example.digital_menu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServerMain extends Application {
    private static final int PORT = 12345;
    static HashMap<String, Information> clientList = new HashMap<>();
    static AdminController adminController; // Reference to the admin controller

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Admin.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setTitle("Admin Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();

        adminController = loader.getController(); // Get the admin controller

        startServer(); // Start the server
    }

    private void startServer() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                System.out.println("Server Started...");
                while (true) {
                    Socket socket = serverSocket.accept();
                    NetworkConnection nc = new NetworkConnection(socket);

                    // Handle the new client connection
                    new Thread(new CreateConnection(clientList, nc)).start();
                }
            } catch (Exception e) {
                System.err.println("Error in ServerMain: " + e.getMessage());
            }
        }).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
