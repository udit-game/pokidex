
import type { ApiError } from '../../../shared/api/ApiError';
import type { AsyncStatus } from '../../../shared/hooks';
import { createStrictContext } from '../../../shared/lib/createStrictContext';
import type { PokemonDetail } from '../types';

export interface PokemonContextValue {
  status: AsyncStatus;
  pokemon: PokemonDetail | null;
  error: ApiError | null;
  reload: () => void;
}

export const [PokemonContext, usePokemon] = createStrictContext<PokemonContextValue>('Pokemon');

/** For components that only render after a successful load: returns the data, never null. */
export function useLoadedPokemon(): PokemonDetail {
  const { pokemon } = usePokemon();
  if (!pokemon) throw new Error('useLoadedPokemon used before the Pokémon finished loading');
  return pokemon;
}