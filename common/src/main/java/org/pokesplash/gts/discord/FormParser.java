package org.pokesplash.gts.discord;

public abstract class FormParser {

    public static String parseForm(String form) {
        return switch (form) {
            case "Alola" -> "-alolan";
            case "Galar" -> "-galarian";
            case "Hisui" -> "-hisuian";
            case "Paldea" -> "-paldean";
            default -> "";
        };
    }
}
