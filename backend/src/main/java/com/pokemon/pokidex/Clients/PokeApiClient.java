package com.pokemon.pokidex.Clients;

import com.pokemon.pokidex.Dtos.PokemonNameEntry;
import com.pokemon.pokidex.Dtos.upstream.PokemonRecord;
import com.pokemon.pokidex.Dtos.upstream.SpeciesRecord;
import java.util.List;

public interface PokeApiClient {

    /** All Pokémon names with ids, sorted by id. */
    List<PokemonNameEntry> fetchNameIndex();

    /** @param name normalized lowercase slug */
    PokemonRecord fetchPokemon(String name);

    /** @param name species slug (may differ from the Pokémon slug for alternate forms) */
    SpeciesRecord fetchSpecies(String name);
}