-- CrimeWatch reference schema for PostgreSQL 17.
-- The running application uses JPA/Hibernate migrations in the lab profile.
-- This file documents the normalized relational model for review and reporting.

CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(20) UNIQUE NOT NULL CHECK (code IN ('CITIZEN','OFFICER','ADMIN')),
    display_name VARCHAR(120) NOT NULL
);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(160) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    role_id BIGINT NOT NULL REFERENCES roles(id),
    phone VARCHAR(20), address VARCHAR(300), enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE crime_categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(80) UNIQUE NOT NULL,
    description VARCHAR(300), active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE officers (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT UNIQUE NOT NULL REFERENCES users(id),
    badge_number VARCHAR(40) UNIQUE NOT NULL,
    department VARCHAR(80), rank_name VARCHAR(80), available BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE crime_reports (
    id BIGSERIAL PRIMARY KEY,
    public_id VARCHAR(30) UNIQUE NOT NULL,
    category_id BIGINT NOT NULL REFERENCES crime_categories(id),
    title VARCHAR(180) NOT NULL, description TEXT NOT NULL,
    incident_date DATE NOT NULL, incident_time TIME NOT NULL,
    location VARCHAR(240) NOT NULL, city VARCHAR(100) NOT NULL, area VARCHAR(120),
    latitude DOUBLE PRECISION, longitude DOUBLE PRECISION,
    suspect_information TEXT, witness_information TEXT, evidence_description TEXT, additional_remarks TEXT,
    status VARCHAR(30) NOT NULL, priority VARCHAR(20) NOT NULL,
    reporter_id BIGINT NOT NULL REFERENCES users(id),
    assigned_officer_id BIGINT REFERENCES users(id),
    created_at TIMESTAMPTZ NOT NULL, updated_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE report_status_history (
    id BIGSERIAL PRIMARY KEY,
    report_id BIGINT NOT NULL REFERENCES crime_reports(id),
    from_status VARCHAR(30), to_status VARCHAR(30) NOT NULL,
    comment VARCHAR(500), changed_by BIGINT NOT NULL REFERENCES users(id),
    changed_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE investigation_notes (
    id BIGSERIAL PRIMARY KEY,
    report_id BIGINT NOT NULL REFERENCES crime_reports(id),
    author_id BIGINT NOT NULL REFERENCES users(id),
    note TEXT NOT NULL, internal_only BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE evidence (
    id BIGSERIAL PRIMARY KEY,
    report_id BIGINT NOT NULL REFERENCES crime_reports(id),
    file_name VARCHAR(200) NOT NULL, storage_path VARCHAR(500) NOT NULL,
    content_type VARCHAR(100), file_size BIGINT,
    uploaded_by BIGINT NOT NULL REFERENCES users(id), uploaded_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id), report_id BIGINT REFERENCES crime_reports(id),
    message VARCHAR(500) NOT NULL, read_flag BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE audit_logs (
    id BIGSERIAL PRIMARY KEY,
    actor_id BIGINT REFERENCES users(id), action VARCHAR(80) NOT NULL,
    entity_type VARCHAR(80) NOT NULL, entity_id VARCHAR(80), details TEXT,
    ip_address VARCHAR(64), created_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_report_public_id ON crime_reports(public_id);
CREATE INDEX idx_report_status ON crime_reports(status);
CREATE INDEX idx_report_city ON crime_reports(city);
CREATE INDEX idx_history_report ON report_status_history(report_id, changed_at);
CREATE INDEX idx_notifications_user ON notifications(user_id, read_flag, created_at);

