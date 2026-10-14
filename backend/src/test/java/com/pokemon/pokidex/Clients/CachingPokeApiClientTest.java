package com.pokemon.pokidex.Clients;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.pokemon.pokidex.Dtos.PokemonNameEntry;
import com.pokemon.pokidex.Dtos.upstream.PokemonRecord;
import com.pokemon.pokidex.Dtos.upstream.SpeciesRecord;
import com.pokemon.pokidex.Exceptions.PokemonNotFoundException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CachingPokeApiClientTest {

    private final AtomicLong nanos = new AtomicLong();
    private PokeApiClient delegate;

    @BeforeEach
    void setUp() {
        delegate = mock(PokeApiClient.class);
        when(delegate.fetchPokemon(anyString())).thenAnswer(i -> mock(PokemonRecord.class));
    }

    private <V> Cache<String, V> cache(long max, Duration ttl) {
        return Caffeine.newBuilder()
                .maximumSize(max)
                .expireAfterWrite(ttl)
                .ticker(nanos::get)            // fake clock
                .executor(Runnable::run)       // synchronous maintenance
                .build();
    }

    private CachingPokeApiClient client(Cache<String, PokemonRecord> pokemon) {
        Cache<String, SpeciesRecord> species = cache(10, Duration.ofMinutes(10));
        Cache<String, List<PokemonNameEntry>> names = cache(1, Duration.ofHours(1));
        Cache<String, Boolean> notFound = cache(10, Duration.ofSeconds(60));
        return new CachingPokeApiClient(delegate, pokemon, species, names, notFound);
    }

    @Test
    void secondCallIsServedFromCacheUntilTtlExpires() {
        CachingPokeApiClient client = client(cache(10, Duration.ofMinutes(10)));

        client.fetchPokemon("pikachu");
        client.fetchPokemon("pikachu");
        verify(delegate, times(1)).fetchPokemon("pikachu");

        nanos.addAndGet(TimeUnit.MINUTES.toNanos(11));   // jump past TTL

        client.fetchPokemon("pikachu");
        verify(delegate, times(2)).fetchPokemon("pikachu");
    }

    @Test
    void notFoundIsNegativelyCached() {
        when(delegate.fetchPokemon("pikchuu")).thenThrow(new PokemonNotFoundException("pikchuu"));
        CachingPokeApiClient client = client(cache(10, Duration.ofMinutes(10)));

        assertThatThrownBy(() -> client.fetchPokemon("pikchuu")).isInstanceOf(PokemonNotFoundException.class);
        assertThatThrownBy(() -> client.fetchPokemon("pikchuu")).isInstanceOf(PokemonNotFoundException.class);

        verify(delegate, times(1)).fetchPokemon("pikchuu");   // second 404 never hit upstream
    }

    @Test
    void cacheNeverExceedsMaxEntries() {
        Cache<String, PokemonRecord> small = cache(2, Duration.ofMinutes(10));
        CachingPokeApiClient client = client(small);

        client.fetchPokemon("a");
        client.fetchPokemon("b");
        client.fetchPokemon("c");
        small.cleanUp();

        assertThat(small.estimatedSize()).isLessThanOrEqualTo(2);
    }
}