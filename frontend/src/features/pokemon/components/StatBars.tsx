import { useEffect, useState } from 'react';

import { useLoadedPokemon } from '../context/PokemonContext';
import { typeColor } from '../../../shared/theme/typeColors';
import { MAX_BASE_STAT } from '../../../config/constants';
import { Card } from '../../../shared/components';
import { formatStat } from '../../../shared/utils/format';

export function StatBars() {
  const { stats, types } = useLoadedPokemon();
  const [ready, setReady] = useState(false);

  // mount at 0% then grow, so the CSS transition animates
  useEffect(() => {
    const id = setTimeout(() => setReady(true), 50);
    return () => clearTimeout(id);
  }, []);

  const color = typeColor(types[0] ?? 'normal');
  const total = stats.reduce((sum, s) => sum + s.base, 0);

  return (
    <Card title="Base stats">
      <ul className="space-y-3">
        {stats.map((s) => (
          <li key={s.name} className="grid grid-cols-[5rem_2.5rem_1fr] items-center gap-3 text-sm">
            <span className="text-muted">{formatStat(s.name)}</span>
            <span className="font-mono font-semibold">{s.base}</span>
            <div
              role="progressbar"
              aria-label={formatStat(s.name)}
              aria-valuemin={0}
              aria-valuemax={MAX_BASE_STAT}
              aria-valuenow={s.base}
              className="h-2 overflow-hidden rounded-full bg-border"
            >
              <div
                className="h-full rounded-full transition-[width] duration-700 ease-out"
                style={{
                  width: `${ready ? Math.min((s.base / MAX_BASE_STAT) * 100, 100) : 0}%`,
                  backgroundColor: color,
                }}
              />
            </div>
          </li>
        ))}
      </ul>
      <p className="mt-4 border-t border-border pt-3 text-sm">
        <span className="text-muted">Total </span>
        <span className="font-mono font-semibold">{total}</span>
      </p>
    </Card>
  );
}