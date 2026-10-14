import { createContext, useContext } from 'react';

/** Creates a context plus a hook that throws if used outside its provider. */
export function createStrictContext<T>(name: string) {
  const Context = createContext<T | null>(null);
  Context.displayName = `${name}Context`;

  function useStrictContext(): T {
    const value = useContext(Context);
    if (value === null) {
      throw new Error(`use${name} must be used inside <${name}Provider>`);
    }
    return value;
  }

  return [Context, useStrictContext] as const;
}