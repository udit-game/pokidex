import { Card } from '../../../shared/components';
import { useLoadedPokemon } from '../context/PokemonContext';

export function FlavorText() {
  const { flavorText } = useLoadedPokemon();
  if (!flavorText) return null;

  return (
    <Card title="Pokédex entry">
      <blockquote className="italic leading-relaxed">{flavorText}</blockquote>
    </Card>
  );
}