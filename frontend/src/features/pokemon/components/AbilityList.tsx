
import { Card } from '../../../shared/components';
import { titleCase } from '../../../shared/utils/format';
import { useLoadedPokemon } from '../context/PokemonContext';

export function AbilityList() {
  const { abilities } = useLoadedPokemon();
  if (abilities.length === 0) return null;

  return (
    <Card title="Abilities">
      <ul className="flex flex-wrap gap-2">
        {abilities.map((a) => (
          <li key={a.name} className="rounded-lg border border-border px-3 py-1.5 text-sm font-medium">
            {titleCase(a.name)}
            {a.hidden && <span className="ml-2 text-xs font-normal text-muted">Hidden</span>}
          </li>
        ))}
      </ul>
    </Card>
  );
}