import type { KeyboardEvent } from 'react';

import { useSearch } from '../context/SearchContext';
import { SearchDropdown } from './SearchDropdown';
import { useClickOutside } from '../../../shared/hooks';
import { LISTBOX_ID, optionId } from '../context/constants';
import { SEARCH_MAX_LENGTH } from '../../../config/constants';

const SIZES = {
  md: 'py-2.5 text-sm',
  lg: 'py-4 text-base md:text-lg',
} as const;

export function SearchBox({ size = 'md' }: { size?: keyof typeof SIZES }) {
  const { query, setQuery, isOpen, open, close, activeIndex, moveActive, submit } = useSearch();
  const containerRef = useClickOutside<HTMLDivElement>(close);

  const onKeyDown = (event: KeyboardEvent<HTMLInputElement>) => {
    switch (event.key) {
      case 'ArrowDown':
        event.preventDefault();
        moveActive(1);
        break;
      case 'ArrowUp':
        event.preventDefault();
        moveActive(-1);
        break;
      case 'Enter':
        event.preventDefault();
        submit();
        break;
      case 'Escape':
        close();
        break;
    }
  };

  const showDropdown = isOpen && query.trim().length > 0;

  return (
    <div ref={containerRef} className="relative w-full">
      <svg
        aria-hidden
        viewBox="0 0 24 24"
        fill="none"
        stroke="currentColor"
        strokeWidth="2"
        strokeLinecap="round"
        className="pointer-events-none absolute left-4 top-1/2 h-5 w-5 -translate-y-1/2 text-muted"
      >
        <circle cx="11" cy="11" r="7" />
        <path d="m20 20-3.5-3.5" />
      </svg>

      <input
        type="text"
        role="combobox"
        aria-label="Search Pokémon"
        aria-autocomplete="list"
        aria-expanded={showDropdown}
        aria-controls={LISTBOX_ID}
        aria-activedescendant={activeIndex >= 0 ? optionId(activeIndex) : undefined}
        autoComplete="off"
        spellCheck={false}
        maxLength={SEARCH_MAX_LENGTH}
        placeholder="Search by name or number…"
        value={query}
        onChange={(e) => setQuery(e.target.value)}
        onFocus={open}
        onKeyDown={onKeyDown}
        className={`w-full rounded-2xl border border-border bg-surface pl-12 pr-4 text-fg shadow-sm placeholder:text-muted focus:border-brand ${SIZES[size]}`}
      />

      {showDropdown && <SearchDropdown />}
    </div>
  );
}