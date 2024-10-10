package com.pkg.app.party.monster.automation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.pkg.app.party.monster.Monster;
import com.pkg.app.party.monster.Type;
import com.pkg.app.party.monster.automation.TypeSuffixes;
import com.pkg.app.party.monster.automation.TypePrefixes;

public class MonsterGenerator {
    private static final Random RANDOM = new Random();

    // Generates a list of monsters with random stats and type
    public static ArrayList<Monster> generateMonsters(int count) {
        ArrayList<Monster> monsters = new ArrayList<>();
        HashSet<String> existingNames = new HashSet<>();
        for (int i = 0; i < count; i++) {
            String type = getRandomType();
            String name = generateUniqueName(type, existingNames);
            int[] stats = generateStatsBasedOnType(type);
            monsters.add(new Monster(name, stats[0], stats[1], stats[2], new Type(type)));
        }
        return monsters;
    }

    // Generates a unique name for a monster
    private static String generateUniqueName(String type, Set<String> existingNames) {
        String name;
        List<String> typePrefixes = TypePrefixes.getPrefixes(type);
        List<String> typeSuffixes = TypeSuffixes.getSuffixes(type);

        if (typePrefixes.isEmpty() || typeSuffixes.isEmpty()) {
            name = generateGenericName(existingNames);
        } else {
            do {
                String prefix = typePrefixes.get(RANDOM.nextInt(typePrefixes.size()));
                String suffix = typeSuffixes.get(RANDOM.nextInt(typeSuffixes.size()));
                name = prefix + suffix;
            } while (existingNames.contains(name.toLowerCase()));
        }

        existingNames.add(name.toLowerCase());
        return name;
    }

    // Fallback name generator
    private static String generateGenericName(Set<String> existingNames) {
        String[] genericPrefixes = {"Dark", "Light", "Shadow", "Thunder", "Storm", "Mystic", "Ancient"};
        String[] genericSuffixes = {"Beast", "Guardian", "Sentinel", "Warden", "Assassin", "Warrior", "Mage"};

        String name;
        do {
            String prefix = genericPrefixes[RANDOM.nextInt(genericPrefixes.length)];
            String suffix = genericSuffixes[RANDOM.nextInt(genericSuffixes.length)];
            name = prefix + suffix;
        } while (existingNames.contains(name.toLowerCase()));

        return name;
    }

    // Randomly generates a type
    private static String getRandomType() {
        String[] types = {"earth", "air", "fire", "water", "light", "darkness"};
        return types[RANDOM.nextInt(types.length)];
    }

    // Generates stats based on type
    private static int[] generateStatsBasedOnType(String type) {
        int health = 0;
        int attack = 0;
        int speed = 0;

        switch (type) {
            case "earth":
                health = getRandomNumber(120, 150);
                attack = getRandomNumber(60, 80);
                speed = getRandomNumber(30, 50);
                break;
            case "air":
                health = getRandomNumber(80, 100);
                attack = getRandomNumber(50, 70);
                speed = getRandomNumber(90, 120);
                break;
            case "fire":
                health = getRandomNumber(100, 130);
                attack = getRandomNumber(80, 100);
                speed = getRandomNumber(60, 80);
                break;
            case "water":
                health = getRandomNumber(110, 140);
                attack = getRandomNumber(70, 90);
                speed = getRandomNumber(50, 70);
                break;
            case "light":
                health = getRandomNumber(90, 120);
                attack = getRandomNumber(65, 85);
                speed = getRandomNumber(80, 100);
                break;
            case "darkness":
                health = getRandomNumber(95, 125);
                attack = getRandomNumber(75, 95);
                speed = getRandomNumber(70, 90);
                break;
            default:
                // Default stats for unknown types
                health = getRandomNumber(80, 100);
                attack = getRandomNumber(50, 70);
                speed = getRandomNumber(50, 70);
                break;
        }

        return new int[]{health, attack, speed};
    }

    private static int getRandomNumber(int min, int max) {
        return RANDOM.nextInt((max - min) + 1) + min;
    }
}

