package com.pokemon.pokidex.Dtos;

import java.util.List;

public record PageDto<T>(List<T> items, long total, int limit, int offset) {}