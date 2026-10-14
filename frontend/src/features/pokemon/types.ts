export interface PokemonSummary {
  id: number;
  name: string;
  displayName: string;
  spriteUrl: string;
}

export interface PokemonStat {
  name: string;
  base: number;
}

export interface PokemonAbility {
  name: string;
  hidden: boolean;
}

/** Null fields are omitted by the backend (non_null inclusion), so they are optional here. */
export interface PokemonDetail {
  id: number;
  name: string;
  displayName: string;
  genus?: string;
  types: string[];
  heightM: number;
  weightKg: number;
  baseExperience?: number;
  artworkUrl: string;
  shinyUrl: string;
  cryUrl?: string;
  stats: PokemonStat[];
  abilities: PokemonAbility[];
  flavorText?: string;
  captureRate?: number;
  baseHappiness?: number;
  femaleRatio?: number; // absent = genderless
  color?: string;
  habitat?: string;
  generation?: string;
  growthRate?: string;
  eggGroups: string[];
  legendary: boolean;
  mythical: boolean;
  baby: boolean;
}