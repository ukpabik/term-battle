package com.pkg.app.server;

import java.net.*;
import java.io.*;
import java.util.Map;
import com.pkg.app.rooms.Room;
import com.pkg.app.party.monster.Monster;
import com.pkg.app.party.Party;
import java.sql.SQLException;
import com.pkg.app.server.commands.CommandHandler;
import com.pkg.app.server.text.AnsiText;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import com.pkg.app.server.Logger;
import com.pkg.app.game.Game;

public class Server implements Runnable {

  // Server fields (serverSocket and set of clients connected)
  private ServerSocket serverSocket;
  private Set<ClientHandler> clients = new CopyOnWriteArraySet<>();
  private Map<String, Room> rooms = new ConcurrentHashMap<>();

  // Initializes server with given port
  public Server(int port) throws IOException {
    serverSocket = new ServerSocket(port);
  }

  // Returns the set of connected clients
  public Set<ClientHandler> getClients() {
    return clients;
  }

  // Starts the server
  public void run() {
    try {
      start();
    } catch (IOException e) {
      Logger.error("Unable to start server: " + e.getMessage());
    }
  }

  // Accepts client connections and starts ClientHandler threads
  public void start() throws IOException {
    Logger.info("Server started. Waiting for clients...");
    while (true) {
      Socket clientSocket = serverSocket.accept();
      ClientHandler clientHandler = new ClientHandler(clientSocket);
      clients.add(clientHandler);
      Logger.info("Client connected: " + clientSocket.getInetAddress());
      Logger.info("Total Connections: " + clients.size());
      // Starts a new thread each time a new user joins
      new Thread(clientHandler).start();
    }
  }

  // Stops the server
  public void stop() throws IOException {
    // Closes the serverSocket
    serverSocket.close();
    // Close connections for all clients
    for (ClientHandler client : clients) {
      client.closeConnection();
    }
  }

  // Inner class to handle client communication
  public class ClientHandler implements Runnable {
    private static final int MAX_ATTEMPTS = 3;
    private int attempts;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String clientName;
    private Room currentRoom;
    private Party party;
    private CommandHandler commandHandler;
    private boolean isReady = false;
    

