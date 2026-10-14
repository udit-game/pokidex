package com.pokemon.pokidex.Clients;

import com.github.benmanes.caffeine.cache.Cache;
import com.pokemon.pokidex.Dtos.PokemonNameEntry;
import com.pokemon.pokidex.Dtos.upstream.PokemonRecord;
import com.pokemon.pokidex.Dtos.upstream.SpeciesRecord;
import com.pokemon.pokidex.Exceptions.PokemonNotFoundException;
import java.util.List;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * Decorator: cache-aside in front of the real client.
 * - Caffeine's get(key, loader) is atomic per key, so concurrent misses trigger ONE upstream call (single-flight).
 * - A loader exception is never cached, except 404s, which go to a short-lived negative cache.
 */
@Primary
@Component
public class CachingPokeApiClient implements PokeApiClient {

    private static final String NAMES_KEY = "all";

    private final PokeApiClient delegate;
    private final Cache<String, PokemonRecord> pokemonCache;
    private final Cache<String, SpeciesRecord> speciesCache;
    private final Cache<String, List<PokemonNameEntry>> nameIndexCache;
    private final Cache<String, Boolean> notFoundCache;

    public CachingPokeApiClient(
            @Qualifier("pokeApiRestClient") PokeApiClient delegate,
            Cache<String, PokemonRecord> pokemonCache,
            Cache<String, SpeciesRecord> speciesCache,
            Cache<String, List<PokemonNameEntry>> nameIndexCache,
            Cache<String, Boolean> notFoundCache) {
        this.delegate = delegate;
        this.pokemonCache = pokemonCache;
        this.speciesCache = speciesCache;
        this.nameIndexCache = nameIndexCache;
        this.notFoundCache = notFoundCache;
    }

    @Override
    public List<PokemonNameEntry> fetchNameIndex() {
        return nameIndexCache.get(NAMES_KEY, k -> delegate.fetchNameIndex());
    }

    @Override
    public PokemonRecord fetchPokemon(String name) {
        return cached("pokemon", name, pokemonCache, delegate::fetchPokemon);
    }

    @Override
    public SpeciesRecord fetchSpecies(String name) {
        return cached("species", name, speciesCache, delegate::fetchSpecies);
    }

    private <V> V cached(String namespace, String key, Cache<String, V> cache, Function<String, V> loader) {
        String negativeKey = namespace + ":" + key;
        if (notFoundCache.getIfPresent(negativeKey) != null) {
            throw new PokemonNotFoundException(key);
        }
        try {
            return cache.get(key, loader);
        } catch (PokemonNotFoundException e) {
            notFoundCache.put(negativeKey, Boolean.TRUE);
            throw e;
        }
    }
}