import { useCallback, useMemo, useState, type ReactNode } from 'react';
import { useNavigate } from 'react-router-dom';
import { useDebounce } from '../../../shared/hooks/useDebounce';
import { useAsync } from '../../../shared/hooks/useAsync';
import { SearchContext, type SearchContextValue } from './SearchContext';
import { searchPokemon } from '../../pokemon/api/pokemonApi';
import { SEARCH_DEBOUNCE_MS, SEARCH_RESULT_LIMIT } from '../../../config/constants';

export function SearchProvider({ children }: { children: ReactNode }) {
  const navigate = useNavigate();
  const [query, setQueryState] = useState('');
  const [isOpen, setIsOpen] = useState(false);
  const [activeIndex, setActiveIndex] = useState(-1);

  const trimmed = query.trim();
  const debounced = useDebounce(trimmed, SEARCH_DEBOUNCE_MS);

  const fetcher = useMemo(
    () =>
      debounced
        ? (signal: AbortSignal) => searchPokemon({ q: debounced, limit: SEARCH_RESULT_LIMIT }, signal)
        : null,
    [debounced],
  );
  const { data, status, error, reload } = useAsync(fetcher);

  const results = useMemo(
    () => (trimmed && status !== 'error' ? (data?.items ?? []) : []),
    [trimmed, status, data],
  );
  const isSearching = trimmed !== '' && (trimmed !== debounced || status === 'loading');

  const setQuery = useCallback((value: string) => {
    setQueryState(value);
    setActiveIndex(-1);
    setIsOpen(true);
  }, []);

  const open = useCallback(() => setIsOpen(true), []);

  const close = useCallback(() => {
    setIsOpen(false);
    setActiveIndex(-1);
  }, []);

  const moveActive = useCallback(
    (delta: 1 | -1) => {
      setIsOpen(true);
      const n = results.length;
      if (n === 0) return;
      setActiveIndex((i) => (i === -1 ? (delta === 1 ? 0 : n - 1) : (i + delta + n) % n));
    },
    [results.length],
  );

  const select = useCallback(
    (name: string) => {
      setQueryState('');
      setActiveIndex(-1);
      setIsOpen(false);
      navigate(`/pokemon/${encodeURIComponent(name)}`);
    },
    [navigate],
  );

  const submit = useCallback(() => {
    if (!trimmed) return;
    const picked = results[activeIndex] ?? (isSearching ? undefined : results[0]);
    if (picked) select(picked.name);
    else if (isSearching || status === 'error') select(trimmed); // let the detail page resolve exact names
    // otherwise: search finished with zero matches, so the dropdown already says so
  }, [trimmed, results, activeIndex, isSearching, status, select]);

  const value = useMemo<SearchContextValue>(
    () => ({
      query,
      setQuery,
      results,
      total: data?.total ?? 0,
      status,
      error,
      isSearching,
      isOpen,
      open,
      close,
      activeIndex,
      setActiveIndex,
      moveActive,
      select,
      submit,
      retry: reload,
    }),
    [query, setQuery, results, data, status, error, isSearching, isOpen, open, close, activeIndex, moveActive, select, submit, reload],
  );

  return <SearchContext.Provider value={value}>{children}</SearchContext.Provider>;
}