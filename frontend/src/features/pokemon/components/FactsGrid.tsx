import { Card } from '../../../shared/components';
import { formatGender, formatGeneration, titleCase } from '../../../shared/utils/format';
import { useLoadedPokemon } from '../context/PokemonContext';

type Fact = [label: string, value: string | number | null | undefined];

export function FactsGrid() {
  const p = useLoadedPokemon();

  const facts: Fact[] = [
    ['Height', `${p.heightM.toFixed(1)} m`],
    ['Weight', `${p.weightKg.toFixed(1)} kg`],
    ['Base EXP', p.baseExperience],
    ['Capture rate', p.captureRate],
    ['Base happiness', p.baseHappiness],
    ['Gender', formatGender(p.femaleRatio)],
    ['Habitat', p.habitat && titleCase(p.habitat)],
    ['Generation', p.generation && formatGeneration(p.generation)],
    ['Growth rate', p.growthRate && titleCase(p.growthRate)],
    ['Egg groups', p.eggGroups.length > 0 ? p.eggGroups.map(titleCase).join(', ') : undefined],
    ['Colour', p.color && titleCase(p.color)],
  ];

  const visible = facts.filter(([, value]) => value !== undefined && value !== null && value !== '');

  return (
    <Card title="Details">
      <dl className="grid grid-cols-2 gap-3 sm:grid-cols-3 md:grid-cols-4">
        {visible.map(([label, value]) => (
          <div key={label} className="rounded-xl bg-bg px-4 py-3">
            <dt className="text-xs text-muted">{label}</dt>
            <dd className="mt-0.5 font-semibold">{value}</dd>
          </div>
        ))}
      </dl>
    </Card>
  );
}