    public ClientHandler(Socket socket) {
      this.socket = socket;
      this.party = new Party(Monster.getRandomMonsters());
      this.commandHandler = new CommandHandler();
      this.attempts = 0;
      try {
        // Initialize input and output streams
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);

        // Set the client's name
        this.clientName = in.readLine().strip();

        // Check if the user and password are in the system
        while (attempts < MAX_ATTEMPTS) {

          String password = in.readLine().strip();

          if (validateUser(clientName, password)) {
            out.println(AnsiText.color("Login successful... You are now connected!", AnsiText.GREEN));
            out.println(AnsiText.color("Type /help for a list of commands.", AnsiText.YELLOW));
            Logger.info(clientName + " has connected to the server.");
            globalBroadcast(clientName + " has connected to the server.", this);
            this.setCurrentRoom(null);
            return;
          } 
          else {
            attempts++;
            Logger.warning("Invalid login attempt for " + clientName + ", " + (MAX_ATTEMPTS - attempts) + " attempts left.");
            out.println(AnsiText.color("Invalid username or password. Please try again.", AnsiText.RED));
          }
        }

        Logger.error("Too many failed attempts for " + clientName + ". Disconnecting them now...");
        out.println(AnsiText.color("Too many login attempts. Please try again later.", AnsiText.RED));
        closeConnection();

      } 
      catch (IOException e) {
        Logger.error("Error initializing client handler: " + e.getMessage());
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
          message = message.strip();
          if (message.length() < 1) continue;
          if (message.equalsIgnoreCase("exit")) {
            break;
          }
          commandHandler.executeCommand(message, this);
        }
      } 
      catch (IOException e) {
        Logger.warning("Connection lost with " + clientName);
      } 
      finally {
        Logger.info("Closing the connection with: " + clientName);
        closeConnection();
      }
    }

    // Sends a message to the client
    public void sendMessage(String message) {
      out.println(message);
    }

    // Sends a global message
    public void sendGlobalMessage(String message) {
      out.println(AnsiText.color("[Global] ", AnsiText.GREEN) + message);
    }

    // Sends a message to users within a room
    public void sendRoomMessage(String message) {
      out.println(AnsiText.color("[Room] " + message, AnsiText.CYAN));
    }

    // Sends a message from the server
    public void sendSystemMessage(String message) {
      out.println(AnsiText.color("[System] " + message, AnsiText.RED));
    }

    // Sends a message from within the game
    public void sendGameMessage(String message) {
      out.println(AnsiText.color("[Game] " + message, AnsiText.PURPLE_BRIGHT));
    }

    // Returns the client's current party
    public Party getParty() {
      return this.party;
    }

    // Sets the client's party
    public void setParty(Party party) {
      this.party = party;
    }

    // Returns whether the client is ready or not
    public boolean getReady() {
      return isReady;
    }

    // Toggles whether the client is ready or not
    public void toggleReady() {
      this.isReady = !this.isReady;
      sendSystemMessage("You are now " + (isReady ? AnsiText.color("ready.", AnsiText.GREEN) : AnsiText.color("not ready.", AnsiText.RED)));
    }

    // Broadcasts a message to all clients except the sender
    public void globalBroadcast(String message, ClientHandler excludeClient) {
      if (excludeClient.getCurrentRoom() == null) {
        for (ClientHandler client : clients) {
          if (client != excludeClient && client.currentRoom == null) {
            client.sendGlobalMessage(message);
          }
        }
      }
    }

    // Function to create a room
    public void createRoom(String roomName) {
      if (roomName.length() > 0) {
        Room newRoom = new Room(roomName, this);
        if (rooms.putIfAbsent(roomName, newRoom) == null) {
          sendSystemMessage("Room '" + roomName + "' created.");
          Logger.info("Room '" + roomName + "' created by " + clientName);
          joinRoom(roomName);
        } 
        else {
          sendSystemMessage("Room '" + roomName + "' already exists.");
          Logger.warning("Room creation failed: '" + roomName + "' already exists.");
        }
      } 
      else {
        sendSystemMessage("Room name cannot be empty.");
        Logger.warning("Room creation failed: Room name cannot be empty.");
      }
    }

    // Function to join a room
    public void joinRoom(String roomName) {
      if (roomName.length() > 0) {
        Room room = rooms.get(roomName);
        if (room != null) {
          if (room.isFull()) {
            sendSystemMessage("Room '" + roomName + "' is full.");
            Logger.warning("Join failed: Room '" + roomName + "' is full.");
          } 
          else {
            if (getCurrentRoom() != null) {
              leaveCurrentRoom();
            }
            if (room.addClient(this)) {
              setCurrentRoom(room);
              sendSystemMessage("Joined room '" + roomName + "'.");
              Logger.info(clientName + " joined room " + roomName);
            } 
            else {
              sendSystemMessage("Unable to join " + roomName);
              Logger.error("Join failed: Unable to join room '" + roomName + "'");
            }
          }
        } 
        else {
          sendSystemMessage("Room '" + roomName + "' does not exist.");
          Logger.warning("Join failed: Room '" + roomName + "' does not exist.");
        }
      } 
      else {
        sendSystemMessage("Please enter a room name.");
        Logger.warning("Join failed: Please enter a room name.");
      }
    }

    // Function to leave a room
    public void leaveCurrentRoom() {
      if (getCurrentRoom() != null) {
        Room room = getCurrentRoom();
        room.removeClient(this);

        sendSystemMessage("You have left the room.");
        Logger.info(clientName + " has left room '" + room.getRoomName() + "'");
        if (room.getClientCount() == 0) {
          rooms.remove(room.getRoomName());
          Logger.info("Room '" + room.getRoomName() + "' has been deleted because it is empty.");
        }
        setCurrentRoom(null);
      } 
      else {
        sendSystemMessage("You are not in any room.");
        Logger.warning("User '" + clientName + "' attempted to leave a room but was not in any.");
      }
    }

    // Function to list all rooms
    public void listRooms() {
      if (rooms.isEmpty()) {
        sendMessage("No rooms available.");
      } 
      else {
        sendSystemMessage("Available rooms:");
        for (Room room : rooms.values()) {
          sendMessage("- " + room.getRoomName() + " (" + room.getClientCount() + "/2)");
        }
      }
    }

    public Game getCurrentGame(){
      if (currentRoom != null){
        return currentRoom.getGame();
      }
      return null;
    }

    public Room getCurrentRoom() {
      return this.currentRoom;
    }

    public void setCurrentRoom(Room room) {
      this.currentRoom = room;
    }

    public String getClientName() {
      return this.clientName;
    }

    // Lists the client's party
    public void listParty() {
      this.party.listParty(this);
    }

    // Closes the connection and removes the client from the set
    public void closeConnection() {
      try {
        if (clientName != null) {
          Logger.info(clientName + " has disconnected.");
          sendSystemMessage("You have been disconnected.");
        }

        if (getCurrentGame() != null){
          getCurrentGame().handleClientDisconnection(this);
        }

        if (getCurrentRoom() != null) {
          leaveCurrentRoom();
        }

        clients.remove(this);
        if (in != null) in.close();
        if (out != null) out.close();
        if (socket != null && !socket.isClosed()) socket.close();
        Logger.info("Total Connections: " + clients.size());
      } 
      catch (IOException e) {
        Logger.error("Error closing connection for " + clientName + ": " + e.getMessage());
      }
    }

    private boolean validateUser(String username, String password) {
      boolean validated = DatabaseManager.validateUser(username, password);
      if (validated){
        return true;
      }
      else{
        Logger.error("Database error during user validation");
        return false;
      }
    }
  }
}

