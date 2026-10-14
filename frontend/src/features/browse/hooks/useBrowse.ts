import { useCallback, useEffect, useState } from 'react';
import { BROWSE_PAGE_SIZE } from '../../../config/constants';
import type { PokemonSummary } from '../../pokemon/types';
import { searchPokemon } from '../../pokemon/api/pokemonApi';
import { useAsync } from '../../../shared/hooks';


/** Offset-paginated browse list: pages accumulate, duplicates are ignored. */
export function useBrowse(pageSize = BROWSE_PAGE_SIZE) {
  const [offset, setOffset] = useState(0);
  const [items, setItems] = useState<PokemonSummary[]>([]);

  const fetcher = useCallback(
    (signal: AbortSignal) => searchPokemon({ limit: pageSize, offset }, signal),
    [pageSize, offset],
  );
  const { data, status, error, reload } = useAsync(fetcher);

  useEffect(() => {
    if (!data) return;
    setItems((prev) => {
      const seen = new Set(prev.map((i) => i.id));
      return [...prev, ...data.items.filter((i) => !seen.has(i.id))];
    });
  }, [data]);

  const total = data?.total ?? 0;
  const isEmpty = status === 'success' && total === 0;
  const isInitialLoading = items.length === 0 && !error && !isEmpty;
  const hasMore = items.length < total;
  const loadMore = useCallback(() => setOffset((o) => o + pageSize), [pageSize]);

  return { items, status, error, isEmpty, isInitialLoading, hasMore, loadMore, reload };
}