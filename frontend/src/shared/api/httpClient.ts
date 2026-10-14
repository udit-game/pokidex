
import { env } from '../../config/env';
import type { ApiResponse } from '../types/api';
import { ApiError } from './ApiError';

type Params = Record<string, string | number | undefined>;

interface GetOptions {
  params?: Params;
  signal?: AbortSignal;
}

function buildUrl(path: string, params?: Params): string {
  const qs = new URLSearchParams();
  Object.entries(params ?? {}).forEach(([key, value]) => {
    if (value !== undefined && value !== '') qs.set(key, String(value));
  });
  const query = qs.toString();
  return `${env.apiBaseUrl}${path}${query ? `?${query}` : ''}`;
}

/** Unwraps ApiResponse<T>; every failure becomes an ApiError. Aborts are rethrown untouched. */
export async function get<T>(path: string, { params, signal }: GetOptions = {}): Promise<T> {
  let res: Response;
  try {
    res = await fetch(buildUrl(path, params), { signal, headers: { Accept: 'application/json' } });
  } catch (err) {
    if (err instanceof DOMException && err.name === 'AbortError') throw err;
    throw new ApiError('Network request failed', 'NETWORK_ERROR');
  }

  let body: ApiResponse<T> | null = null;
  try {
    body = (await res.json()) as ApiResponse<T>;
  } catch {
    // non-JSON body (e.g. a proxy error page): handled below
  }

  if (res.ok && body?.success && body.data !== undefined) return body.data;

  throw new ApiError(
    body?.error?.message ?? `Request failed (HTTP ${res.status})`,
    body?.error?.code ?? `HTTP_${res.status}`,
    res.status,
  );
}