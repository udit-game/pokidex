import { Link, useLocation } from 'react-router-dom';
import { SearchBox } from '../../features/search/components/SearchBox';
import { ThemeToggle } from '../../shared/theme/ThemeToggle';

export function Header() {
  const { pathname } = useLocation();
  const showSearch = pathname !== '/'; // the home page has its own hero search

  return (
    <header className="sticky top-0 z-30 border-b border-border bg-surface/80 backdrop-blur">
      <div className="mx-auto flex max-w-5xl items-center gap-4 px-4 py-3">
        <Link to="/" className="flex items-center gap-2 text-lg font-bold">
          <span aria-hidden className="relative h-6 w-6 overflow-hidden rounded-full border-2 border-fg">
            <span className="absolute inset-x-0 top-0 h-1/2 bg-brand" />
            <span className="absolute inset-x-0 bottom-0 h-1/2 bg-surface" />
          </span>
          Pokidex
        </Link>

        <div className="flex-1">
          {showSearch && (
            <div className="ml-auto max-w-sm">
              <SearchBox />
            </div>
          )}
        </div>

        <ThemeToggle />
      </div>
    </header>
  );
}