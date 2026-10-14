package com.pokemon.pokidex.Clients;

import com.pokemon.pokidex.Dtos.PokemonNameEntry;
import com.pokemon.pokidex.Dtos.upstream.PokemonListRecord;
import com.pokemon.pokidex.Dtos.upstream.PokemonRecord;
import com.pokemon.pokidex.Dtos.upstream.SpeciesRecord;
import com.pokemon.pokidex.Exceptions.PokemonNotFoundException;
import com.pokemon.pokidex.Exceptions.UpstreamException;
import com.pokemon.pokidex.Exceptions.UpstreamUnavailableException;
import com.pokemon.pokidex.Utils.NameNormalizer;
import com.pokemon.pokidex.Utils.PokeUrlUtils;
import java.util.Comparator;
import java.util.List;
import java.util.OptionalInt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/** Plain, uncached PokeAPI client. Translates HTTP failures into domain exceptions. */
@Component("pokeApiRestClient")
public class PokeApiRestClient implements PokeApiClient {

    private static final Logger log = LoggerFactory.getLogger(PokeApiRestClient.class);
    private static final int ALL = 100_000;

    private final RestTemplate restTemplate;

    public PokeApiRestClient(RestTemplate pokeApiRestTemplate) {
        this.restTemplate = pokeApiRestTemplate;
    }

    @Override
    public List<PokemonNameEntry> fetchNameIndex() {
        log.debug("Upstream: name index");
        PokemonListRecord list = get("/pokemon?limit={limit}", PokemonListRecord.class, "name-index", ALL);
        if (list.results() == null) {
            throw new UpstreamException("PokeAPI returned an empty Pokémon list");
        }
        return list.results().stream()
                .map(ref -> {
                    OptionalInt id = PokeUrlUtils.idFromUrl(ref.url());
                    return id.isPresent()
                            ? new PokemonNameEntry(id.getAsInt(), ref.name(), NameNormalizer.displayName(ref.name()))
                            : null;
                })
                .filter(e -> e != null)
                .sorted(Comparator.comparingInt(PokemonNameEntry::id))
                .toList();                         // immutable snapshot
    }

    @Override
    public PokemonRecord fetchPokemon(String name) {
        log.debug("Upstream: pokemon {}", name);
        return get("/pokemon/{name}", PokemonRecord.class, name, name);
    }

    @Override
    public SpeciesRecord fetchSpecies(String name) {
        log.debug("Upstream: species {}", name);
        return get("/pokemon-species/{name}", SpeciesRecord.class, name, name);
    }

    private <T> T get(String uri, Class<T> type, String resource, Object... vars) {
        try {
            T body = restTemplate.getForObject(uri, type, vars);
            if (body == null) {
                throw new UpstreamException("PokeAPI returned an empty body for " + resource);
            }
            return body;
        } catch (HttpClientErrorException.NotFound e) {
            throw new PokemonNotFoundException(resource);
        } catch (HttpStatusCodeException e) {
            throw new UpstreamException("PokeAPI responded with HTTP " + e.getStatusCode().value(), e);
        } catch (ResourceAccessException e) {
            throw new UpstreamUnavailableException("PokeAPI is unreachable or timed out", e);
        } catch (RestClientException e) {
            throw new UpstreamException("Unexpected error calling PokeAPI", e);
        }
    }
}