-- database schema (PostgreSQL)
CREATE TABLE coupon (
    id SERIAL PRIMARY KEY,
    type VARCHAR(255) NOT NULL,
    details JSONB NOT NULL
);