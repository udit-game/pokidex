export class ApiError extends Error {
  readonly code: string;
  readonly status: number;

  constructor(message: string, code: string, status = 0) {
    super(message);
    this.name = 'ApiError';
    this.code = code;
    this.status = status;
  }

  get isNotFound(): boolean {
    return this.code === 'POKEMON_NOT_FOUND' || this.status === 404;
  }
}

export function toApiError(err: unknown): ApiError {
  if (err instanceof ApiError) return err;
  return new ApiError(err instanceof Error ? err.message : 'Unknown error', 'UNKNOWN');
}