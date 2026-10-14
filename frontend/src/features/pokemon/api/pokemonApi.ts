
import { get } from '../../../shared/api/httpClient';
import type { Page } from '../../../shared/types/api';
import type { PokemonDetail, PokemonSummary } from '../types';

type SearchArgs = { q?: string; limit?: number; offset?: number };

export function searchPokemon(args: SearchArgs, signal?: AbortSignal) {
  return get<Page<PokemonSummary>>('/pokemon', { params: args, signal });
}

export function getPokemon(idOrName: string, signal?: AbortSignal) {
  return get<PokemonDetail>(`/pokemon/${encodeURIComponent(idOrName)}`, { signal });
}