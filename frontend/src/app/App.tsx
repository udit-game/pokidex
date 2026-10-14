import { lazy } from 'react';
import { Route, Routes } from 'react-router-dom';

import { AppLayout } from './layout/AppLayout';
import HomePage from '../pages/HomePage';
import NotFoundPage from '../pages/NotFoundPage';

// detail page is code-split: the home page loads without it
const PokemonPage = lazy(() => import('../pages/PokemonPage'));

export function App() {
  return (
    <Routes>
      <Route element={<AppLayout />}>
        <Route index element={<HomePage />} />
        <Route path="pokemon/:name" element={<PokemonPage />} />
        <Route path="*" element={<NotFoundPage />} />
      </Route>
    </Routes>
  );
}