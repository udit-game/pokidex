# Pokidex

Pokidex is a Pokémon search and detail app. Search by name, partial name, or
Pokédex number; browse paginated results; and open a Pokémon profile with its
artwork, types, stats, abilities, species facts, and flavor text.

The frontend is a React single-page app. A Spring Boot API serves the frontend's
search and detail requests and retrieves Pokémon data from PokeAPI.

## Features

- Ranked search: exact name, prefix, word prefix, substring, then fuzzy match.
- Search by numeric Pokédex ID and name.
- Paginated Pokédex browsing and Pokémon detail pages.
- Normal and shiny artwork, type badges, stats, abilities, cries, and species
  information where available.
- Persistent light/dark theme.
- Loading states, inline retry actions, friendly API error messages, and a
  render error boundary.

## Tech stack

| Area | Technologies |
| --- | --- |
| Frontend | React 19, TypeScript, React Router, Vite, Tailwind CSS 4 |
| Backend | Java 21, Spring Boot, Spring MVC, Caffeine |
| Upstream data | [PokeAPI](https://pokeapi.co/) |
| Deployment | Docker, Docker Compose, nginx |

## Run locally

### Requirements

- Node.js 22 and npm
- Java 21
- Docker and Docker Compose (only if using the containerized setup)

### Start the backend

In a terminal:

```sh
cd backend
./gradlew bootRun
```

The API listens on `http://localhost:8080`. On startup it attempts to warm the
Pokémon name index. If warm-up fails, the backend logs a warning and loads the
index when a later request needs it.

### Start the frontend

In a second terminal:

```sh
cd frontend
npm ci
npm run dev
```

Open the URL printed by Vite (normally `http://localhost:5173`). In development,
Vite proxies `/api` requests to `http://localhost:8080`.

The frontend uses the same-origin API base `/api/v1` by default. To point it at a
different API base, set `VITE_API_BASE_URL` in the frontend environment before
starting/building Vite.

## Run with Docker Compose

From the repository root:

```sh
docker compose up --build
```

Open `http://localhost:3000`. Compose starts the Spring Boot backend and nginx
frontend; nginx proxies `/api/` requests to the backend. The backend is also
published on `http://localhost:8080`.

The frontend container serves the compiled static app. Hashed assets under
`/assets/` receive a one-year immutable cache policy, while `index.html` is
revalidated so deployments become visible promptly. API responses, including
their `Cache-Control` and `ETag` headers, pass through nginx.

## API

The API base path is `/api/v1`; the Spring servlet path is
`/api/v1/pokemon`.

| Method | Path | Description |
| --- | --- | --- |
| `GET` | `/api/v1/pokemon?q=pi&limit=10&offset=0` | Search Pokémon or list the index |
| `GET` | `/api/v1/pokemon/pikachu` | Get Pokémon details by name |
| `GET` | `/api/v1/pokemon/25` | Get Pokémon details by numeric ID |

Successful responses use an `ApiResponse` envelope. Search returns a page with
`items`, `total`, `limit`, and `offset`. Errors use the same envelope with an
error message and code.

Search is performed against a cached name index. Numeric detail requests first
resolve the ID to a name, so looking up `25` and `pikachu` uses the same
Pokémon-data cache entry.

## How the frontend is organized

Frontend source lives in [`frontend/src`](./frontend/src):

- `app/` contains app routes, shared layout, and top-level providers.
- `features/search/` contains the search context and dropdown components.
- `features/browse/` contains the paginated Pokédex grid.
- `features/pokemon/` contains detail data, context, and presentation.
- `shared/` contains API/error helpers, hooks, theme state, and reusable UI.
- `pages/` contains the home, detail, and not-found pages.

### State and data fetching

`AppProviders` mounts the theme and search contexts. The search context owns the
query, dropdown, keyboard selection, and result state. The Pokémon detail
context is mounted by the detail page with `key={name}`; changing Pokémon
therefore remounts its provider and starts with clean detail state.

`useAsync` owns request status and data. When its fetcher changes, it aborts the
previous request before starting the next one, preventing a stale, slower
response from replacing newer results. Search input is debounced by 250 ms
before it triggers a backend request.

The detail page is lazy-loaded, keeping its code out of the initial route
bundle.

### Error handling

The shared `httpClient` unwraps successful API envelopes and turns network
failures, non-JSON responses (such as a proxy error page), and backend
`ApiResponse` errors into `ApiError` instances. `friendlyMessage` maps known
error codes to user-facing copy.

Search results and the browse grid provide inline retry actions. The detail page
provides a retry for failed requests, and the app-level `ErrorBoundary` handles
unexpected render errors as a last resort.

## Backend caching and design decisions

The backend source lives in [`backend/src/main/java`](./backend/src/main/java).
The controller delegates search and detail work to services; a PokeAPI client
fetches upstream records, and the mapper assembles detail DTOs.

- Caffeine caches Pokémon records, species records, the name index, and
  not-found results. Cache sizes and TTLs are configured in
  `backend/src/main/resources/application.properties`.
- The name index is stored in the same Caffeine setup under one key. This
  reuses its TTL and Caffeine's atomic `get(key, loader)` behavior, so concurrent
  cache misses for that key share one load rather than maintaining a separate
  set of names.
- The backend caches the trimmed vendor records (`PokemonRecord` and
  `SpeciesRecord`), not the final detail DTO. DTO assembly is inexpensive, and
  changes to mapping logic take effect without waiting for cached DTOs to
  expire.
- Numeric IDs resolve to names through the name index before Pokémon data is
  loaded, avoiding duplicate cache entries for an ID and its corresponding
  name.
- Cache misses are single-flight per key. Upstream 404s are stored in a
  short-lived negative cache; other loader failures are not cached.
- Records and constructor injection are used instead of Lombok.

**Known limitation:** if the name-index cache expires, the first request that
needs it pays the reload cost. If PokeAPI is unavailable during that reload,
that request fails with a service-unavailable response. Serving stale index
data when a reload fails is a possible future optimization.

## Development commands

Frontend commands (run from `frontend/`):

```sh
npm run dev
npm run build
npm run lint
```

Backend commands (run from `backend/`):

```sh
./gradlew bootRun
./gradlew test
```

The frontend currently has no test script. The backend tests use JUnit through
Gradle.

## Repository layout

```text
.
├── backend/       Spring Boot API and backend tests
├── frontend/      React app, Vite config, and nginx deployment config
└── docker-compose.yml
```
