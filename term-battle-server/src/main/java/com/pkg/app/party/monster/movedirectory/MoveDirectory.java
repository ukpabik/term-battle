package com.pkg.app.party.monster.movedirectory;

import com.pkg.app.party.monster.Move;
import com.pkg.app.party.monster.Type;
import java.util.List;
import java.util.ArrayList;

public class MoveDirectory{

  // Fire moves
  public static List<Move> getFireMoves() {
    List<Move> fireMoves = new ArrayList<>();
    fireMoves.add(new Move("Flame Burst", 40, new Type("fire"), 15));
    fireMoves.add(new Move("Heat Wave", 50, new Type("fire"), 10));
    fireMoves.add(new Move("Ember", 30, new Type("fire"), 25));
    fireMoves.add(new Move("Fire Spin", 35, new Type("fire"), 20));
    fireMoves.add(new Move("Inferno", 60, new Type("fire"), 5)); 
    fireMoves.add(new Move("Lava Plume", 55, new Type("fire"), 7));
    fireMoves.add(new Move("Flame Charge", 40, new Type("fire"), 15));
    fireMoves.add(new Move("Fire Fang", 35, new Type("fire"), 20));
    fireMoves.add(new Move("Blaze Kick", 45, new Type("fire"), 12));
    fireMoves.add(new Move("Overheat", 70, new Type("fire"), 5)); 
    fireMoves.add(new Move("Flamethrower", 60, new Type("fire"), 10));
    fireMoves.add(new Move("Sunny Day", 30, new Type("fire"), 20));
    fireMoves.add(new Move("Eruption", 80, new Type("fire"), 3)); 
    fireMoves.add(new Move("Scorching Sands", 40, new Type("fire"), 15));
    fireMoves.add(new Move("Fire Punch", 50, new Type("fire"), 15));
    fireMoves.add(new Move("Will-O-Wisp", 20, new Type("fire"), 25));
    fireMoves.add(new Move("Searing Shot", 60, new Type("fire"), 8));
    fireMoves.add(new Move("Magma Storm", 75, new Type("fire"), 5));
    fireMoves.add(new Move("Burn Up", 70, new Type("fire"), 5));
    fireMoves.add(new Move("Pyro Ball", 65, new Type("fire"), 7));
    return fireMoves;
  }

  // Water moves
  public static List<Move> getWaterMoves() {
    List<Move> waterMoves = new ArrayList<>();
    waterMoves.add(new Move("Water Gun", 30, new Type("water"), 25));
    waterMoves.add(new Move("Bubble Beam", 35, new Type("water"), 20));
    waterMoves.add(new Move("Aqua Tail", 45, new Type("water"), 15));
    waterMoves.add(new Move("Hydro Pump", 50, new Type("water"), 10));
    waterMoves.add(new Move("Rain Dance", 40, new Type("water"), 15));
    waterMoves.add(new Move("Surf", 55, new Type("water"), 10));
    waterMoves.add(new Move("Water Pulse", 40, new Type("water"), 20));
    waterMoves.add(new Move("Scald", 45, new Type("water"), 15));
    waterMoves.add(new Move("Aqua Jet", 35, new Type("water"), 20));
    waterMoves.add(new Move("Whirlpool", 30, new Type("water"), 20));
    waterMoves.add(new Move("Muddy Water", 50, new Type("water"), 12));
    waterMoves.add(new Move("Hydro Cannon", 70, new Type("water"), 5));
    waterMoves.add(new Move("Waterfall", 60, new Type("water"), 10));
    waterMoves.add(new Move("Aqua Ring", 20, new Type("water"), 25));
    waterMoves.add(new Move("Ocean Pulse", 45, new Type("water"), 15));
    waterMoves.add(new Move("Tsunami Wave", 65, new Type("water"), 7));
    waterMoves.add(new Move("Rain Surge", 55, new Type("water"), 10));
    waterMoves.add(new Move("Geyser Burst", 75, new Type("water"), 5));
    waterMoves.add(new Move("Water Spout", 80, new Type("water"), 3));
    waterMoves.add(new Move("Tidal Crash", 65, new Type("water"), 7));
    return waterMoves;
  }

