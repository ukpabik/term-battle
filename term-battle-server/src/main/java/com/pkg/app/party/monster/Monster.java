package com.pkg.app.party.monster;



public class Monster {

  private String name;
  private int health;
  private int damage;
  private Type type;


  public Monster(String name, int health, int damage, Type type) {
    this.name = name;
    this.health = health;
    this.damage = damage;
    this.type = type;
  }


  public String getName() {
    return name;
  }

  public int getHealth() {
    return health;
  }

  public int getDamage() {
    return damage;
  }

  public Type getType() {
    return type;
  }

  public void setHealth(int health) {
    this.health = health;
  }


  public void setDamage(int damage) {
    this.damage = damage;
  }


}
