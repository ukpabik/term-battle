package com.pkg.app.party.monster;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Type {
  private String name;
  private List<String> weaknesses;



  public static final Map<String, List<String>> WEAKNESSES = Map.of(
    "earth", List.of("air", "water"),
    "air", List.of("earth", "light"),
    "fire", List.of("water", "darkness"),
    "water", List.of("earth", "light"),
    "light", List.of("darkness"),
    "darkness", List.of("light", "fire")
  );

  public Type(String name) {
    if (name == null) {
      throw new IllegalArgumentException("Type name cannot be null");
    }
    this.name = name.toLowerCase();
    this.weaknesses = WEAKNESSES.getOrDefault(this.name, new ArrayList<>());
    if (this.weaknesses.isEmpty()) {
      System.out.println("Unknown type: " + this.name);
    }
  }

  public List<String> getWeaknesses() {
    return this.weaknesses;
  }

  public boolean isWeakTo(String type) {
    return this.weaknesses.contains(type.toLowerCase());
  }

  public String getName() {
    return this.name;
  }

  public static double calculateFloat(Type attacker, Type defender){
    if (Type.WEAKNESSES.get(defender.getName()).contains(attacker.getName())) {
      return 1.2;
    } else if (Type.WEAKNESSES.get(attacker.getName()).contains(defender.getName())) {
      return 0.75;
    }

    return 1;
  }

  @Override
  public String toString() {
    return "Type: " + this.name + ", Weaknesses: " + this.weaknesses;
  }
}

