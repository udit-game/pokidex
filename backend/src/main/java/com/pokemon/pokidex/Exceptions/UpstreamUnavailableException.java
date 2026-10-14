package com.pokemon.pokidex.Exceptions;

import org.springframework.http.HttpStatus;

public class UpstreamUnavailableException extends PokedexException {
    public UpstreamUnavailableException(String message, Throwable cause) {
        super(HttpStatus.SERVICE_UNAVAILABLE, "UPSTREAM_UNAVAILABLE", message, cause);
    }
}