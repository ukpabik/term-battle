package com.pkg.app.game;

import com.pkg.app.server.Server.ClientHandler;
import com.pkg.app.server.text.AnsiText;
import com.pkg.app.party.monster.Monster;
import com.pkg.app.party.monster.Type;
import com.pkg.app.party.monster.Move;
import com.pkg.app.rooms.Room;
import com.pkg.app.server.DatabaseManager;

import java.util.Random;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class Game {
    private boolean isOngoing;
    private List<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private Map<ClientHandler, Move> clientMoves = new ConcurrentHashMap<>();
    private Room room;
    private String winnerName;
    private String loserName;
    private final double DAMAGE_MULTIPLIER = 2.6;
    private Random random = new Random();

    public Game(List<ClientHandler> clients, Room room) {
        this.clients.addAll(clients);
        this.isOngoing = true;
        this.room = room;
        this.startGame();
    }

    public boolean getIsOngoing() {
        return this.isOngoing;
    }

    public Room getRoom() {
        return this.room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setIsOngoing(boolean status) {
        this.isOngoing = status;
    }

    public synchronized void endGame() {
      if (this.winnerName != null && this.loserName != null) {
        DatabaseManager.addWinner(this.winnerName);
        DatabaseManager.addLoser(this.loserName);
      }

      for (ClientHandler client : clients) {
        client.getParty().setMonsters(Monster.getRandomMonsters());
        client.toggleReady();
      }

      getRoom().setGame(null);
      clients.clear();
      this.isOngoing = false;

      sendMessageToAll(AnsiText.color("The game has ended.", AnsiText.GREEN));
    }

    public void startGame() {

        for (ClientHandler client : clients) {
            client.sendGameMessage(AnsiText.color("The game has started! Good luck!", AnsiText.GREEN));
            client.sendGameMessage(AnsiText.color("Your current monster is " + client.getParty().getCurrentMonster().getName(), AnsiText.CYAN));
        }
    }

    public synchronized void handleMove(String moveName, ClientHandler client) {
        if (!isOngoing) {
            client.sendSystemMessage(AnsiText.color("There is no ongoing game.", AnsiText.YELLOW));
            return;
        }

        if (clientMoves.containsKey(client)) {
            client.sendGameMessage(AnsiText.color("Wait for your opponent to submit their move...", AnsiText.YELLOW));
            return;
        }

        Monster currentMonster = client.getParty().getCurrentMonster();
        if (currentMonster == null) {
            client.sendGameMessage(AnsiText.color("You have no available monsters.", AnsiText.RED));
            return;
        }

        Move selectedMove = null;
        for (Move move : currentMonster.getMoves()) {
            if (move.getName().equalsIgnoreCase(moveName)) {
                selectedMove = move;
                break;
            }
        }

        if (selectedMove == null) {
            client.sendGameMessage(AnsiText.color("Invalid move. Your monster doesn't know '" + moveName + "'.", AnsiText.RED));
            return;
        }

        clientMoves.put(client, selectedMove);
        client.sendGameMessage(AnsiText.color("Your move '" + selectedMove.getName() + "' has been received.", AnsiText.GREEN));

        if (clientMoves.size() == clients.size()) {
            processMoves();
            clientMoves.clear();
        } 
    }

    private synchronized void processMoves() {
        ClientHandler client1 = clients.get(0);
        ClientHandler client2 = clients.get(1);
        Move move1 = clientMoves.get(client1);
        Move move2 = clientMoves.get(client2);

        int speed1 = client1.getParty().getCurrentMonster().getSpeed();
        int speed2 = client2.getParty().getCurrentMonster().getSpeed();

        ClientHandler first, second;
        Move firstMove, secondMove;

        if (speed1 > speed2) {
            first = client1;
            firstMove = move1;
            second = client2;
            secondMove = move2;
        } 
        else if (speed1 < speed2) {
            first = client2;
            firstMove = move2;
            second = client1;
            secondMove = move1;
        } 
        else {
            if (Math.random() < 0.5) {
                first = client1;
                firstMove = move1;
                second = client2;
                secondMove = move2;
            } 
            else {
                first = client2;
                firstMove = move2;
                second = client1;
                secondMove = move1;
            }
        }

        boolean secondAlive = executeMove(first, firstMove, second);

        if (secondAlive) {
            executeMove(second, secondMove, first);
        } 

        if (checkEndGame()) {
            endGame();
        }
    }

    private boolean checkEndGame() {
      boolean gameEnded = false;
      ClientHandler loser = null;
      ClientHandler winner = null;

      for (ClientHandler client : clients) {
        if (client.getParty().allMonstersFainted()) {
          sendMessageToAll(AnsiText.color(client.getClientName() + " has no remaining monsters. Good game!", AnsiText.YELLOW_BRIGHT));
          loser = client;
          gameEnded = true;
          break;
        }
      }

      if (gameEnded) {
        for (ClientHandler client : clients) {
          if (!client.equals(loser)) {
            winner = client;
            break;
          }
        }

        if (winner != null) {
          this.winnerName = winner.getClientName();
          this.loserName = loser.getClientName();
        } 
        else {
          this.winnerName = null;
          this.loserName = null;
        }
      }

      return gameEnded;
    }


    private boolean executeMove(ClientHandler attacker, Move move, ClientHandler defender) {
        Monster attackMon = attacker.getParty().getCurrentMonster();
        Monster defendMon = defender.getParty().getCurrentMonster();

        int damage = calculateDamage(attackMon, defendMon, move);
        defendMon.setHealth(defendMon.getHealth() - damage);

        // Messages to attacker
        attacker.sendGameMessage(AnsiText.color("Your " + attackMon.getName() + " used " + move.getName() + "!", AnsiText.CYAN));
        attacker.sendGameMessage(AnsiText.color("It dealt " + damage + " damage to opponent's " + defendMon.getName() + ".", AnsiText.GREEN));

        // Messages to defender
        defender.sendGameMessage(AnsiText.color("Opponent's " + attackMon.getName() + " used " + move.getName() + "!", AnsiText.CYAN));
        defender.sendGameMessage(AnsiText.color("It dealt " + damage + " damage to your " + defendMon.getName() + ".", AnsiText.RED));

        if (defendMon.getHealth() <= 0) {
          defendMon.setHealth(0);
          defendMon.setIsFainted(true);

          defender.sendGameMessage(AnsiText.color("Your " + defendMon.getName() + " has fainted...", AnsiText.RED_BOLD));
          attacker.sendGameMessage(AnsiText.color(defender.getClientName() + "'s " + defendMon.getName() + " has fainted!", AnsiText.GREEN));

          Monster nextMonster = defender.getParty().findNextCurrent(defendMon);
          defender.getParty().setCurrentMonster(nextMonster);

          if (nextMonster != null) {
            defender.sendGameMessage(AnsiText.color("Your next monster is " + nextMonster.getName() + "!", AnsiText.GREEN_BOLD));
          }
          return false;
        } 
        else {
          defender.sendGameMessage(AnsiText.color("Your " + defendMon.getName() + " has " + defendMon.getHealth() + " health remaining.", AnsiText.YELLOW));
        }

        return true;
    }

    private int calculateDamage(Monster attacker, Monster defender, Move move) {
        double variability = 0.85 + (random.nextDouble() * 0.15);
        double baseDamage = (move.getDamage() + attacker.getAttack()) / DAMAGE_MULTIPLIER;
        double typeFactor = Type.calculateFloat(move.getType(), defender.getType());

        double damage = baseDamage * typeFactor * variability;

        damage = Math.max(damage, 1);

        return (int) damage;
    }

    public void sendMessageToAll(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    public synchronized void handleClientDisconnection(ClientHandler disconnectedClient) {
      sendMessageToAll(AnsiText.color("Player " + disconnectedClient.getClientName() + " has disconnected.", AnsiText.RED));
      for (ClientHandler client : clients) {
        if (!client.equals(disconnectedClient)) {
          sendMessageToAll(AnsiText.color("Player " + client.getClientName() + " wins by default!", AnsiText.GREEN_BRIGHT));
          this.winnerName = client.getClientName();
          this.loserName = disconnectedClient.getClientName();
          break;
        }
      }

      if (this.winnerName == null){
        this.loserName = null;
      }

      endGame();
    }

}

