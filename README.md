# CobaltGate — SaaS Entitlements API

Java 21 / Spring Boot portfolio project for remote backend positions.

## Implemented
- REST subscription registration and retrieval
- Seat counts and plan validation
- Basic MockMvc tests
- GitHub Actions Maven verification

## Run
```bash
mvn spring-boot:run
mvn test
```
Sample: POST `/api/v1/subscriptions` with `{"accountId":"acme","plan":"business","seats":3}`.

**Scope:** This first uploaded version stores subscriptions in memory. MySQL persistence, Flyway migrations, authentication, audit events, and richer SaaS rules remain future work.
