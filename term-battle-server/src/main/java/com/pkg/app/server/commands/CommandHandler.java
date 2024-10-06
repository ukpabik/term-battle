package com.pkg.app.server.commands;

import com.pkg.app.server.Server.ClientHandler;
import java.util.HashMap;
import java.util.Map;

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

    // TODO: Add more commands
  }


  // Method to execute commands
  public void executeCommand(String command, ClientHandler client){
    if (!command.startsWith("/")) {
      if (client.getCurrentRoom() != null) {
        client.getCurrentRoom().roomBroadcast(client.getClientName() + ": " + command, client);
      }
      else{
        client.globalBroadcast(client.getClientName() + ": " + command, client);
      }
      return;
    }


    String[] parts = command.trim().split("\\s+");
    String commandName = parts[0].substring(1).toLowerCase();

    Command commandObj = commandMap.get(commandName);
    if (commandObj != null) {
      commandObj.execute(client, parts);
    }
    else{
      client.sendSystemMessage("Unknown command: " + commandName);
    }
  }




  // /join <room>
  private class JoinCommand implements Command {
    @Override
    public void execute(ClientHandler client, String[] args) {
      if (args.length < 2) {
        client.sendSystemMessage("Usage: /join <room name>");
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
      client.listHelp();
    }
  }

  // /ready
  private class ReadyCommand implements Command {
    @Override
    public void execute(ClientHandler client, String[] args) {
      if (!checkClient(client)) {
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
        return;
      }
      client.getCurrentRoom().start(client);
    }
  }

  // /enemy
  private class EnemyCommand implements Command {
    @Override
    public void execute(ClientHandler client, String[] args){
      if (!checkClient(client)) {
        return;
      }
      client.getCurrentRoom().listOtherParties(client);
    }
  }

  // /list
  private class ListCommand implements Command {
    @Override
    public void execute(ClientHandler client, String[] args){
      if (!checkClient(client)) {
        return;
      }
      client.getCurrentRoom().listAllUsers(client);
    }
  }

  public boolean checkClient(ClientHandler client) {
    if (client.getCurrentRoom() == null) {
      client.sendSystemMessage("You are not in a room.");
      return false;
    }
    return true;
  }

}
