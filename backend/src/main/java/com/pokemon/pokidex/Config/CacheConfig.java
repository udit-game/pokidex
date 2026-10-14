package com.pokemon.pokidex.Config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.pokemon.pokidex.Dtos.PokemonNameEntry;
import com.pokemon.pokidex.Dtos.upstream.PokemonRecord;
import com.pokemon.pokidex.Dtos.upstream.SpeciesRecord;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.cache.CaffeineCacheMetrics;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Four Caffeine caches, each with its own TTL (expireAfterWrite) and max size (size-based eviction).
 * Hit/miss/eviction stats are exposed at /actuator/metrics/cache.gets?tag=cache:pokemon
 */
@Configuration
public class CacheConfig {

    private static <V> Cache<String, V> build(String name, PokedexProperties.CacheSpec spec, MeterRegistry registry) {
        Cache<String, V> cache = Caffeine.newBuilder()
                .maximumSize(spec.maxEntries())
                .expireAfterWrite(spec.ttl())
                .recordStats()
                .build();
        CaffeineCacheMetrics.monitor(registry, cache, name);
        return cache;
    }

    @Bean
    public Cache<String, PokemonRecord> pokemonCache(PokedexProperties p, MeterRegistry r) {
        return build("pokemon", p.cache().pokemon(), r);
    }

    @Bean
    public Cache<String, SpeciesRecord> speciesCache(PokedexProperties p, MeterRegistry r) {
        return build("species", p.cache().species(), r);
    }

    /** Single entry (key "all") holding the immutable name index. */
    @Bean
    public Cache<String, List<PokemonNameEntry>> nameIndexCache(PokedexProperties p, MeterRegistry r) {
        return build("names", p.cache().names(), r);
    }

    /** Negative cache for 404s. */
    @Bean
    public Cache<String, Boolean> notFoundCache(PokedexProperties p, MeterRegistry r) {
        return build("notFound", p.cache().notFound(), r);
    }
}