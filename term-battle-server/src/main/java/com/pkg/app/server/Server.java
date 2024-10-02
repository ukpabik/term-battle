package com.pkg.app.server;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import com.pkg.app.rooms.Room;

// Server class for the Terminal Battle
public class Server implements Runnable {

  // Server fields (serverSocket and list of clients connected)
  private ServerSocket serverSocket;
  // Synchronized list of clients
  private List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());
  private Map<String, Room> rooms = new HashMap<>();

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
  public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String clientName;
    private Room currentRoom;

    public ClientHandler(Socket socket) {
      this.socket = socket;
      try {
        // Initialize input and output streams
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // Set the client's name and broadcast that they have joined the server.
        this.clientName = in.readLine();
        System.out.println(clientName + " has connected to the server.");

        globalBroadcast(clientName + " has connected to the server.", this);
        //TODO: Which way should I do it?
//        globalBroadcast(clientName + " has connected to the server.", this);
        this.setCurrentRoom(null);
      } catch (IOException e) {
        System.out.println("Error initializing client handler: " + e.getMessage());
        closeConnection();
      }
    }



    // Runs the client-handler to allow for synchronous activity across the server
    @Override
    public void run() {
      String message;
      try {
        // Listen for messages from the client
        while ((message = in.readLine()) != null) {
          if (message.equalsIgnoreCase("exit")){
            break;
          }
          handleCommand(message, clientName);
        }
      } catch (IOException e) {
        System.out.println("Connection lost with " + clientName);
      } finally {
        System.out.println("Closing the connection with: " + clientName);
        closeConnection();
      }
    }

    // Sends a message to the client
    public void sendMessage(String message) {
      out.println(message);
    }

    public void sendGlobalMessage(String message){
      out.println("[Global] " + message);
    }
    public void sendRoomMessage(String message){
      out.println("[Room] " + message);
    }
    public void sendSystemMessage(String message){
      out.println("[System] " + message);
    }

  //TODO: Implement command logic
  private void handleCommand(String command, String clientName){
    //TODO: Implement other commands logic: options, party, etc.
    if (command.startsWith("/join ")){
      String roomName = command.substring(6);
      joinRoom(roomName);
    }
    else if (command.startsWith("/create ")){
      String roomName = command.substring(8);
      createRoom(roomName);
      joinRoom(roomName);
    }
    else if (command.startsWith("/leave")){
      leaveCurrentRoom();
    }
    else if (command.strip().equals("/rooms")){
      listRooms();
    }
    else {
        System.out.println(clientName + ": " + command);
        if (getCurrentRoom() != null){
          currentRoom.roomBroadcast(clientName + ": " + command, this);
        }
        else{
          globalBroadcast(clientName + ": " + command, this);
        }
    }
  }

  // Create room function
  private void createRoom(String roomName){
    synchronized (rooms) {
      if (!rooms.containsKey(roomName)){
        Room room = new Room(roomName);
        rooms.put(roomName, room);
        sendSystemMessage("Room '" + roomName + "' created.");
        System.out.println("Room '" + roomName + "' created by " + clientName);
      }
      else {
        sendSystemMessage("Room '" + roomName + "' already exists.");
        System.out.println("Room creation failed: '" + roomName + "' already exists.");
      }
    }
  } 
  // Function to join a room
  private void joinRoom(String roomName){
    synchronized (rooms) {
      Room room = rooms.get(roomName);
      if (room != null){
        if (room.isFull()){
          sendSystemMessage("Room '" + roomName + "' is full.");
          System.out.println("Join failed: Room '" + roomName + "' is full.");
        }
        else{
          if (getCurrentRoom() != null){
            leaveCurrentRoom();
          }
          if (room.addClient(this)){
            setCurrentRoom(room);            
            sendSystemMessage("Joined room '" + roomName + "'.");
            System.out.println(clientName + " joined room " + roomName);
          }
          else{
            sendSystemMessage("Unable to join " + roomName);
            System.out.println("Join failed: Unable to join room '" + roomName + "'");
          }
        }
      }
      else{
        sendSystemMessage("Room '" + roomName + "' does not exist.");
        System.out.println("Join failed: Room '" + roomName + "' does not exist.");
      }
    }
  }  
  // Function to leave a room
  private void leaveCurrentRoom(){
    synchronized(rooms){
      if (getCurrentRoom() != null){
        Room room = getCurrentRoom();
        room.removeClient(this);

        sendSystemMessage("You have left the room.");
        if (room.getClientCount() == 0){
          rooms.remove(room.getRoomName());
          System.out.println("Room '" + room.getRoomName() +"' has been deleted because it is empty.");
        }
        setCurrentRoom(null); 
      }
      else{
        sendSystemMessage("You are not in any room.");
        System.out.println("User '" + clientName + "' attempted to leave a room but was not in any.");
      }
    }
  }
  // Function to list all rooms
  private void listRooms(){
    synchronized (rooms){
      if (rooms.isEmpty()){
        sendMessage("No rooms available.");
      }
      else {
        sendSystemMessage("Available rooms:");
        for (Room room : rooms.values()) {
          sendMessage("- " + room.getRoomName() + " (" + room.getClientCount() + "/2)");
        }
      }
    }
  }

  public synchronized Room getCurrentRoom(){
    return this.currentRoom;
  }

  public synchronized void setCurrentRoom(Room room){
    this.currentRoom = room;
  }
  public String getClientName(){
    return this.clientName;
  }

    // Closes the connection and removes the client from the list
    public void closeConnection() {
      try {
        if (clientName != null) {
          System.out.println(clientName + " has disconnected.");
          sendSystemMessage(clientName + " has disconnected.");
        }
        leaveCurrentRoom();
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
  private void globalBroadcast(String message, ClientHandler excludeClient) {
    // Since all users are on different threads
    // Synchronize the message being sent across all users
    if (excludeClient.getCurrentRoom() == null){
      synchronized (clients) {
        for (ClientHandler client : clients) {
          if (client != excludeClient && client.currentRoom == null) {
            client.sendGlobalMessage(message);
          }
        }
      }
    }
  }



 
}

