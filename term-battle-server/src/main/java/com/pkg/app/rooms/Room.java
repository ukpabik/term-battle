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

  public Room(String roomName){
    this.roomName = roomName;
  }

  public String getRoomName(){
    return this.roomName;
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
    client.setCurrentRoom(null);
    roomBroadcast(client.getClientName() + " has left the room.", client);
  }

  // Broadcast messages to all clients in the room
  public void roomBroadcast(String message, ClientHandler excludeClient){
    synchronized(clients){
      for (ClientHandler c: clients){
        if (c != excludeClient){
          c.sendRoomMessage(message);
        }
      }
    }
  }

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
                .append(" (Type: ").append(monster.getType())
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

}
