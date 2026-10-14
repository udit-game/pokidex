package com.pokemon.pokidex.Exceptions;

import org.springframework.http.HttpStatus;

public class PokemonNotFoundException extends PokedexException {
    public PokemonNotFoundException(String identifier) {
        super(HttpStatus.NOT_FOUND, "POKEMON_NOT_FOUND", "No Pokémon matches '" + identifier + "'");
    }
}