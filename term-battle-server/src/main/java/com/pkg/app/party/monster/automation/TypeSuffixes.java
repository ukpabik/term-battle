package com.pkg.app.party.monster.automation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TypeSuffixes {
  private static final HashMap<String, List<String>> suffixesByType = new HashMap<>();

  static {
    suffixesByType.put("earth", Arrays.asList(
          "grim", "knarr", "thor", "grash", "lorn", "berk", "dorn", "fark",
          "nark", "grom", "barr", "tor", "crux", "varn", "harn", "krel",
          "lurk", "morn", "skar", "tharn", "drum", "grith", "mash", "brank",
          "drax"
          ));

    suffixesByType.put("air", Arrays.asList(
          "zyl", "vex", "mir", "quill", "flyn", "syr", "wyn", "nyx",
          "lix", "tarn", "dris", "quor", "lyth", "kyl", "zyr", "fyr",
          "nix", "ryl", "cly", "vyr", "skyl", "wynth", "aeris", "thryl",
          "glyn"
          ));

    suffixesByType.put("fire", Arrays.asList(
          "thra", "gorn", "lynn", "xen", "pril", "vyr", "tor", "zir",
          "blith", "vash", "kraz", "nith", "drax", "lith", "karn",
          "gnor", "shyr", "vorn", "threx", "zorn", "pyra", "blazeon",
          "fira", "rax"
          ));

    suffixesByType.put("water", Arrays.asList(
          "quil", "sarn", "varn", "nox", "lyra", "myl", "grix", "zyl",
          "thor", "dyl", "brix", "clan", "fyrn", "wyr", "ryn", "lax",
          "zarn", "nix", "taryn", "glyn", "marx", "jyn", "klyr", "lyth",
          "vryn"
          ));
    suffixesByType.put("light", Arrays.asList(
          "solis", "lyth", "cryl", "syl", "vynn", "zir", "phyn",
          "glyn", "kyra", "lynn", "syra", "thrya", "phel",
          "vexa", "lythia", "zora", "nir", "clyra", "lysa", "strix",
          "lumix", "solara", "phire", "glara", "lumis"
          ));


    suffixesByType.put("darkness", Arrays.asList(
          "zyl", "thor", "kyn", "gryph", "thryn", "mal", "ryn",
          "zarn", "kril", "gryx", "vryn", "syl", "dray",
          "korn", "syl", "myr", "thrax", "zyr", "nix",
          "grysh", "drax", "kris", "sarn", "nym", "shyr"
          ));

  }

  public static List<String> getSuffixes(String type) {
    return suffixesByType.getOrDefault(type.toLowerCase(), new ArrayList<>());
  }
}

