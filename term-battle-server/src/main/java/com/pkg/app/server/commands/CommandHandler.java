package com.pkg.app.server.commands;

import com.pkg.app.server.Server.ClientHandler;
import com.pkg.app.server.Logger;
import com.pkg.app.party.monster.Type;
import com.pkg.app.party.monster.Monster;
import com.pkg.app.party.monster.Move;
import com.pkg.app.server.text.AnsiText;
import com.pkg.app.server.text.TypeColor;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class CommandHandler {

  // Command interface to model all commands
  public interface Command {
    void execute(ClientHandler client, String[] args);
  }

  private Map<String, Command> lobbyCommands = new HashMap<>();
  private Map<String, Command> roomCommands = new HashMap<>();
  private Map<String, Command> gameCommands = new HashMap<>();

  public CommandHandler() {
    registerGameCommands();
    registerLobbyCommands();
    registerRoomCommands();
  }

  // Method to register all commands
  private void registerGameCommands() {
    gameCommands.put("party", new PartyCommand());
    gameCommands.put("help", new HelpCommand());
    gameCommands.put("enemy", new EnemyCommand());
    gameCommands.put("list", new ListCommand());
    gameCommands.put("exit", new ExitCommand());
    gameCommands.put("type", new TypeInfoCommand());
    gameCommands.put("moves", new MovesCommand());
    gameCommands.put("attack", new AttackCommand());
  }

  private void registerLobbyCommands() {
    lobbyCommands.put("create", new CreateCommand());
    lobbyCommands.put("join", new JoinCommand());
    lobbyCommands.put("rooms", new ListRoomsCommand());
    lobbyCommands.put("help", new HelpCommand());
    lobbyCommands.put("exit", new ExitCommand());
  }

  // Method to register all commands
  private void registerRoomCommands() {
    roomCommands.put("join", new JoinCommand());
    roomCommands.put("create", new CreateCommand());
    roomCommands.put("rooms", new ListRoomsCommand());
    roomCommands.put("party", new PartyCommand());
    roomCommands.put("help", new HelpCommand());
    roomCommands.put("enemy", new EnemyCommand());
    roomCommands.put("list", new ListCommand());
    roomCommands.put("exit", new ExitCommand());
    roomCommands.put("type", new TypeInfoCommand());
    roomCommands.put("leave", new LeaveCommand());
    roomCommands.put("start", new StartCommand());
    roomCommands.put("ready", new ReadyCommand());
  }

  // Method to execute commands
  public void executeCommand(String command, ClientHandler client) {
    if (!command.startsWith("/")) {
      if (client.getCurrentRoom() != null) {
        client.getCurrentRoom().roomBroadcast(client.getClientName() + ": " + command, client);
        Logger.info("Message from " + client.getClientName() + " in room '" + client.getCurrentRoom().getRoomName() + "': " + command);
      } 
      else {
        client.globalBroadcast(client.getClientName() + ": " + command, client);
        Logger.info("Global message from " + client.getClientName() + ": " + command);
      }
      return;
    }

    String[] parts = command.trim().split("\\s+");
    String commandName = parts[0].substring(1).toLowerCase();
    Map<String, Command> availableCommands;

    if (client.getCurrentGame() != null) {
      availableCommands = gameCommands;
    } 
    else if (client.getCurrentRoom() != null) {
      availableCommands = roomCommands;
    }
    else {
      availableCommands = lobbyCommands;
    }

    Command commandObj = availableCommands.get(commandName);
    if (commandObj != null) {
      commandObj.execute(client, parts);
    } 
    else {
      client.sendSystemMessage("Unknown command: " + commandName);
      Logger.warning("Unknown command '" + commandName + "' from user '" + client.getClientName() + "'");
    }
  }

  // /join <room>
  private class JoinCommand implements Command {
    @Override
    public void execute(ClientHandler client, String[] args) {
      if (args.length < 2) {
        client.sendSystemMessage("Usage: /join <room name>");
        Logger.warning("User '" + client.getClientName() + "' used /join without specifying a room name.");
        return;
      }
      String roomName = args[1];
      client.joinRoom(roomName);
    }
  }

  // /create <room>
  private class CreateCommand implements Command {
    @Override
    public void execute(ClientHandler client, String[] args) {
      if (args.length < 2) {
        client.sendSystemMessage("Usage: /create <room name>");
        Logger.warning("User '" + client.getClientName() + "' used /create without specifying a room name.");
        return;
      }
      String roomName = args[1];
      client.createRoom(roomName);
    }
  }

  // /leave
  private class LeaveCommand implements Command {
    @Override
    public void execute(ClientHandler client, String[] args) {
      if (!checkClient(client)) {
        Logger.warning("User '" + client.getClientName() + "' attempted to leave a room but is not in one.");
        return;
      }
      client.leaveCurrentRoom();
    }
  }

  // /rooms
  private class ListRoomsCommand implements Command {
    @Override
    public void execute(ClientHandler client, String[] args) {
      client.listRooms();
    }
  }

  // /type <type>
  private class TypeInfoCommand implements Command {
    @Override
    public void execute(ClientHandler client, String[] args) {
      if (args.length < 2) {
        client.sendSystemMessage("Usage: /type <type name>");
        Logger.warning("User '" + client.getClientName() + "' used /type without specifying a type name.");
        return;
      }

      String typeInput = args[1].toLowerCase(); // Ensure case-insensitivity
      List<String> weaknesses = Type.WEAKNESSES.get(typeInput);

      if (weaknesses == null) {
        client.sendSystemMessage("Unknown type: " + AnsiText.color(typeInput, AnsiText.RED));
        return;
      }

      String coloredType = TypeColor.colorType(typeInput);

      String coloredWeaknesses;
      if (weaknesses.isEmpty()) {
        coloredWeaknesses = AnsiText.color("None", AnsiText.WHITE);
      } 
      else {
        StringBuilder weaknessesBuilder = new StringBuilder();
        for (int i = 0; i < weaknesses.size(); i++) {
          String weakness = weaknesses.get(i);
          String coloredWeakness = TypeColor.colorType(weakness);
          weaknessesBuilder.append(coloredWeakness);

          if (i < weaknesses.size() - 1) {
            weaknessesBuilder.append(", ");
          }
        }
        coloredWeaknesses = weaknessesBuilder.toString();
      }
      String coloredTypeLabel = AnsiText.color("Type: ", AnsiText.WHITE);
      String message = coloredTypeLabel + coloredType + ", Weaknesses: " + coloredWeaknesses;
      client.sendSystemMessage(message);
    }
  }

  // /party
  private class PartyCommand implements Command {
    @Override
    public void execute(ClientHandler client, String[] args) {
      client.listParty();
    }
  }

  // /help
  private class HelpCommand implements Command {
    @Override
    public void execute(ClientHandler client, String[] args) {
      StringBuilder sb = new StringBuilder();
      Map<String, Command> availableCommands;

      if (client.getCurrentGame() != null) {
        availableCommands = gameCommands;
      } 
      else if (client.getCurrentRoom() != null) {
        availableCommands = roomCommands;
      } 
      else {
        availableCommands = lobbyCommands;
      }

      sb.append("Available Commands:\n");
      for (String cmd : availableCommands.keySet()) {
        sb.append("/").append(cmd).append("\n");
      }

      client.sendSystemMessage(sb.toString());
    }
  }

  // /exit
  private class ExitCommand implements Command {
    @Override
    public void execute(ClientHandler client, String[] args) {
      client.sendSystemMessage("Bye!");
      client.closeConnection();
    }
  }

  // /ready
  private class ReadyCommand implements Command {
    @Override
    public void execute(ClientHandler client, String[] args) {
      if (!checkClient(client)) {
        Logger.warning("User '" + client.getClientName() + "' attempted to toggle ready but is not in a room.");
        return;
      }
      client.toggleReady();
    }
  }

  // /start
  private class StartCommand implements Command {
    @Override
    public void execute(ClientHandler client, String[] args) {
      if (!checkClient(client)) {
        Logger.warning("User '" + client.getClientName() + "' attempted to start a game but is not in a room.");
        return;
      }
      client.getCurrentRoom().start(client);
    }
  }

  // /enemy
  private class EnemyCommand implements Command {
    @Override
    public void execute(ClientHandler client, String[] args) {
      if (!checkClient(client)) {
        Logger.warning("User '" + client.getClientName() + "' requested enemy information but is not in a room.");
        return;
      }
      client.getCurrentRoom().listOtherParties(client);
    }
  }

  // /list
  private class ListCommand implements Command {
    @Override
    public void execute(ClientHandler client, String[] args) {
      if (!checkClient(client)) {
        Logger.warning("User '" + client.getClientName() + "' requested a list of users but is not in a room.");
        return;
      }
      client.getCurrentRoom().listAllUsers(client);
    }
  }

  // /attack
  private class AttackCommand implements Command {
    @Override
    public void execute(ClientHandler client, String[] args) {
      if (!checkClientInGame(client)) {
        Logger.warning("User '" + client.getClientName() + "' is not in game.");
        return;
      }

      if (args.length < 2) {
        client.sendSystemMessage("Usage: /attack <move name>");
        Logger.warning("User '" + client.getClientName() + "' used /attack without specifying a move name.");
        return;
      }
      else{
        String moveName;
        if (args.length > 2){
          moveName = "";
          for (int i = 1; i < args.length; i++){
            moveName += args[i] + " ";
          }

        }
        else{
          moveName = args[1];
        }
        client.getCurrentRoom().getGame().handleMove(moveName.trim(), client);

      }


    }
  }

  // /moves
  private class MovesCommand implements Command {
    @Override
    public void execute(ClientHandler client, String[] args) {
      if (!checkClientInGame(client)) {
        Logger.warning("User '" + client.getClientName() + "' is not in game.");
        return;
      }

      Monster currentMonster = client.getParty().getCurrentMonster();
      if (currentMonster != null) {
        StringBuilder sb = new StringBuilder("Your " + currentMonster.getName() + "'s Moves:\n");
        for (Move move : currentMonster.getMoves()) {
          sb.append("- ").append(move.getName())
            .append(" (").append(move.getType().toString())
            .append(", Damage: ").append(move.getDamage()).append(")\n");
        }
        client.sendMessage(sb.toString());
      } else {
        client.sendSystemMessage("You have no active monster.");
      }
    }
  }

  public boolean checkClient(ClientHandler client) {
    if (client.getCurrentRoom() == null) {
      client.sendSystemMessage("You are not in a room.");
      return false;
    }
    return true;
  }

  public boolean checkClientInGame(ClientHandler client) {
    if (client.getCurrentRoom() == null) {
      client.sendSystemMessage("You are not in a room.");
      return false;
    }
    if (client.getCurrentGame() == null) {
      client.sendSystemMessage("You are not in game.");
      return false;
    }
    return true;
  }
}
