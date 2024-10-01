package com.poke.app.server;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


// Server class for the Terminal Battle
public class Server implements Runnable {

  // Server fields (serverSocket and list of clients connected)
  private ServerSocket serverSocket;
  private List<Socket> clients = new ArrayList<>();

  // Initializes server with given port
  public Server(int port) throws IOException {
    serverSocket = new ServerSocket(port);
  }

  // Returns the list of connected clients
  public List<Socket> getClients() {
    return clients;
  }

  //TODO: implement
  public void start() throws IOException {
    while (true){
      Socket client = serverSocket.accept();
      clients.add(client);
      System.out.println("Client connected");
      System.out.println("Total Connections: " + clients.size());
    }
  }

  // Starts the server
  public void run() {
    try{
      start();
    }
    catch (IOException e){
      System.out.println("Unable to start server");
    }
  }

  // Stops the server
  public void stop() throws IOException {
    serverSocket.close();
  }
}
