package com.pokemon.pokidex.Config;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "pokedex")
public record PokedexProperties(Upstream upstream, Caches cache, Search search) {

    public record Upstream(
            String baseUrl,
            Duration connectTimeout,
            Duration readTimeout,
            int maxTotalConnections,
            int maxPerRoute,
            int maxRetries) {}

    public record CacheSpec(long maxEntries, Duration ttl) {}

    public record Caches(CacheSpec pokemon, CacheSpec species, CacheSpec names, CacheSpec notFound) {}

    public record Search(int defaultLimit, int maxLimit) {}
}