package com.pokemon.pokidex.Dtos.upstream;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PokemonListRecord(int count, List<NamedRef> results) {}