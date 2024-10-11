package com.pkg.app.game;

import com.pkg.app.server.Server.ClientHandler;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ConcurrentHashMap;
import com.pkg.app.party.monster.Monster;
import com.pkg.app.party.monster.Type;
import com.pkg.app.party.monster.Move;

public class Game {
  private boolean isEnded;
  private boolean isOngoing;
  private List<ClientHandler> clients = new CopyOnWriteArrayList<>();
  private Map<ClientHandler, Move> clientMoves = new ConcurrentHashMap<>();

  public Game(List<ClientHandler> clients){
    this.clients.addAll(clients);
    this.isOngoing = true;
    this.isEnded = false;
    this.startGame();
  }



  public boolean getIsEnded(){
    return this.isEnded;
  }

  public boolean getIsOngoing(){
    return this.isOngoing;
  }


  public void setIsOngoing(boolean status){
    this.isOngoing = status;
  }

  public void setIsEnded(boolean status){
    this.isEnded = status;
  }


  public void endGame(){
    //TODO: COMPLETE LOGIC HERE 
    System.out.println("GAME ENDED");
  }


  public void startGame(){
    //TODO: COMPLETE LOGIC HERE 
    System.out.println("GAME START");
  }

  public synchronized void receiveMessage(ClientHandler sender, String message){
    //TODO: HANDLE MESSAGE
    System.out.println("Received message from " + sender.getClientName() + ": " + message);

  }

  public void sendMessageToAll(String message){
    for (ClientHandler client: clients){
      client.sendMessage(message);
    }
  }


  public synchronized void handleMove(String moveName, ClientHandler client){
    if (!isOngoing){
      client.sendSystemMessage("There is no ongoing game.");
      return;
    }

    if (clientMoves.containsKey(client)){
      client.sendSystemMessage("You have already submitted your move...");
      return;
    }

    Monster currentMonster = client.getParty().getCurrentMonster();
    if (currentMonster == null) {
      client.sendSystemMessage("You have no available monsters.");
      return;
    }

    // Validate the move
    Move selectedMove = null;
    for (Move move : currentMonster.getMoves()) {
      if (move.getName().equalsIgnoreCase(moveName)) {
        selectedMove = move;
        break;
      }
    }

    if (selectedMove == null) {
      client.sendSystemMessage("Invalid move. Your monster doesn't know '" + moveName + "'.");
      return;
    }

    clientMoves.put(client, selectedMove);
    client.sendSystemMessage("Your move '" + selectedMove.getName() + "' has been received.");

    if (clientMoves.size() == clients.size()){
      // Process the moves
      processMoves();
      clientMoves.clear();
    } 
    else {
      client.sendSystemMessage("Wait for your opponent to submit their move...");
    }
  }

  private synchronized void processMoves(){
    ClientHandler client1 = clients.get(0);
    ClientHandler client2 = clients.get(1);
    Move move1 = clientMoves.get(client1);
    Move move2 = clientMoves.get(client2);

    int speed1 = client1.getParty().getCurrentMonster().getSpeed();
    int speed2 = client2.getParty().getCurrentMonster().getSpeed();

    ClientHandler first, second;
    Move firstMove, secondMove;

    if (speed1 > speed2){
      first = client1;
      firstMove = move1;
      second = client2;
      secondMove = move2;
    } else if (speed1 < speed2){
      first = client2;
      firstMove = move2;
      second = client1;
      secondMove = move1;
    } else {
      // Randomly decide
      if (Math.random() < 0.5) {
        first = client1;
        firstMove = move1;
        second = client2;
        secondMove = move2;
      } else {
        first = client2;
        firstMove = move2;
        second = client1;
        secondMove = move1;
      }
    }

    // Execute the moves
    executeMove(first, firstMove, second);

    if (second.getParty().getCurrentMonster().getHealth() > 0) {
      executeMove(second, secondMove, first);
    } else {
      second.sendSystemMessage("Your monster has fainted and cannot attack.");
    }

    if (checkEndGame()){
      endGame();
    }
  }

  private boolean checkEndGame(){
    for (ClientHandler client : clients){
      if (!client.getParty().allMonstersFainted()){
        //TODO: Add one win to the winner, and add a loss to the loser
      }
      else {
        sendMessageToAll("Player " + client.getClientName() + " has no remaining monsters. Game over.");
        return true;
      }
    }

    return false;
  }

  private void executeMove(ClientHandler attacker, Move move, ClientHandler defender){
    Monster attackMon = attacker.getParty().getCurrentMonster();
    Monster defendMon = defender.getParty().getCurrentMonster();

    int damage = calculateDamage(attackMon, defendMon, move);
    defendMon.setHealth(defendMon.getHealth() - damage);

    attacker.sendSystemMessage("Your " + attackMon.getName() + " used " + move.getName() + " and dealt " + damage + " damage!");
    defender.sendSystemMessage("Opponent's " + attackMon.getName() + " used " + move.getName() + " and dealt " + damage + " damage to your " + defendMon.getName() + "!");

    if (defendMon.getHealth() <= 0){
      defender.sendSystemMessage("Your " + defendMon.getName() + " has fainted...");

      // Handle monster fainting
      Monster nextMonster = defender.getParty().findNextCurrent(defendMon);
      defender.getParty().setCurrentMonster(nextMonster);

      if (nextMonster != null) {
        defender.sendSystemMessage("Your next monster is " + nextMonster.getName() + "!");
      }
    }
  }

  private int calculateDamage(Monster attacker, Monster defender, Move move){
    double baseDamage = move.getDamage() + attacker.getAttack();
    double typeFactor = Type.calculateFloat(move.getType(), defender.getType());

    double damage = baseDamage * typeFactor;

    if (damage < 0) damage = 0;
    return (int)damage;
  }

  public synchronized void handleClientDisconnection(ClientHandler client) {
    sendMessageToAll("Player " + client.getClientName() + " has disconnected.");
    //TODO: Set the other client's game to null and trash this game
    //TODO: Also, add one win to the player who didn't leave
    endGame();
  }

}
