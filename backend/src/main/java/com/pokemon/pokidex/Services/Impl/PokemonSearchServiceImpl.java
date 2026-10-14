package com.pokemon.pokidex.Services.Impl;

import com.pokemon.pokidex.Clients.PokeApiClient;
import com.pokemon.pokidex.Config.PokedexProperties;
import com.pokemon.pokidex.Dtos.PageDto;
import com.pokemon.pokidex.Dtos.PokemonNameEntry;
import com.pokemon.pokidex.Dtos.PokemonSummaryDto;
import com.pokemon.pokidex.Services.PokemonSearchService;
import com.pokemon.pokidex.Utils.NameNormalizer;
import com.pokemon.pokidex.Utils.PokeUrlUtils;
import com.pokemon.pokidex.Utils.TextUtils;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PokemonSearchServiceImpl implements PokemonSearchService {

    private static final int NO_MATCH = Integer.MAX_VALUE;
    private static final int FORM_ID_THRESHOLD = 10_000;   // ids >= 10000 are alternate forms (gmax, mega...)
    private static final int MIN_FUZZY_LENGTH = 3;

    private final PokeApiClient client;
    private final PokedexProperties props;

    public PokemonSearchServiceImpl(PokeApiClient client, PokedexProperties props) {
        this.client = client;
        this.props = props;
    }

    @Override
    public PageDto<PokemonSummaryDto> search(String rawQuery, int limit, int offset) {
        int safeLimit = Math.min(Math.max(limit, 1), props.search().maxLimit());
        int safeOffset = Math.max(offset, 0);
        String q = NameNormalizer.normalize(rawQuery);

        List<PokemonNameEntry> index = client.fetchNameIndex();
        List<PokemonNameEntry> matches = q.isEmpty() ? index : rank(index, q);

        int from = (int) Math.min(safeOffset, matches.size());
        int to = (int) Math.min((long) from + safeLimit, matches.size());
        List<PokemonSummaryDto> items = matches.subList(from, to).stream()
                .map(e -> new PokemonSummaryDto(e.id(), e.name(), e.displayName(), PokeUrlUtils.thumbnailUrl(e.id())))
                .toList();

        return new PageDto<>(items, matches.size(), safeLimit, safeOffset);
    }

    private record Scored(PokemonNameEntry entry, int score) {}

    private List<PokemonNameEntry> rank(List<PokemonNameEntry> index, String q) {
        return index.stream()
                .map(e -> new Scored(e, score(e, q)))
                .filter(s -> s.score() != NO_MATCH)
                .sorted(Comparator.comparingInt(Scored::score)
                        .thenComparingInt(s -> s.entry().id() >= FORM_ID_THRESHOLD ? 1 : 0)
                        .thenComparingInt(s -> s.entry().name().length())
                        .thenComparingInt(s -> s.entry().id()))
                .map(Scored::entry)
                .toList();
    }

    private int score(PokemonNameEntry e, String q) {
        String name = e.name();

        if (NameNormalizer.isNumeric(q)) {
            String id = String.valueOf(e.id());
            if (id.equals(q)) return 0;
            return id.startsWith(q) ? 1 : NO_MATCH;
        }

        if (name.equals(q)) return 0;
        if (name.startsWith(q)) return 1;
        if (hasWordPrefix(name, q)) return 2;
        if (name.contains(q)) return 3;

        if (q.length() >= MIN_FUZZY_LENGTH) {
            int maxEdits = q.length() <= 4 ? 1 : 2;
            if (Math.abs(name.length() - q.length()) <= maxEdits
                    && TextUtils.levenshtein(name, q) <= maxEdits) {
                return 4;
            }
        }
        return NO_MATCH;
    }

    /** "mime" matches "mr-mime". */
    private boolean hasWordPrefix(String name, String q) {
        return Arrays.stream(name.split("-")).anyMatch(part -> part.startsWith(q));
    }
}