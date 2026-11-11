# Authorization model — local development

The HTTP API requires Basic authentication (over HTTPS in deployment).
GET subscription endpoints require READER or ADMIN; create, grant seat,
and change limit require ADMIN. All unknown routes are denied.

Two locally generated accounts are configurable via environment variables:
`DEMO_ADMIN_USER` / `DEMO_ADMIN_PASSWORD`, and
`DEMO_READER_USER` / `DEMO_READER_PASSWORD`.

**Do not deploy the sample passwords or local in-memory user database.**
For production, prefer a managed OIDC identity provider, short-lived
OAuth2 access tokens, TLS, secret management, audit-log retention, and
tenant-scoped authorization. Tests cover 401, 403 and read-only access.
