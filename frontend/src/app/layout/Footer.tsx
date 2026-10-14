export function Footer() {
  return (
    <footer className="border-t border-border py-6 text-center text-sm text-muted">
      Data from{' '}
      <a href="https://pokeapi.co" target="_blank" rel="noreferrer" className="underline hover:text-fg">
        PokéAPI
      </a>
      . Pokémon is © Nintendo / Game Freak.
    </footer>
  );
}