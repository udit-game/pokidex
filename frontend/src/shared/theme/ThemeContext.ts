import { createStrictContext } from "../lib/createStrictContext";


export type Theme = 'light' | 'dark';

export interface ThemeContextValue {
  theme: Theme;
  toggleTheme: () => void;
}

export const [ThemeContext, useTheme] = createStrictContext<ThemeContextValue>('Theme');