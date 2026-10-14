import type { ApiError } from './ApiError';

const MESSAGES: Record<string, string> = {
  NETWORK_ERROR: "We can't reach the server. Check your connection and try again.",
  HTTP_502: 'The server is starting up or unreachable. Try again in a moment.',
  HTTP_503: 'The server is starting up or unreachable. Try again in a moment.',
  HTTP_504: 'The server took too long to respond. Try again.',
  UPSTREAM_UNAVAILABLE: 'The Pokémon data source is not responding. Please retry in a moment.',
  UPSTREAM_ERROR: 'The Pokémon data source returned an error. Please retry.',
  INTERNAL_ERROR: 'Something went wrong on our side. Please retry.',
};

/** Known codes get friendly copy; everything else (e.g. validation) shows the server's message. */
export function friendlyMessage(error: ApiError): string {
  return MESSAGES[error.code] ?? error.message;
}