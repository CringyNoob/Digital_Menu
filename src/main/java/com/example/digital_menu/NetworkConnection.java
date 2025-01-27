package com.example.digital_menu;

import java.io.*;
import java.net.Socket;

public class NetworkConnection {
    private final Socket socket;
    private final BufferedReader reader;
    private final BufferedWriter writer;

    public NetworkConnection(Socket socket) throws IOException {
        this.socket = socket;
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public NetworkConnection(String ip, int port) throws IOException {
        this(new Socket(ip, port));
    }

    public void write(String message) {
        try {
            writer.write(message);
            writer.newLine(); // Ensure proper message termination
            writer.flush();
            System.out.println("Sent: " + message);
        } catch (IOException e) {
            System.err.println("Failed to write: " + e.getMessage());
        }
    }

    public String read() {
        try {
            String message = reader.readLine(); // Read a single line
            System.out.println("Received: " + message);
            return message;
        } catch (IOException e) {
            System.err.println("Failed to read: " + e.getMessage());
            return null;
        }
    }

    public Socket getSocket() {
        return socket;
    }
}