  // Earth moves
  public static List<Move> getEarthMoves() {
    List<Move> earthMoves = new ArrayList<>();
    earthMoves.add(new Move("Mud Slap", 30, new Type("earth"), 25));
    earthMoves.add(new Move("Earthquake", 50, new Type("earth"), 10));
    earthMoves.add(new Move("Sandstorm", 35, new Type("earth"), 20));
    earthMoves.add(new Move("Rock Slide", 40, new Type("earth"), 15));
    earthMoves.add(new Move("Stone Edge", 55, new Type("earth"), 8));
    earthMoves.add(new Move("Fissure", 60, new Type("earth"), 5));
    earthMoves.add(new Move("Drill Run", 45, new Type("earth"), 15));
    earthMoves.add(new Move("Magnitude", 50, new Type("earth"), 12));
    earthMoves.add(new Move("Dig", 35, new Type("earth"), 20));
    earthMoves.add(new Move("Mud Bomb", 40, new Type("earth"), 20));
    earthMoves.add(new Move("Bulldoze", 45, new Type("earth"), 15));
    earthMoves.add(new Move("Earth Power", 60, new Type("earth"), 10));
    earthMoves.add(new Move("Sand Tomb", 30, new Type("earth"), 25));
    earthMoves.add(new Move("Rock Blast", 35, new Type("earth"), 20));
    earthMoves.add(new Move("Seismic Slam", 70, new Type("earth"), 5));
    earthMoves.add(new Move("Tectonic Shift", 65, new Type("earth"), 7));
    earthMoves.add(new Move("Quicksand", 55, new Type("earth"), 10));
    earthMoves.add(new Move("Landslide", 75, new Type("earth"), 3));
    earthMoves.add(new Move("Stone Barrage", 60, new Type("earth"), 8));
    earthMoves.add(new Move("Boulder Bash", 65, new Type("earth"), 7));
    return earthMoves;
  }

  // Air moves
  public static List<Move> getAirMoves() {
    List<Move> airMoves = new ArrayList<>();
    airMoves.add(new Move("Gust", 30, new Type("air"), 25));
    airMoves.add(new Move("Air Cutter", 35, new Type("air"), 20));
    airMoves.add(new Move("Aerial Ace", 40, new Type("air"), 15));
    airMoves.add(new Move("Hurricane", 50, new Type("air"), 10));
    airMoves.add(new Move("Sky Attack", 60, new Type("air"), 7));
    airMoves.add(new Move("Feather Dance", 40, new Type("air"), 20));
    airMoves.add(new Move("Wind Slash", 45, new Type("air"), 15));
    airMoves.add(new Move("Tailwind", 30, new Type("air"), 25));
    airMoves.add(new Move("Air Slash", 50, new Type("air"), 12));
    airMoves.add(new Move("Fly", 60, new Type("air"), 10));
    airMoves.add(new Move("Twister", 40, new Type("air"), 20));
    airMoves.add(new Move("Whirlwind", 30, new Type("air"), 25));
    airMoves.add(new Move("Sky Drop", 55, new Type("air"), 10));
    airMoves.add(new Move("Gale Force", 65, new Type("air"), 7));
    airMoves.add(new Move("Cyclone", 70, new Type("air"), 5));
    airMoves.add(new Move("Tornado Spin", 50, new Type("air"), 12));
    airMoves.add(new Move("Wind Blast", 60, new Type("air"), 10));
    airMoves.add(new Move("Aeroblast", 75, new Type("air"), 5));
    airMoves.add(new Move("Soar Strike", 65, new Type("air"), 7));
    airMoves.add(new Move("Sky Surge", 70, new Type("air"), 5));
    return airMoves;
  }

