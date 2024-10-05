package com.pkg.app.party.monster;

import java.util.ArrayList;
import java.util.List;


public class Type {
  private String name;
  private List<String> weaknesses = new ArrayList<>();




  public Type(String name) {
    this.name = name;


    // Add weaknesses to weaknesses list
    switch(name){
      case "fire":
        this.weaknesses.add("water");
        break;
      case "water":
        this.weaknesses.add("grass");
        break;
      case "grass":
        this.weaknesses.add("fire");
        break;
      case "rock":
        this.weaknesses.add("flying");
        break;
      case "flying":
        this.weaknesses.add("rock");
        break;
      case "electric":
        this.weaknesses.add("ground");
        break;
      case "ground":
        this.weaknesses.add("electric");
        break;
      case "normal":
        this.weaknesses.add("ghost");
        break;
      case "poison":
        this.weaknesses.add("ground");
        break;
    }
  }


  public List<String> getWeaknesses() {
    return this.weaknesses;
  }

  public boolean isWeakTo(String type) {
    return this.weaknesses.contains(type);
  }


  public String getName(){
    return this.name;
  }

  @Override
  public String toString(){
    return this.name;
  }
}
