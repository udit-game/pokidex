package com.pokemon.pokidex.Services;

import com.pokemon.pokidex.Dtos.PokemonDetailDto;

public interface PokemonService {
    PokemonDetailDto getPokemon(String idOrName);
}