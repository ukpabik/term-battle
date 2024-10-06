package com.pkg.app.party;

import com.pkg.app.party.monster.Monster;
import java.util.ArrayList;
import java.util.List;
import com.pkg.app.server.Server.ClientHandler;



public class Party {
  private List<Monster> monsters = new ArrayList<>();
  private Monster currentMonster;


  public Party(List<Monster> monsters) {
    this.monsters = monsters;
    this.currentMonster = this.monsters.get(0);
  }


  public List<Monster> getMonsters() {
    return monsters;
  }

  public Monster getCurrentMonster() {
    return currentMonster;
  }

  public void setCurrentMonster(Monster currentMonster) {
    this.currentMonster = currentMonster;
  }

  public void listParty(ClientHandler clientHandler) {
    clientHandler.sendMessage("Your party:");
    for (Monster monster : monsters) {
      clientHandler.sendMessage("- " + monster.getName() + 
          " (Health: " + monster.getHealth() + 
          ", Attack: " + monster.getAttack() + 
          ", Speed: " + monster.getSpeed() + 
          ", Type: " + monster.getType().getName() + ")");
    }
  }

}
