package com.pkg.app.server.commands;

import com.pkg.app.server.Server.ClientHandler;
import com.pkg.app.server.Logger;
import com.pkg.app.party.monster.Type;
import java.util.HashMap;
import java.util.Map;
import com.pkg.app.party.monster.Monster;
import com.pkg.app.party.monster.Move;

public class CommandHandler {

  // Command interface to model all commands
  public interface Command {
    void execute(ClientHandler client, String[] args);
  }

  private Map<String, Command> commandMap = new HashMap<>();

  public CommandHandler() {
    registerCommands();
  }

  public Map<String, Command> getCommands() {
    return commandMap;
  }

  // Method to register all commands
  private void registerCommands() {
    commandMap.put("join", new JoinCommand());
    commandMap.put("create", new CreateCommand());
    commandMap.put("leave", new LeaveCommand());
    commandMap.put("rooms", new ListRoomsCommand());
    commandMap.put("party", new PartyCommand());
    commandMap.put("help", new HelpCommand());
    commandMap.put("ready", new ReadyCommand());
    commandMap.put("start", new StartCommand());
    commandMap.put("enemy", new EnemyCommand());
    commandMap.put("list", new ListCommand());
    commandMap.put("exit", new ExitCommand());
    commandMap.put("type", new TypeInfoCommand());
    commandMap.put("moves", new MovesCommand());
    commandMap.put("attack", new AttackCommand());
    // TODO: Add more commands
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

    Command commandObj = commandMap.get(commandName);
    if (commandObj != null) {
      Logger.debug("Executing command '" + commandName + "' for user '" + client.getClientName() + "'");
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
      Logger.info("User '" + client.getClientName() + "' is attempting to join room '" + roomName + "'");
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
      Logger.info("User '" + client.getClientName() + "' is attempting to create room '" + roomName + "'");
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
      Logger.info("User '" + client.getClientName() + "' has left their current room.");
    }
  }

  // /rooms
  private class ListRoomsCommand implements Command {
    @Override
    public void execute(ClientHandler client, String[] args) {
      client.listRooms();
      Logger.debug("User '" + client.getClientName() + "' requested a list of rooms.");
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

      String type = args[1];
      client.sendSystemMessage("Type: " + type + ", Weaknesses: " + Type.WEAKNESSES.get(type));
      Logger.debug("User '" + client.getClientName() + "' requested info on type '" + type + "'.");
    }
  }

  // /party
  private class PartyCommand implements Command {
    @Override
    public void execute(ClientHandler client, String[] args) {
      client.listParty();
      Logger.debug("User '" + client.getClientName() + "' requested their party information.");
    }
  }

  // /help
  private class HelpCommand implements Command {
    @Override
    public void execute(ClientHandler client, String[] args) {
      client.listHelp();
      Logger.debug("User '" + client.getClientName() + "' requested help.");
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
      Logger.info("User '" + client.getClientName() + "' toggled ready status to " + client.getReady());
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
      Logger.info("User '" + client.getClientName() + "' initiated a game start in room '" + client.getCurrentRoom().getRoomName() + "'");
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
      Logger.debug("User '" + client.getClientName() + "' requested enemy party information.");
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
      Logger.debug("User '" + client.getClientName() + "' requested a list of all users in the room.");
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
      
      //TODO: Handle moves here!

      String moveName = args[1];

      client.getCurrentRoom().getGame().handleMove(moveName, client);

      Logger.debug("User '" + client.getClientName() + "' used a move.");
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
            .append(" (Type: ").append(move.getType().getName())
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

