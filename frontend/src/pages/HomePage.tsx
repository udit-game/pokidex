import { Link } from 'react-router-dom';
import { SearchBox } from '../features/search/components/SearchBox';
import { QUICK_PICKS } from '../config/constants';
import { titleCase } from '../shared/utils/format';
import { BrowseGrid } from '../features/browse/components/BrowseGrid';


export default function HomePage() {
  return (
    <div className="space-y-12">
      <section className="mx-auto max-w-2xl space-y-5 pt-6 text-center">
        <h1 className="text-4xl font-extrabold tracking-tight md:text-5xl">
          Gotta search &apos;em <span className="text-brand">fast</span>
        </h1>
        <p className="text-muted">Find any Pokémon by name or number. Typos welcome.</p>

        <SearchBox size="lg" />

        <div className="flex flex-wrap justify-center gap-2 pt-1">
          {QUICK_PICKS.map((name) => (
            <Link
              key={name}
              to={`/pokemon/${name}`}
              className="rounded-full border border-border bg-surface px-3 py-1 text-sm transition hover:border-brand"
            >
              {titleCase(name)}
            </Link>
          ))}
        </div>
      </section>

      <BrowseGrid />
    </div>
  );
}