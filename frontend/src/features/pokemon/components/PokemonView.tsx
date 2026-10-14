import { Link } from 'react-router-dom';
import { usePokemon } from '../context/PokemonContext';
import { AbilityList } from './AbilityList';
import { FactsGrid } from './FactsGrid';
import { FlavorText } from './FlavorText';
import { PokemonHero } from './PokemonHero';
import { PokemonSkeleton } from './PokemonSkeleton';
import { StatBars } from './StatBars';
import { useDocumentTitle } from '../../../shared/hooks';
import { EmptyState, ErrorState } from '../../../shared/components';
import { friendlyMessage } from '../../../shared/api/errorMessages';

export function PokemonView() {
  const { status, pokemon, error, reload } = usePokemon();
  useDocumentTitle(pokemon ? `${pokemon.displayName} · Pokidex` : 'Pokidex');

  if (status === 'error' && error) {
    if (error.isNotFound) {
      return (
        <EmptyState icon="🕵️" title="Pokémon not found" message={error.message}>
          <Link to="/" className="font-medium text-brand hover:underline">
            Back to search
          </Link>
        </EmptyState>
      );
    }
    return <ErrorState message={friendlyMessage(error)} onRetry={reload} />;
  }

  if (!pokemon) return <PokemonSkeleton />;

  return (
    <article className="animate-fade-in space-y-6">
      <Link to="/" className="inline-block text-sm text-muted hover:text-fg">
        ← Back
      </Link>
      <PokemonHero />
      <div className="grid gap-6 md:grid-cols-2">
        <StatBars />
        <div className="space-y-6">
          <FlavorText />
          <AbilityList />
        </div>
      </div>
      <FactsGrid />
    </article>
  );
}