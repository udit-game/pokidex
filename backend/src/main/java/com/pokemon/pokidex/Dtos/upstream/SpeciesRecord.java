package com.pokemon.pokidex.Dtos.upstream;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SpeciesRecord(
        int id,
        String name,
        @JsonProperty("capture_rate") Integer captureRate,
        @JsonProperty("base_happiness") Integer baseHappiness,
        @JsonProperty("gender_rate") Integer genderRate,   // -1 = genderless, else eighths female
        @JsonProperty("is_legendary") boolean legendary,
        @JsonProperty("is_mythical") boolean mythical,
        @JsonProperty("is_baby") boolean baby,
        NamedRef color,
        NamedRef habitat,                                   // can be null
        NamedRef generation,
        @JsonProperty("growth_rate") NamedRef growthRate,
        @JsonProperty("egg_groups") List<NamedRef> eggGroups,
        @JsonProperty("flavor_text_entries") List<FlavorText> flavorTextEntries,
        List<Genus> genera
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record FlavorText(@JsonProperty("flavor_text") String flavorText, NamedRef language) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Genus(String genus, NamedRef language) {}
}