
import { BROWSE_PAGE_SIZE } from '../../../config/constants';
import { friendlyMessage } from '../../../shared/api/errorMessages';
import { Button, EmptyState, ErrorState, Skeleton, Spinner } from '../../../shared/components';
import { PokemonCard } from '../../pokemon/components/PokemonCard';
import { useBrowse } from '../hooks/useBrowse';

export function BrowseGrid() {
  const { items, status, error, isEmpty, isInitialLoading, hasMore, loadMore, reload } = useBrowse();

  if (error && items.length === 0) {
    return <ErrorState message={friendlyMessage(error)} onRetry={reload} />;
  }
  if (isEmpty) {
    return <EmptyState title="No Pokémon available" message="The Pokémon index is empty. Try again shortly." />;
  }

  const loading = status === 'loading';

  return (
    <section aria-labelledby="browse-title">
      <h2 id="browse-title" className="mb-4 text-xl font-bold">
        Browse the Pokédex
      </h2>

      <ul className="grid grid-cols-2 gap-3 sm:grid-cols-3 md:grid-cols-4">
        {isInitialLoading
          ? Array.from({ length: BROWSE_PAGE_SIZE }, (_, i) => (
              <li key={i}>
                <Skeleton className="h-36" />
              </li>
            ))
          : items.map((item) => (
              <li key={item.id} className="animate-fade-in">
                <PokemonCard item={item} />
              </li>
            ))}
      </ul>

      {error && (
        <p role="alert" className="mt-4 text-center text-sm text-muted">
          {friendlyMessage(error)}
        </p>
      )}

      {hasMore && !isInitialLoading && (
        <div className="mt-6 flex justify-center">
          <Button variant="ghost" onClick={error ? reload : loadMore} disabled={loading}>
            {loading && <Spinner label="Loading more" />}
            {error ? 'Retry' : 'Load more'}
          </Button>
        </div>
      )}
    </section>
  );
}