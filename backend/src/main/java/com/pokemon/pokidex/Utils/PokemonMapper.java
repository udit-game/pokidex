package com.pokemon.pokidex.Utils;

import com.pokemon.pokidex.Dtos.PokemonDetailDto;
import com.pokemon.pokidex.Dtos.PokemonDetailDto.AbilityDto;
import com.pokemon.pokidex.Dtos.PokemonDetailDto.StatDto;
import com.pokemon.pokidex.Dtos.upstream.NamedRef;
import com.pokemon.pokidex.Dtos.upstream.PokemonRecord;
import com.pokemon.pokidex.Dtos.upstream.SpeciesRecord;
import java.util.Comparator;
import java.util.List;

public final class PokemonMapper {

    private static final String ENGLISH = "en";

    private PokemonMapper() {}

    public static PokemonDetailDto toDetail(PokemonRecord p, SpeciesRecord s) {
        String artwork = null;
        String shiny = null;
        if (p.sprites() != null && p.sprites().other() != null && p.sprites().other().officialArtwork() != null) {
            artwork = p.sprites().other().officialArtwork().frontDefault();
            shiny = p.sprites().other().officialArtwork().frontShiny();
        }
        if (artwork == null) artwork = PokeUrlUtils.artworkUrl(p.id());
        if (shiny == null) shiny = PokeUrlUtils.shinyArtworkUrl(p.id());

        return new PokemonDetailDto(
                p.id(),
                p.name(),
                NameNormalizer.displayName(p.name()),
                englishGenus(s),
                nullSafe(p.types()).stream()
                        .sorted(Comparator.comparingInt(PokemonRecord.TypeSlot::slot))
                        .map(t -> t.type().name()).toList(),
                p.height() / 10.0,
                p.weight() / 10.0,
                p.baseExperience(),
                artwork,
                shiny,
                p.cries() != null ? p.cries().latest() : null,
                nullSafe(p.stats()).stream()
                        .map(st -> new StatDto(st.stat().name(), st.baseStat())).toList(),
                nullSafe(p.abilities()).stream()
                        .sorted(Comparator.comparingInt(PokemonRecord.AbilitySlot::slot))
                        .map(a -> new AbilityDto(a.ability().name(), a.hidden())).toList(),
                englishFlavorText(s),
                s.captureRate(),
                s.baseHappiness(),
                femaleRatio(s.genderRate()),
                nameOf(s.color()),
                nameOf(s.habitat()),
                nameOf(s.generation()),
                nameOf(s.growthRate()),
                nullSafe(s.eggGroups()).stream().map(NamedRef::name).toList(),
                s.legendary(),
                s.mythical(),
                s.baby()
        );
    }

    private static Double femaleRatio(Integer genderRate) {
        return (genderRate == null || genderRate < 0) ? null : genderRate / 8.0;
    }

    private static String nameOf(NamedRef ref) {
        return ref == null ? null : ref.name();
    }

    /** Entries are ordered oldest to newest, so take the last English one. */
    private static String englishFlavorText(SpeciesRecord s) {
        List<SpeciesRecord.FlavorText> list = nullSafe(s.flavorTextEntries());
        for (int i = list.size() - 1; i >= 0; i--) {
            SpeciesRecord.FlavorText ft = list.get(i);
            if (ft.language() != null && ENGLISH.equals(ft.language().name())) {
                return TextUtils.cleanFlavorText(ft.flavorText());
            }
        }
        return null;
    }

    private static String englishGenus(SpeciesRecord s) {
        return nullSafe(s.genera()).stream()
                .filter(g -> g.language() != null && ENGLISH.equals(g.language().name()))
                .map(SpeciesRecord.Genus::genus)
                .findFirst().orElse(null);
    }

    private static <T> List<T> nullSafe(List<T> list) {
        return list == null ? List.of() : list;
    }
}