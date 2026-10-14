package com.pokemon.pokidex.Utils;

import java.util.OptionalInt;

public final class PokeUrlUtils {

    private static final String SPRITE_BASE =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/";

    private PokeUrlUtils() {}

    /** "https://pokeapi.co/api/v2/pokemon/25/" -> 25 */
    public static OptionalInt idFromUrl(String url) {
        if (url == null || url.isBlank()) return OptionalInt.empty();
        String trimmed = url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
        try {
            return OptionalInt.of(Integer.parseInt(trimmed.substring(trimmed.lastIndexOf('/') + 1)));
        } catch (NumberFormatException e) {
            return OptionalInt.empty();
        }
    }

    /** Small thumbnail for list rows: derived from id, so no upstream call per row. */
    public static String thumbnailUrl(int id) {
        return SPRITE_BASE + id + ".png";
    }

    public static String artworkUrl(int id) {
        return SPRITE_BASE + "other/official-artwork/" + id + ".png";
    }

    public static String shinyArtworkUrl(int id) {
        return SPRITE_BASE + "other/official-artwork/shiny/" + id + ".png";
    }
}