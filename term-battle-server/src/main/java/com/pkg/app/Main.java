package com.pkg.app;

import com.pkg.app.server.Server;
import java.io.IOException;


public class Main {
    public static void main(String[] args) {

        try {
            Server s = new Server(8080);

            // Start the server in a new thread to avoid blocking the main thread
            Thread serverThread = new Thread(s);
            serverThread.start();

        } catch (IOException e) {
            System.out.println("Unable to create server");
        }
    }
}
