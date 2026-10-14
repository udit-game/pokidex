/** CSS colour for a Pokémon type, from the @theme palette in index.css. Unknown types fall back to normal. */
export const typeColor = (type: string): string =>
  `var(--color-type-${type}, var(--color-type-normal))`;

/** Translucent tint of a type colour (for gradients and backgrounds). */
export const typeTint = (type: string, percent = 30): string =>
  `color-mix(in srgb, ${typeColor(type)} ${percent}%, transparent)`;