import { typeColor } from "../../../shared/theme/typeColors";


export function TypeBadge({ type }: { type: string }) {
  return (
    <span
      className="rounded-full px-3 py-1 text-xs font-semibold uppercase tracking-wide text-white [text-shadow:0_1px_2px_rgba(0,0,0,0.4)]"
      style={{ backgroundColor: typeColor(type) }}
    >
      {type}
    </span>
  );
}