package com.pkg.app.rooms;

import java.util.List;
import java.util.ArrayList;
import com.pkg.app.server.Server.ClientHandler;
import java.util.Collections;
import com.pkg.app.party.Party;
import com.pkg.app.party.monster.Monster;


// This class represents a room in the game. It has a list of clients in the room
public class Room {
  // Room capacity
  private static final int MAX_CAPACITY = 2;
  private String roomName;
  private List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());
  private ClientHandler host;

  public Room(String roomName, ClientHandler host) {
    this.roomName = roomName;
    this.host = host;
  }


  // Synchronized method to get random party
  public synchronized void getRandomParty(ClientHandler client){
    List<Monster> monsters = Monster.getRandomMonsters();
    Party party = new Party(monsters);
    client.setParty(party);
  }

  // Synchronized method to add clients to rooms
  public synchronized boolean addClient(ClientHandler client){
    if (clients.size() < MAX_CAPACITY){
      clients.add(client);
      client.setCurrentRoom(this);
      roomBroadcast(client.getClientName() + " has joined the room.", client);
      return true;
    }
    else {
      client.sendMessage("Room is full.");
      return false;
    }
  }

  // Synchronized method to remove clients from rooms
  public synchronized void removeClient(ClientHandler client) {
    clients.remove(client);
    if (client == this.host && clients.size() > 0) {
      switchHost(clients.get(0));
    }
    client.setCurrentRoom(null);
    roomBroadcast(client.getClientName() + " has left the room.", client);
  }

  // Broadcast messages to all clients in the room except the sender
  public void roomBroadcast(String message, ClientHandler excludeClient){
    synchronized(clients){
      for (ClientHandler c: clients){
        if (c != excludeClient){
          c.sendRoomMessage(message);
        }
      }
    }
  }

  // Broadcast messages to all clients in the room
  public void globalRoomBroadcast(String message){
    synchronized(clients){
      for (ClientHandler c: clients){
        c.sendRoomMessage(message);
      }
    }
  }


  // Lists all users in the room
  public synchronized void listAllUsers(ClientHandler client) {
    if (clients.isEmpty()) {
      client.sendMessage("There are no users currently in the room.");
      return;
    }

    StringBuilder userList = new StringBuilder("Users in the room:\n");
    synchronized(clients) {
      for (ClientHandler c : clients) {
        if (c == client) {
          userList.append("*").append(c.getClientName()).append("*\n");
        }
        else{
          userList.append(c.getClientName()).append("\n");
        }
      }
    }
    client.sendMessage(userList.toString());
  }

  // Lists the enemy party for the user to see and strategize
  public synchronized void listOtherParties(ClientHandler requestingClient) {
    if (clients.size() <= 1) {
      requestingClient.sendMessage("There are no other clients in the room.");
      return;
    }

    StringBuilder partyList = new StringBuilder();
    synchronized(clients) {
      for (ClientHandler c : clients) {
        if (c != requestingClient) {
          Party party = c.getParty();
          if (party != null) {
            partyList.append(c.getClientName()).append("'s Party:\n");
            for (Monster monster : party.getMonsters()) {
              partyList.append("- ").append(monster.getName())
                .append(" (").append(monster.getType())
                .append(", Health: ").append(monster.getHealth())
                .append(", Attack: ").append(monster.getAttack())
                .append(", Speed: ").append(monster.getSpeed()).append(")\n");
            }
          } 
          else {
            partyList.append(c.getClientName()).append(" has no party assigned.\n");
          }
        }
      }
    }
    requestingClient.sendMessage(partyList.toString());
  }


  // Function that starts the room 
  public void startBattle(){
    if (clients.size() < 2) {
      host.sendMessage("There are not enough players in the room to start the battle.");
    }
    else{
      globalRoomBroadcast("Battle started!");

      // TODO: Start the battle -> Do the logic
    }

  }

  public ClientHandler getHost(){
    return this.host;
  }

  public void setHost(ClientHandler host){
    this.host = host;
  }

  // If a user leaves the room, switch the host
  public void switchHost(ClientHandler newHost){
    synchronized(clients){
      this.host = newHost;
    }
  }

  // Starts the game if all clients are ready and the host calls /start
  public void start(ClientHandler caller) {
    if (this.host != caller) {
      caller.sendMessage("You are not the host of the room.");
      return;
    }

    if (!checkReady()) {
      caller.sendMessage("Please wait for all players to be ready.");
      return;
    }

    if (!isFull()) {
      caller.sendMessage("You can't play with yourself. Get some friends and try again.");
      return;
    }

    startBattle();
  }


  // Checks if all clients are ready
  public boolean checkReady(){
    synchronized(clients){
      for (ClientHandler c: clients){
        if (!c.getReady()){
          return false;
        }
      }
      return true;
    }
  }


  // Gets the client count in the current room
  public int getClientCount(){
    return this.clients.size();
  }

  // Returns true if room is not full
  public boolean isFull(){
    return clients.size() >= MAX_CAPACITY;
  }

  // Returns the list of clients
  public List<ClientHandler> getClients() {
    return clients;
  }

  public String getRoomName(){
    return this.roomName;
  }
}
