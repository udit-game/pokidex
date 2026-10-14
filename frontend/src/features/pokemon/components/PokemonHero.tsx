import { useState } from 'react';

import { useLoadedPokemon } from '../context/PokemonContext';
import { Artwork } from './Artwork';
import { TypeBadge } from './TypeBadge';
import { Button } from '../../../shared/components';
import { typeTint } from '../../../shared/theme/typeColors';
import { formatId } from '../../../shared/utils/format';

export function PokemonHero() {
  const p = useLoadedPokemon();
  const [shiny, setShiny] = useState(false);

  const primaryType = p.types[0] ?? 'normal';
  const src = shiny ? p.shinyUrl : p.artworkUrl;

  const flags: string[] = [];
  if (p.legendary) flags.push('Legendary');
  if (p.mythical) flags.push('Mythical');
  if (p.baby) flags.push('Baby');

  return (
    <section
      className="overflow-hidden rounded-3xl border border-border"
      style={{ background: `linear-gradient(135deg, ${typeTint(primaryType, 35)}, var(--surface) 70%)` }}
    >
      <div className="grid items-center gap-6 p-6 md:grid-cols-2 md:p-10">
        <div>
          <p className="font-mono text-sm text-muted">{formatId(p.id)}</p>
          <h1 className="text-4xl font-extrabold tracking-tight md:text-5xl">{p.displayName}</h1>
          {p.genus && <p className="mt-1 text-muted">{p.genus}</p>}

          <div className="mt-4 flex flex-wrap gap-2">
            {p.types.map((t) => (
              <TypeBadge key={t} type={t} />
            ))}
          </div>

          {flags.length > 0 && (
            <div className="mt-3 flex flex-wrap gap-2">
              {flags.map((f) => (
                <span key={f} className="rounded-full border border-border bg-surface px-3 py-1 text-xs font-medium">
                  {f}
                </span>
              ))}
            </div>
          )}

          <div className="mt-6 flex flex-wrap gap-3">
            <Button variant="ghost" aria-pressed={shiny} onClick={() => setShiny((s) => !s)}>
              <span aria-hidden>✨</span> {shiny ? 'Show normal' : 'Show shiny'}
            </Button>
          </div>
        </div>

        <div className="flex justify-center">
          <Artwork key={src} src={src} alt={`${p.displayName} official artwork`} />
        </div>
      </div>
    </section>
  );
}