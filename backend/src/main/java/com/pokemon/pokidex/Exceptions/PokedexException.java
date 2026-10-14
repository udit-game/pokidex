package com.pokemon.pokidex.Exceptions;

import org.springframework.http.HttpStatus;

public abstract class PokedexException extends RuntimeException {
    private final HttpStatus status;
    private final String code;

    protected PokedexException(HttpStatus status, String code, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }

    protected PokedexException(HttpStatus status, String code, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.code = code;
    }

    public HttpStatus getStatus() { return status; }
    public String getCode() { return code; }
}