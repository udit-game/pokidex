export const titleCase = (slug: string): string =>
  slug
    .split('-')
    .filter(Boolean)
    .map((part) => part.charAt(0).toUpperCase() + part.slice(1))
    .join(' ');

export const formatId = (id: number): string => `#${String(id).padStart(4, '0')}`;

const STAT_LABELS: Record<string, string> = {
  hp: 'HP',
  attack: 'Attack',
  defense: 'Defense',
  'special-attack': 'Sp. Atk',
  'special-defense': 'Sp. Def',
  speed: 'Speed',
};

export const formatStat = (name: string): string => STAT_LABELS[name] ?? titleCase(name);

/** "generation-iii" -> "Gen III" */
export const formatGeneration = (slug: string): string =>
  `Gen ${slug.replace('generation-', '').toUpperCase()}`;

/** femaleRatio is 0..1; absent means genderless. */
export function formatGender(femaleRatio?: number | null): string {
  if (femaleRatio == null) return 'Genderless';
  const female = Number((femaleRatio * 100).toFixed(1));
  const male = Number((100 - female).toFixed(1));
  return `${male}% ♂ · ${female}% ♀`;
}