# CobaltGate — SaaS Entitlements & Seat Provisioning
**Target role:** Remote Java backend engineer (Java 21, Spring Boot 3.5, Spring Data JPA, Hibernate, MySQL 8, Flyway, Spring Security, Maven, JUnit 5).

A B2B account provisioning service that enforces seat entitlements with transactional invariants. Workspace administrators can subscribe, grant seats, inspect members and increase their seat limit. This is a backend application with meaningful state transitions, not a CRUD-only database exercise.

## Core capabilities
- Spring REST API with validated DTOs and typed responses.
- JPA/Hibernate entities, versioned optimistic concurrency, row-locking during seat assignment.
- Unique membership per tenant/email and seat-exhaustion conflicts.
- Audit records created in the same transaction as subscription changes.
- Flyway schema versioning, indexed MySQL relational tables, problem responses.
- Development-only HTTP Basic login (replace with OIDC/JWT for internet-facing systems).
- JUnit + MockMvc API integration tests using H2 MySQL compatibility mode.
- GitHub Actions CI, Dockerfile and Compose.

## Run
```bash
cp .env.example .env
# change the local sample passwords
docker compose --env-file .env up --build
```
API: `http://localhost:8092` · Health: `http://localhost:8092/actuator/health`.

```bash
curl -u portfolio:ChangeMeLocal -H 'Content-Type: application/json' \
 -d '{"company":"Northwind Research","seatLimit":5}' \
 -X POST http://localhost:8092/api/v1/subscriptions
```
Response includes an `id`. Assign a seat:
```bash
curl -u portfolio:ChangeMeLocal -H 'Content-Type: application/json' \
 -d '{"email":"member@example.org"}' \
 -X POST http://localhost:8092/api/v1/subscriptions/1/members
```
Repeated membership requests return the same membership; seat count does not increase twice. After reaching `seatLimit` the API returns 409.

## Test
```bash
mvn -B verify
```

## Production readiness checklist
The Basic authentication fixture and credentials are for isolated local development. Before public deployment: OIDC/JWT, role-based tenant authorization, TLS, secret storage, rate limiting, structured traces, real MySQL integration tests with Testcontainers and rolling schema migrations. No claim of production approval is made.
