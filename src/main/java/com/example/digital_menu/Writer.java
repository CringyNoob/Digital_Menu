package com.example.digital_menu;

import java.util.Scanner;

public class Writer implements Runnable {
    private final NetworkConnection netConnection;

    public Writer(NetworkConnection nc) {
        netConnection = nc;
    }

    @Override
    public void run() {
        Scanner in = new Scanner(System.in);
        Data data = new Data();

        while (true) {
            String inputMessage = in.nextLine();
            data.message = inputMessage;

            try {
                netConnection.write(data.clone());
            } catch (Exception ex) {
                System.err.println("Failed to send message: " + ex.getMessage());
            }
        }
    }
}
