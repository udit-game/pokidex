
import { friendlyMessage } from '../../../shared/api/errorMessages';
import { Button, Spinner } from '../../../shared/components';
import { LISTBOX_ID } from '../context/constants';
import { useSearch } from '../context/SearchContext';
import { SearchResultItem } from './SearchResultItem';

export function SearchDropdown() {
  const { query, results, total, error, isSearching, retry } = useSearch();

  let content;
  if (error) {
    content = (
      <div role="alert" className="space-y-3 p-4 text-sm">
        <p className="text-muted">{friendlyMessage(error)}</p>
        <Button variant="ghost" onClick={retry}>Retry</Button>
      </div>
    );
  } else if (results.length > 0) {
    content = (
      <>
        <ul id={LISTBOX_ID} role="listbox" aria-label="Search results">
          {results.map((item, index) => (
            <SearchResultItem key={item.id} item={item} index={index} />
          ))}
        </ul>
        <div className="flex items-center justify-between border-t border-border px-4 py-2 text-xs text-muted">
          <span>Showing {results.length} of {total}</span>
          {isSearching && <Spinner label="Updating results" />}
        </div>
      </>
    );
  } else if (isSearching) {
    content = (
      <div className="flex items-center gap-3 p-4 text-sm text-muted">
        <Spinner label="Searching" /> Searching…
      </div>
    );
  } else {
    content = <p className="p-4 text-sm text-muted">No Pokémon match “{query.trim()}”.</p>;
  }

  return (
    <div className="absolute left-0 right-0 top-full z-40 mt-2 animate-fade-in overflow-hidden rounded-2xl border border-border bg-surface shadow-xl">
      {content}
    </div>
  );
}