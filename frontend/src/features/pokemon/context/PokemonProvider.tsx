import { useCallback, useMemo, type ReactNode } from 'react';
import { getPokemon } from '../api/pokemonApi';
import { PokemonContext } from './PokemonContext';
import { useAsync } from '../../../shared/hooks';

interface Props {
  name: string;
  children: ReactNode;
}

/** Mount with key={name} so each Pokémon starts from a clean state. */
export function PokemonProvider({ name, children }: Props) {
  const fetcher = useCallback((signal: AbortSignal) => getPokemon(name, signal), [name]);
  const { data, status, error, reload } = useAsync(fetcher);

  const value = useMemo(
    () => ({ status, pokemon: data, error, reload }),
    [status, data, error, reload],
  );

  return <PokemonContext.Provider value={value}>{children}</PokemonContext.Provider>;
}