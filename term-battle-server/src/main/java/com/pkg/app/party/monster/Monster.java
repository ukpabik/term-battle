package com.pkg.app.party.monster;

import java.util.List;
import com.pkg.app.server.DatabaseManager; 

public class Monster {

  private String name;
  private int health;
  private int attack;
  private int speed;
  private Type type;


  public Monster(String name, int health, int attack, int speed, Type type) {
    this.name = name;
    this.health = health;
    this.attack = attack;
    this.speed = speed;
    this.type = type;
  }


  public String getName() {
    return name;
  }

  public int getHealth() {
    return health;
  }

  public int getAttack() {
    return attack;
  }

  public Type getType() {
    return type;
  }

  public int getSpeed() {
    return speed;
  }

  public void setHealth(int health) {
    this.health = health;
  }


  public void setAttack(int attack) {
    this.attack = attack;
  }


  // Global method to get a list of random monsters
  public static List<Monster> getRandomMonsters() {
    List<Monster> monsters = DatabaseManager.getRandomMonsters();
    return monsters;
  }

}
