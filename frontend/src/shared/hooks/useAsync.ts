import { useCallback, useEffect, useState } from 'react';
import { toApiError, type ApiError } from '../api/ApiError';

export type AsyncStatus = 'idle' | 'loading' | 'success' | 'error';

interface AsyncState<T> {
  status: AsyncStatus;
  data: T | null;
  error: ApiError | null;
}

/**
 * Runs `fetcher` whenever its identity changes (wrap it in useCallback/useMemo).
 * - null fetcher => idle
 * - the previous request is aborted when a new one starts (no race conditions)
 * - `reload()` re-runs the same fetcher
 */
export function useAsync<T>(fetcher: ((signal: AbortSignal) => Promise<T>) | null) {
  const [state, setState] = useState<AsyncState<T>>({
    status: fetcher ? 'loading' : 'idle',
    data: null,
    error: null,
  });
  const [attempt, setAttempt] = useState(0);

  useEffect(() => {
    if (!fetcher) {
      setState({ status: 'idle', data: null, error: null });
      return;
    }

    const controller = new AbortController();
    setState((prev) => ({ ...prev, status: 'loading', error: null }));

    fetcher(controller.signal)
      .then((data) => {
        if (!controller.signal.aborted) setState({ status: 'success', data, error: null });
      })
      .catch((err: unknown) => {
        if (controller.signal.aborted) return;
        setState((prev) => ({ status: 'error', data: prev.data, error: toApiError(err) }));
      });

    return () => controller.abort();
  }, [fetcher, attempt]);

  const reload = useCallback(() => setAttempt((n) => n + 1), []);

  return { ...state, reload };
}