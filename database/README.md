# Database

CrimeWatch uses PostgreSQL 17 in Docker with the named volume `crimewatch_postgres_data`.
JPA creates and updates the lab schema from the entity model. `schema.sql` is a readable reference used in the report and database-design review; it is not executed automatically, avoiding a second source of schema migrations.

The demo-data initializer creates 3 roles, 21 synthetic users, 5 officer profiles, 10 categories, 30 reports, case history, investigation notes, notifications, and audit activity. No real crime or personal records are included.
