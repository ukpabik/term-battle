package com.poke.app.rooms;

import java.util.List;
import java.util.ArrayList;
import com.poke.app.server.Server.ClientHandler;

// Class that handles room functionality
public class Room {
  // Room capacity
  private static final int MAX_CAPACITY = 2;
  private String roomName;
  private List<ClientHandler> clients = new ArrayList<>();
  

  public Room(String roomName){
    this.roomName = roomName;
  }

  public String getRoomName(){
    return this.roomName;
  }


  // Synchronized method to add clients to rooms
  public synchronized boolean addClient(ClientHandler client){

    if (clients.size() < MAX_CAPACITY){
      clients.add(client);
      broadcast(client.getClientName() + " has joined the room.");


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
    broadcast(client.getClientName() + " has left the room.");
  }

  // Broadcast messages to all clients in the room
  public void broadcast(String message){
    for (ClientHandler c: clients){
      c.sendMessage(message);
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

}
