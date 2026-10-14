import { Link } from 'react-router-dom';

import type { PokemonSummary } from '../types';
import { formatId } from '../../../shared/utils/format';

export function PokemonCard({ item }: { item: PokemonSummary }) {
  return (
    <Link
      to={`/pokemon/${encodeURIComponent(item.name)}`}
      className="group flex flex-col items-center gap-1 rounded-2xl border border-border bg-surface p-4 transition hover:-translate-y-0.5 hover:border-brand hover:shadow-lg"
    >
      <img
        src={item.spriteUrl}
        alt=""
        loading="lazy"
        width={96}
        height={96}
        className="h-24 w-24 [image-rendering:pixelated] transition group-hover:scale-110"
      />
      <span className="font-mono text-xs text-muted">{formatId(item.id)}</span>
      <span className="text-center font-semibold">{item.displayName}</span>
    </Link>
  );
}