# PostgreSQL Optimization Notes

- Use UUID primary keys for global distribution and future microservice decoupling.
- Add `pg_trgm` indexes for search over product name and brand when free-text search is required.
- Use `JSONB` for analytics payloads because it supports indexing and flexible event fields.
- Partition `analytics_events` by `event_timestamp` if event volume grows into millions of rows.
- Keep `created_at`, `updated_at`, and `event_timestamp` indexed where query patterns are time-based.
- Apply `VACUUM`, `ANALYZE`, and connection pooling in production.
