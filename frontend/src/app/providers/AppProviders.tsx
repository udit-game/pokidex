import type { ReactNode } from 'react';
import { BrowserRouter } from 'react-router-dom';
import { ErrorBoundary } from '../../shared/components';
import { ThemeProvider } from '../../shared/theme/ThemeProvider';
import { SearchProvider } from '../../features/search/context/SearchProvider';


/** Order matters: SearchProvider needs the router (useNavigate). */
export function AppProviders({ children }: { children: ReactNode }) {
  return (
    <ErrorBoundary>
      <BrowserRouter>
        <ThemeProvider>
          <SearchProvider>{children}</SearchProvider>
        </ThemeProvider>
      </BrowserRouter>
    </ErrorBoundary>
  );
}