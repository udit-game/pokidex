export const env = {
  /** Same-origin by default: Vite proxy in dev, nginx in Docker. */
  apiBaseUrl: (import.meta.env.VITE_API_BASE_URL as string | undefined) ?? '/api/v1',
} as const;