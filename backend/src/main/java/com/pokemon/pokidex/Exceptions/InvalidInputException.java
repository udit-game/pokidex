package com.pokemon.pokidex.Exceptions;

import org.springframework.http.HttpStatus;

public class InvalidInputException extends PokedexException {
    public InvalidInputException(String message) {
        super(HttpStatus.BAD_REQUEST, "INVALID_INPUT", message);
    }
}