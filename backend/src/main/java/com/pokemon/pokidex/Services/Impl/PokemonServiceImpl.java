package com.pokemon.pokidex.Services.Impl;

import com.pokemon.pokidex.Clients.PokeApiClient;
import com.pokemon.pokidex.Dtos.PokemonDetailDto;
import com.pokemon.pokidex.Dtos.PokemonNameEntry;
import com.pokemon.pokidex.Dtos.upstream.PokemonRecord;
import com.pokemon.pokidex.Dtos.upstream.SpeciesRecord;
import com.pokemon.pokidex.Exceptions.PokemonNotFoundException;
import com.pokemon.pokidex.Services.PokemonService;
import com.pokemon.pokidex.Utils.NameNormalizer;
import com.pokemon.pokidex.Utils.PokemonMapper;
import org.springframework.stereotype.Service;

@Service
public class PokemonServiceImpl implements PokemonService {

    private static final int MAX_ID_DIGITS = 7;

    private final PokeApiClient client;

    public PokemonServiceImpl(PokeApiClient client) {
        this.client = client;
    }

    @Override
    public PokemonDetailDto getPokemon(String idOrName) {
        String key = NameNormalizer.toLookupKey(idOrName);
        // numeric ids are resolved to a name via the cached index, so "25" and "pikachu" share ONE cache entry
        String name = NameNormalizer.isNumeric(key) ? resolveName(key) : key;

        PokemonRecord pokemon = client.fetchPokemon(name);
        // forms like "rotom-heat" share a species with a different slug
        SpeciesRecord species = client.fetchSpecies(pokemon.species().name());

        return PokemonMapper.toDetail(pokemon, species);
    }

    private String resolveName(String numericKey) {
        if (numericKey.length() > MAX_ID_DIGITS) {
            throw new PokemonNotFoundException(numericKey);
        }
        int id = Integer.parseInt(numericKey);
        return client.fetchNameIndex().stream()
                .filter(e -> e.id() == id)
                .map(PokemonNameEntry::name)
                .findFirst()
                .orElseThrow(() -> new PokemonNotFoundException(numericKey));
    }
}