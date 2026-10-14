export interface ApiErrorBody {
  message: string;
  code: string;
}

/** Mirrors the backend's single response envelope. */
export interface ApiResponse<T> {
  success: boolean;
  data?: T;
  error?: ApiErrorBody;
}

export interface Page<T> {
  items: T[];
  total: number;
  limit: number;
  offset: number;
}