package com.pokemon.pokidex.Services;

import com.pokemon.pokidex.Dtos.PageDto;
import com.pokemon.pokidex.Dtos.PokemonSummaryDto;

public interface PokemonSearchService {
    PageDto<PokemonSummaryDto> search(String query, int limit, int offset);
}