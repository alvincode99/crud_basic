CREATE TABLE productos (
    id BIGSERIAL PRIMARY KEY,
    sku VARCHAR(100) NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT,
    precio NUMERIC(12, 2) NOT NULL,
    stock INTEGER NOT NULL,
    categoria VARCHAR(120),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT uk_productos_sku UNIQUE (sku)
);

CREATE INDEX idx_productos_nombre_lower ON productos (LOWER(nombre));
