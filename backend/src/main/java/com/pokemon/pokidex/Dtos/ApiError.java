package com.pokemon.pokidex.Dtos;


public record ApiError(
        String message,
        String code
) {}
