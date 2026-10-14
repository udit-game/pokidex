package com.pokemon.pokidex.Utils;

import java.util.regex.Pattern;

public final class TextUtils {

    private static final Pattern LINE_BREAKS = Pattern.compile("[\\n\\f\\r\\u00ad]+");
    private static final Pattern SPACES = Pattern.compile("\\s{2,}");

    private TextUtils() {}

    /** PokeAPI flavor text contains \n, \f and soft hyphens. */
    public static String cleanFlavorText(String raw) {
        if (raw == null) return null;
        String s = LINE_BREAKS.matcher(raw).replaceAll(" ");
        return SPACES.matcher(s).replaceAll(" ").trim();
    }

    /** Classic two-row Levenshtein distance. */
    public static int levenshtein(String a, String b) {
        int[] prev = new int[b.length() + 1];
        int[] curr = new int[b.length() + 1];
        for (int j = 0; j <= b.length(); j++) prev[j] = j;
        for (int i = 1; i <= a.length(); i++) {
            curr[0] = i;
            for (int j = 1; j <= b.length(); j++) {
                int cost = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;
                curr[j] = Math.min(Math.min(curr[j - 1] + 1, prev[j] + 1), prev[j - 1] + cost);
            }
            int[] tmp = prev; prev = curr; curr = tmp;
        }
        return prev[b.length()];
    }
}