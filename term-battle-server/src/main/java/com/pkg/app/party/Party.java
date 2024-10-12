package com.pkg.app.party;

import com.pkg.app.party.monster.Monster;
import java.util.ArrayList;
import java.util.List;
import com.pkg.app.server.Server.ClientHandler;
import com.pkg.app.server.text.AnsiText;



public class Party {
  private List<Monster> monsters = new ArrayList<>();
  private Monster currentMonster;


  public Party(List<Monster> monsters) {
    this.monsters = monsters;
    setCurrentMonster(this.monsters.get(0));
  }


  public List<Monster> getMonsters() {
    return monsters;
  }
  public void setMonsters(List<Monster> monsters){
    this.monsters = monsters;
    this.currentMonster = monsters.get(0);
  }

  public boolean allMonstersFainted() {
    for (Monster monster : monsters) {
      if (!monster.getIsFainted()) {
        return false;
      }
    }
    return true;
  }

  public Monster getCurrentMonster() {
    for (Monster monster : monsters) {
      if (!monster.getIsFainted()) {
        return monster;
      }
    }
    return null;
  }

  public Monster findNextCurrent(Monster faintedMonster){
    for (Monster mon : monsters){
      if (mon != faintedMonster && !mon.getIsFainted()){
        return mon;
      }
    }
    return null;
  }

  public void setCurrentMonster(Monster currentMonster) {
    this.currentMonster = currentMonster;
    if (this.currentMonster != null) {
      this.currentMonster.assignRandomMoves();
    }
  }

  public void listParty(ClientHandler clientHandler) {
    clientHandler.sendMessage("Your party:");
    for (Monster monster : monsters) {
      String name = monster.getIsFainted() ? AnsiText.color(monster.getName(), AnsiText.RED_BACKGROUND) : monster.getName();
      clientHandler.sendMessage("- " + name + 
          " (Health: " + monster.getHealth() + 
          ", Attack: " + monster.getAttack() + 
          ", Speed: " + monster.getSpeed() + 
          ", " + monster.getType().toString() + ")");
    }
  }
}
