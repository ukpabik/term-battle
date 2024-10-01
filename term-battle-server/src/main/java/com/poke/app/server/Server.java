package com.poke.app.server;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

// Server class for the Terminal Battle
public class Server implements Runnable {

    // Server fields (serverSocket and list of clients connected)
    private ServerSocket serverSocket;
    private List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());

    // Initializes server with given port
    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    // Returns the list of connected clients
    public List<ClientHandler> getClients() {
        return clients;
    }

    // Starts the server
    public void run() {
        try {
            start();
        } catch (IOException e) {
            System.out.println("Unable to start server: " + e.getMessage());
        }
    }

    // Accepts client connections and starts ClientHandler threads
    public void start() throws IOException {
        System.out.println("Server started. Waiting for clients...");
        while (true) {
            Socket clientSocket = serverSocket.accept();
            ClientHandler clientHandler = new ClientHandler(clientSocket);
            clients.add(clientHandler);
            System.out.println("Client connected: " + clientSocket.getInetAddress());
            System.out.println("Total Connections: " + clients.size());
            // Starts a new thread each time a new user joins
            // Each user is ran on their own thread
            new Thread(clientHandler).start();
        }
    }

    // Stops the server
    public void stop() throws IOException {
        // Closes the serverSocket
        serverSocket.close();
        // Since each client is run on its own thread
        // Synchronize closing the connections between all clients
        synchronized (clients) {
            for (ClientHandler client : clients) {
                client.closeConnection();
            }
        }
    }

    // Inner class to handle client communication
    private class ClientHandler implements Runnable {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String clientName;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                // Initialize input and output streams
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Prompt client for a name
                out.println("Enter your name:");
                this.clientName = in.readLine();
                broadcast(clientName + " has joined the chat!", this);
            } catch (IOException e) {
                System.out.println("Error initializing client handler: " + e.getMessage());
                closeConnection();
            }
        }

        @Override
        public void run() {
            String message;
            try {
                // Listen for messages from the client
                while ((message = in.readLine()) != null) {
                    if (message.equalsIgnoreCase("exit")) {
                        break;
                    }
                    System.out.println(clientName + ": " + message);
                    broadcast(clientName + ": " + message, this);
                }
            } catch (IOException e) {
                System.out.println("Connection lost with " + clientName);
            } finally {
                closeConnection();
            }
        }

        // Sends a message to the client
        public void sendMessage(String message) {
            out.println(message);
        }

        // Closes the connection and removes the client from the list
        public void closeConnection() {
            try {
                if (clientName != null) {
                    System.out.println(clientName + " has disconnected.");
                    broadcast(clientName + " has left the chat.", this);
                }
                clients.remove(this);
                if (in != null) in.close();
                if (out != null) out.close();
                if (socket != null && !socket.isClosed()) socket.close();
                System.out.println("Total Connections: " + clients.size());
            } catch (IOException e) {
                System.out.println("Error closing connection for " + clientName + ": " + e.getMessage());
            }
        }
    }

    // Broadcasts a message to all clients except the sender
    private void broadcast(String message, ClientHandler excludeClient) {
        // Since all users are on different threads
        // Synchronize the message being sent across all users
        synchronized (clients) {
            for (ClientHandler client : clients) {
                if (client != excludeClient) {
                    client.sendMessage(message);
                }
            }
        }
    }

}

