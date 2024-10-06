package com.pkg.app.party.monster;



public class Move {

  private String name;
  private int damage;
  private Type type;



  public Move(String name, int damage, Type type) {
    this.name = name;
    this.damage = damage;
    this.type = type;
  }


  public String getName() {
    return name;
  }


  public int getDamage() {
    return damage;
  }

  public Type getType() {
    return type;
  }

}
