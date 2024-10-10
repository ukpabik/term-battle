package com.pkg.app.party.monster.automation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TypePrefixes {
  private static final HashMap<String, List<String>> prefixesByType = new HashMap<>();

  static {
    prefixesByType.put("earth", Arrays.asList(
          "Grav", "Bram", "Terr", "Gron", "Dur", "Morr", "Syl", "Bran",
          "Karn", "Vul", "Thar", "Orin", "Drax", "Gryn", "Belt",
          "Marv", "Cryn", "Fald", "Harn", "Jorn", "Lurk", "Nyr",
          "Pald", "Quor", "Rav"
          ));


    prefixesByType.put("air", Arrays.asList(
          "Aer", "Vex", "Sylph", "Zeph", "Lyr", "Fyl", "Blyth", "Nim",
          "Kyra", "Syr", "Ryze", "Typh", "Eir", "Fyre", "Gryth",
          "Hyr", "Ira", "Jyr", "Kyl", "Lyth", "Myra", "Nyx",
          "Ory", "Pyr", "Qyre"
          ));



    prefixesByType.put("fire", Arrays.asList(
          "Pyra", "Ignis", "Vulc", "Blaz", "Ember", "Fira", "Scor",
          "Solan", "Fyrn", "Krynn", "Zara", "Ryx", "Drak", "Fynn",
          "Grym", "Hax", "Jynx", "Kyro", "Lyth", "Morn", "Nyra",
          "Oryn", "Pyrra", "Quynn", "Ryza"
          ));

    prefixesByType.put("water", Arrays.asList(
          "Hydr", "Marin", "Aquar", "Nerix", "Cyr", "Tidal",
          "Vapor", "Ryv", "Morl", "Drys", "Syl", "Fyrn",
          "Lyris", "Kyra", "Myrn", "Nirv", "Orin", "Pyran",
          "Quill", "Ryven", "Syrin", "Tylis", "Vyrn", "Wyren",
          "Zyr"
          ));


    prefixesByType.put("light", Arrays.asList(
          "Lumin", "Radi", "Sol", "Gleam", "Phos", "Bril",
          "Helio", "Aurix", "Celes", "Stell", "Glint", "Prism",
          "Flar", "Shine", "Illum", "Vera", "Glory", "Rayn",
          "Flash", "Spark", "Shyr", "Glim", "Zora", "Pyre",
          "Nyx"
          ));


    prefixesByType.put("darkness", Arrays.asList(
          "Umbra", "Noct", "Shadow", "Ebon", "Gloom",
          "Phant", "Crypt", "Nyx", "Vesper", "Teneb",
          "Obliv", "Duskr", "Sable", "Veil", "Void",
          "Cimmer", "Hallow", "Morth", "Syrin", "Ravyn",
          "Vorn", "Drake", "Blight", "Mourn", "Reven"
          ));

  }

  public static List<String> getPrefixes(String type) {
    return prefixesByType.getOrDefault(type.toLowerCase(), new ArrayList<>());
  }
}

