import type { ApiError } from "../../../shared/api/ApiError";
import type { AsyncStatus } from "../../../shared/hooks";
import { createStrictContext } from "../../../shared/lib/createStrictContext";
import type { PokemonSummary } from "../../pokemon/types";


export interface SearchContextValue {
  query: string;
  setQuery: (query: string) => void;
  results: PokemonSummary[];
  total: number;
  status: AsyncStatus;
  error: ApiError | null;
  /** true while the debounce is pending or a request is in flight */
  isSearching: boolean;
  isOpen: boolean;
  open: () => void;
  close: () => void;
  activeIndex: number;
  setActiveIndex: (index: number) => void;
  moveActive: (delta: 1 | -1) => void;
  select: (name: string) => void;
  submit: () => void;
  retry: () => void;
}

export const [SearchContext, useSearch] = createStrictContext<SearchContextValue>('Search');