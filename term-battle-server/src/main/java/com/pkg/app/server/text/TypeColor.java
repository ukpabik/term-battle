package com.pkg.app.server.text;

public class TypeColor {

    public static String colorType(String typeName) {
        switch (typeName.toLowerCase()) {
            case "earth":
                return AnsiText.color(typeName, AnsiText.GREEN);
            case "air":
                return AnsiText.color(typeName, AnsiText.CYAN);
            case "fire":
                return AnsiText.color(typeName, AnsiText.RED);
            case "water":
                return AnsiText.color(typeName, AnsiText.BLUE);
            case "light":
                return AnsiText.color(typeName, AnsiText.YELLOW);
            case "darkness":
                return AnsiText.color(typeName, AnsiText.PURPLE);
            default:
                return AnsiText.color(typeName, AnsiText.WHITE);
        }
    }
}
