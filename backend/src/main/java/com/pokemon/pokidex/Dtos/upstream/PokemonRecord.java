package com.pokemon.pokidex.Dtos.upstream;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PokemonRecord(
        int id,
        String name,
        int height,   // decimetres
        int weight,   // hectograms
        @JsonProperty("base_experience") Integer baseExperience,
        NamedRef species,
        List<TypeSlot> types,
        List<StatSlot> stats,
        List<AbilitySlot> abilities,
        Sprites sprites,
        Cries cries
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record TypeSlot(int slot, NamedRef type) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record StatSlot(@JsonProperty("base_stat") int baseStat, NamedRef stat) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record AbilitySlot(@JsonProperty("is_hidden") boolean hidden, int slot, NamedRef ability) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Sprites(@JsonProperty("front_default") String frontDefault, Other other) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Other(@JsonProperty("official-artwork") Artwork officialArtwork) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Artwork(
            @JsonProperty("front_default") String frontDefault,
            @JsonProperty("front_shiny") String frontShiny) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Cries(String latest) {}
}