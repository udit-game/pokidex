package com.pokemon.pokidex.Config;

import com.pokemon.pokidex.Clients.PokeApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CacheWarmup {

    private static final Logger log = LoggerFactory.getLogger(CacheWarmup.class);
    private final PokeApiClient client;

    public CacheWarmup(PokeApiClient client) {
        this.client = client;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void warm() {
        try {
            int size = client.fetchNameIndex().size();
            log.info("Name index warmed: {} entries", size);
        } catch (Exception e) {
            log.warn("Name index warm-up failed, will load lazily on first request: {}", e.getMessage());
        }
    }
}