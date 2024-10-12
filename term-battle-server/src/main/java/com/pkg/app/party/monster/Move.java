package com.pkg.app.party.monster;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import com.pkg.app.party.monster.movedirectory.MoveDirectory;

public class Move {

  private String name;
  private int damage;
  private int maxUses;
  private int currentUses;
  private Type type;

  // Static map to hold moves for each type
  private static final Map<String, List<Move>> typeMoves = new HashMap<>();

  static {
    initializeMoves();
  }

  public Move(String name, int damage, Type type, int maxUses) {
    this.name = name;
    this.damage = damage;
    this.type = type;
    this.maxUses = maxUses;
    this.currentUses = 0;
  }

  public int getMaxUses(){
    return this.maxUses;
  }

  public int getCurrentUses(){
    return this.currentUses;
  }

  public boolean canUse(){
    return currentUses < maxUses;
  }

  public void use(){
    if (canUse()){
      currentUses++;
    }
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
    typeMoves.put("fire", MoveDirectory.getFireMoves());

    // Water type moves
    typeMoves.put("water", MoveDirectory.getWaterMoves());

    // Earth type moves
    typeMoves.put("earth", MoveDirectory.getEarthMoves());

    // Air type moves
    typeMoves.put("air", MoveDirectory.getAirMoves());

    // Light type moves
    typeMoves.put("light", MoveDirectory.getLightMoves());

    // Darkness type moves
    typeMoves.put("darkness", MoveDirectory.getDarknessMoves());

  }

  // Method to get random moves for a given type
  public static List<Move> getRandomMoves(String typeName, int num) {
    // Number of moves to get from the specified type
    int specifiedTypeMovesCount = num / 2;

    // Number of random types to select
    int randomTypeCount = num / 2;

    // Create a list to hold the final moves
    List<Move> selectedMoves = new ArrayList<>();

    // Get moves from the specified type
    List<Move> specifiedTypeMoves = typeMoves.get(typeName.toLowerCase());
    if (specifiedTypeMoves == null || specifiedTypeMoves.size() < specifiedTypeMovesCount) {
      throw new IllegalArgumentException("Not enough moves for type " + typeName);
    }

    // Shuffle and select 2 moves from the specified type
    Collections.shuffle(specifiedTypeMoves);
    selectedMoves.addAll(specifiedTypeMoves.subList(0, specifiedTypeMovesCount));

    // Get a list of other types excluding the specified type
    List<String> otherTypes = new ArrayList<>(typeMoves.keySet());
    otherTypes.remove(typeName.toLowerCase());

    if (otherTypes.size() < randomTypeCount) {
      throw new IllegalArgumentException("Not enough other types to select from.");
    }

    // Shuffle the list of other types
    Collections.shuffle(otherTypes);

    // For each of the random types, select 1 move
    for (int i = 0; i < randomTypeCount; i++) {
      String randomType = otherTypes.get(i);
      List<Move> movesOfRandomType = typeMoves.get(randomType);

      if (movesOfRandomType == null || movesOfRandomType.isEmpty()) {
        throw new IllegalArgumentException("No moves available for type " + randomType);
      }

      Collections.shuffle(movesOfRandomType);
      selectedMoves.add(movesOfRandomType.get(0));
    }

    Collections.shuffle(selectedMoves);

    return selectedMoves;
  }
}
