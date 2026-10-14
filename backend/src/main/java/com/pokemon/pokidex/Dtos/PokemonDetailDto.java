package com.pokemon.pokidex.Dtos;

import java.util.List;

public record PokemonDetailDto(
        int id,
        String name,
        String displayName,
        String genus,
        List<String> types,
        double heightM,
        double weightKg,
        Integer baseExperience,
        String artworkUrl,
        String shinyUrl,
        String cryUrl,
        List<StatDto> stats,
        List<AbilityDto> abilities,
        String flavorText,
        Integer captureRate,
        Integer baseHappiness,
        Double femaleRatio,          // null = genderless
        String color,
        String habitat,
        String generation,
        String growthRate,
        List<String> eggGroups,
        boolean legendary,
        boolean mythical,
        boolean baby
) {
    public record StatDto(String name, int base) {}

    public record AbilityDto(String name, boolean hidden) {}
}