import { Skeleton } from "../../../shared/components";


export function PokemonSkeleton() {
  return (
    <div className="space-y-6" aria-busy="true" aria-label="Loading Pokémon">
      <Skeleton className="h-72" />
      <div className="grid gap-6 md:grid-cols-2">
        <Skeleton className="h-64" />
        <Skeleton className="h-64" />
      </div>
      <Skeleton className="h-40" />
    </div>
  );
}