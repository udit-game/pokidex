package com.pokemon.pokidex.Utils;

import com.pokemon.pokidex.Exceptions.InvalidInputException;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/** "Mr. Mime" -> "mr-mime", "Farfetch'd" -> "farfetchd", "Nidoran♀" -> "nidoran-f", "Flabébé" -> "flabebe". */
public final class NameNormalizer {

    private static final int MAX_LENGTH = 60;
    private static final Pattern DIACRITICS = Pattern.compile("\\p{M}+");
    private static final Pattern SEPARATORS = Pattern.compile("[\\s_]+");
    private static final Pattern ILLEGAL = Pattern.compile("[^a-z0-9-]");
    private static final Pattern MULTI_HYPHEN = Pattern.compile("-{2,}");
    private static final Pattern EDGE_HYPHEN = Pattern.compile("^-|-$");
    private static final Pattern SLUG = Pattern.compile("^[a-z0-9]+(-[a-z0-9]+)*$");

    private NameNormalizer() {}

    /** Lenient: never throws. Used for search queries. */
    public static String normalize(String raw) {
        if (raw == null) return "";
        String s = raw.trim().toLowerCase(Locale.ROOT).replace("♀", "-f").replace("♂", "-m");
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = DIACRITICS.matcher(s).replaceAll("");
        s = SEPARATORS.matcher(s).replaceAll("-");
        s = ILLEGAL.matcher(s).replaceAll("");
        s = MULTI_HYPHEN.matcher(s).replaceAll("-");
        return EDGE_HYPHEN.matcher(s).replaceAll("");
    }

    /** Strict: returns a safe id-or-name key for lookups or throws InvalidInputException. */
    public static String toLookupKey(String raw) {
        String key = normalize(raw);
        if (key.isEmpty() || key.length() > MAX_LENGTH || !SLUG.matcher(key).matches()) {
            throw new InvalidInputException("Invalid Pokémon name or id");
        }
        return key;
    }

    public static boolean isNumeric(String s) {
        return !s.isEmpty() && s.chars().allMatch(Character::isDigit);
    }

    public static String displayName(String slug) {
        return Arrays.stream(slug.split("-"))
                .filter(p -> !p.isEmpty())
                .map(p -> Character.toUpperCase(p.charAt(0)) + p.substring(1))
                .collect(Collectors.joining(" "));
    }
}