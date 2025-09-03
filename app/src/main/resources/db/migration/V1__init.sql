-- Products table for Day 2
CREATE TABLE IF NOT EXISTS products (
    id           BIGSERIAL PRIMARY KEY,
    name         VARCHAR(100) NOT NULL,
    description  TEXT,
    price_cents  INTEGER      NOT NULL CHECK (price_cents >= 0),
    created_at   TIMESTAMPTZ  NOT NULL DEFAULT now()
);

-- Optional index on name (helps later with lookups)
CREATE INDEX IF NOT EXISTS idx_products_name ON products (name);
