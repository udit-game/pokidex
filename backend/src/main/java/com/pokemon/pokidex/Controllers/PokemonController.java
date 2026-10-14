package com.pokemon.pokidex.Controllers;

import com.pokemon.pokidex.Dtos.ApiResponse;
import com.pokemon.pokidex.Dtos.PageDto;
import com.pokemon.pokidex.Dtos.PokemonDetailDto;
import com.pokemon.pokidex.Dtos.PokemonSummaryDto;
import com.pokemon.pokidex.Services.PokemonSearchService;
import com.pokemon.pokidex.Services.PokemonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.time.Duration;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Pokémon")
public class PokemonController {

    private static final CacheControl SEARCH_CACHE = CacheControl.maxAge(Duration.ofMinutes(1)).cachePublic();
    private static final CacheControl DETAIL_CACHE = CacheControl.maxAge(Duration.ofHours(1)).cachePublic();

    private final PokemonSearchService searchService;
    private final PokemonService pokemonService;

    public PokemonController(PokemonSearchService searchService, PokemonService pokemonService) {
        this.searchService = searchService;
        this.pokemonService = pokemonService;
    }

    @Operation(summary = "Search Pokémon by name (ranked: exact, prefix, word-prefix, substring, fuzzy) or id")
    @GetMapping
    public ResponseEntity<ApiResponse<PageDto<PokemonSummaryDto>>> search(
            @RequestParam(name = "q", defaultValue = "")
            @Size(max = 50, message = "q must be at most 50 characters") String q,
            @RequestParam(name = "limit", defaultValue = "10")
            @Min(value = 1, message = "limit must be at least 1") int limit,
            @RequestParam(name = "offset", defaultValue = "0")
            @Min(value = 0, message = "offset must not be negative") int offset) {
        PageDto<PokemonSummaryDto> page = searchService.search(q, limit, offset);
        return ResponseEntity.ok().cacheControl(SEARCH_CACHE).body(ApiResponse.ok(page));
    }

    @Operation(summary = "Get full details of one Pokémon by name or numeric id")
    @GetMapping("/{idOrName}")
    public ResponseEntity<ApiResponse<PokemonDetailDto>> getPokemon(@PathVariable String idOrName) {
        PokemonDetailDto dto = pokemonService.getPokemon(idOrName);
        return ResponseEntity.ok().cacheControl(DETAIL_CACHE).body(ApiResponse.ok(dto));
    }
}