  // Light moves
  public static List<Move> getLightMoves() {
    List<Move> lightMoves = new ArrayList<>();
    lightMoves.add(new Move("Flash", 30, new Type("light"), 25));
    lightMoves.add(new Move("Solar Beam", 50, new Type("light"), 10));
    lightMoves.add(new Move("Photon Blast", 45, new Type("light"), 15));
    lightMoves.add(new Move("Illuminate", 35, new Type("light"), 20));
    lightMoves.add(new Move("Light Screen", 40, new Type("light"), 20));
    lightMoves.add(new Move("Radiant Beam", 55, new Type("light"), 8));
    lightMoves.add(new Move("Sun Flare", 60, new Type("light"), 7));
    lightMoves.add(new Move("Luminous Wave", 50, new Type("light"), 12));
    lightMoves.add(new Move("Dazzling Gleam", 45, new Type("light"), 15));
    lightMoves.add(new Move("Prism Blast", 55, new Type("light"), 8));
    lightMoves.add(new Move("Bright Burst", 40, new Type("light"), 20));
    lightMoves.add(new Move("Sparkling Ray", 35, new Type("light"), 25));
    lightMoves.add(new Move("Starlight Strike", 60, new Type("light"), 10));
    lightMoves.add(new Move("Shine Wave", 45, new Type("light"), 15));
    lightMoves.add(new Move("Daybreak", 65, new Type("light"), 7));
    lightMoves.add(new Move("Glare", 20, new Type("light"), 30));
    lightMoves.add(new Move("Sun Beam", 55, new Type("light"), 10));
    lightMoves.add(new Move("Supernova", 75, new Type("light"), 3));
    lightMoves.add(new Move("Radiance", 70, new Type("light"), 5));
    lightMoves.add(new Move("Blinding Strike", 65, new Type("light"), 7));
    return lightMoves;
  }

  // Darkness moves
  public static List<Move> getDarknessMoves() {
    List<Move> darknessMoves = new ArrayList<>();
    darknessMoves.add(new Move("Shadow Sneak", 30, new Type("darkness"), 25));
    darknessMoves.add(new Move("Dark Pulse", 40, new Type("darkness"), 20));
    darknessMoves.add(new Move("Night Slash", 35, new Type("darkness"), 20));
    darknessMoves.add(new Move("Shadow Ball", 45, new Type("darkness"), 15));
    darknessMoves.add(new Move("Blackout", 50, new Type("darkness"), 10));
    darknessMoves.add(new Move("Phantom Force", 55, new Type("darkness"), 8));
    darknessMoves.add(new Move("Oblivion", 70, new Type("darkness"), 5));
    darknessMoves.add(new Move("Dark Void", 30, new Type("darkness"), 25));
    darknessMoves.add(new Move("Moonlight Slash", 45, new Type("darkness"), 15));
    darknessMoves.add(new Move("Eclipse", 60, new Type("darkness"), 7));
    darknessMoves.add(new Move("Nightmare", 50, new Type("darkness"), 10));
    darknessMoves.add(new Move("Shadow Claw", 40, new Type("darkness"), 20));
    darknessMoves.add(new Move("Spectral Strike", 55, new Type("darkness"), 8));
    darknessMoves.add(new Move("Midnight Burst", 65, new Type("darkness"), 7));
    darknessMoves.add(new Move("Void Strike", 60, new Type("darkness"), 7));
    darknessMoves.add(new Move("Dark Flame", 45, new Type("darkness"), 15));
    darknessMoves.add(new Move("Shadow Strike", 55, new Type("darkness"), 8));
    darknessMoves.add(new Move("Doom", 75, new Type("darkness"), 3)); 
    darknessMoves.add(new Move("Black Hole", 80, new Type("darkness"), 2)); 
    darknessMoves.add(new Move("Nightfall", 65, new Type("darkness"), 7));
    return darknessMoves;
  }
}

