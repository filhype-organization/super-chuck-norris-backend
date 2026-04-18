# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Dev mode (hot reload)
./mvnw quarkus:dev

# Run tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=JokeServiceTest

# Build (JVM)
./mvnw package

# Quarkus update
./mvnw -DquarkusRegistryClient=true io.quarkus:quarkus-maven-plugin:<version>:update -N -ntp
```

> **WSL note**: `mvnw` must have LF line endings to run on Linux/WSL. A `.gitattributes` enforces this. If it breaks, run: `tr -d '\r' < mvnw > /tmp/mvnw_lf && cp /tmp/mvnw_lf mvnw`

## Architecture

Single-resource REST API built with Quarkus. Three meaningful layers:

- **`app.entity.Joke`** — Panache entity (`PanacheEntityBase`) with UUID primary key, `joke` (String), and `created_at` (auto-set). Public fields per Panache convention.
- **`app.service.JokeService`** — All business logic and DB access. `@Transactional` on write operations.
- **`app.controller.JokeResource`** — JAX-RS resource at `@Path("/")`. Authorization via `@RolesAllowed` per endpoint.
- **`app.configuration.ApplicationConfiguration`** — Global `@ServerExceptionMapper` that converts all exceptions (including `WebApplicationException`) to a uniform JSON error response.

## Authentication

Keycloak OIDC. In dev/test, a Keycloak dev service starts automatically on port `8180` using `app-realm.json`. Client credentials: `client_id=back`, `secret=secret`.

Role model:
- `GET /` (all jokes) — public
- `GET /getRandomJoke`, `GET /{id}` — requires `user`, `admin`, `read`, or `write`
- `POST /`, `PUT /`, `DELETE /{id}` — requires `admin` or `write`

## Tests

- **`JokeResourceTest`** — `@QuarkusTest` with REST Assured. Fetches a real Keycloak token via `client_credentials` before each test. `BASE_PATH = "/"`.
- **`JokeServiceTest`** — `@QuarkusTest` injecting `JokeService` directly, no HTTP involved.

Test profile loads `chuck_norris_db.sql` as seed data (`%dev,test.quarkus.hibernate-orm.sql-load-script`).

## CI/CD

GitHub Actions calls a reusable workflow from `filhype-organization/universal-devops-action`. Builds a native multi-arch image (`amd64` + `arm64`) and pushes to Docker Hub as `leeson77/chuck-norris-backend`.
