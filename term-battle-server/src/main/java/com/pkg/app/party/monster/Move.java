package com.pkg.app.party.monster;

import java.util.*;

public class Move {

  private String name;
  private int damage;
  private Type type;

  // Static map to hold moves for each type
  private static final Map<String, List<Move>> typeMoves = new HashMap<>();

  static {
    initializeMoves();
  }

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

  // Initialize moves for each type
  private static void initializeMoves() {
    // Fire type moves
    List<Move> fireMoves = new ArrayList<>();
    fireMoves.add(new Move("Flame Burst", 40, new Type("fire")));
    fireMoves.add(new Move("Heat Wave", 50, new Type("fire")));
    fireMoves.add(new Move("Ember", 30, new Type("fire")));
    fireMoves.add(new Move("Fire Spin", 35, new Type("fire")));
    typeMoves.put("fire", fireMoves);

    // Water type moves
    List<Move> waterMoves = new ArrayList<>();
    waterMoves.add(new Move("Water Gun", 30, new Type("water")));
    waterMoves.add(new Move("Bubble Beam", 35, new Type("water")));
    waterMoves.add(new Move("Aqua Tail", 45, new Type("water")));
    waterMoves.add(new Move("Hydro Pump", 50, new Type("water")));
    typeMoves.put("water", waterMoves);

    // Earth type moves
    List<Move> earthMoves = new ArrayList<>();
    earthMoves.add(new Move("Mud Slap", 30, new Type("earth")));
    earthMoves.add(new Move("Earthquake", 50, new Type("earth")));
    earthMoves.add(new Move("Sandstorm", 35, new Type("earth")));
    earthMoves.add(new Move("Rock Slide", 40, new Type("earth")));
    typeMoves.put("earth", earthMoves);

    // Air type moves
    List<Move> airMoves = new ArrayList<>();
    airMoves.add(new Move("Gust", 30, new Type("air")));
    airMoves.add(new Move("Air Cutter", 35, new Type("air")));
    airMoves.add(new Move("Aerial Ace", 40, new Type("air")));
    airMoves.add(new Move("Hurricane", 50, new Type("air")));
    typeMoves.put("air", airMoves);

    // Light type moves
    List<Move> lightMoves = new ArrayList<>();
    lightMoves.add(new Move("Flash", 30, new Type("light")));
    lightMoves.add(new Move("Solar Beam", 50, new Type("light")));
    lightMoves.add(new Move("Photon Blast", 45, new Type("light")));
    lightMoves.add(new Move("Illuminate", 35, new Type("light")));
    typeMoves.put("light", lightMoves);

    // Darkness type moves
    List<Move> darknessMoves = new ArrayList<>();
    darknessMoves.add(new Move("Shadow Sneak", 30, new Type("darkness")));
    darknessMoves.add(new Move("Dark Pulse", 40, new Type("darkness")));
    darknessMoves.add(new Move("Night Slash", 35, new Type("darkness")));
    darknessMoves.add(new Move("Shadow Ball", 45, new Type("darkness")));
    typeMoves.put("darkness", darknessMoves);

    //TODO: GET MORE MOVES
  }

  // Method to get random moves for a given type
  public static List<Move> getRandomMoves(String typeName, int numberOfMoves) {
    List<Move> moves = typeMoves.get(typeName.toLowerCase());
    if (moves == null || moves.size() < numberOfMoves) {
      throw new IllegalArgumentException("Not enough moves for type " + typeName);
    }

    Collections.shuffle(moves);
    return new ArrayList<>(moves.subList(0, numberOfMoves));
  }
}
