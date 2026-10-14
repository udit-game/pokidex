package com.pokemon.pokidex.Dtos.upstream;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record NamedRef(String name, String url) {}