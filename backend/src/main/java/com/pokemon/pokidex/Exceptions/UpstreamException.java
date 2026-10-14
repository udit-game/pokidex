package com.pokemon.pokidex.Exceptions;

import org.springframework.http.HttpStatus;

public class UpstreamException extends PokedexException {
    public UpstreamException(String message, Throwable cause) {
        super(HttpStatus.BAD_GATEWAY, "UPSTREAM_ERROR", message, cause);
    }

    public UpstreamException(String message) {
        super(HttpStatus.BAD_GATEWAY, "UPSTREAM_ERROR", message);
    }
}