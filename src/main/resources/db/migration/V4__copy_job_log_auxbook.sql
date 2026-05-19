-- DDL para el log de idempotencia de copia del módulo auxiliary-book.
-- PostgreSQL: usa TIMESTAMPTZ.
-- ADR-38, ADR-40.

CREATE TABLE IF NOT EXISTS auxbook_copy_job_log (
    id                      BIGSERIAL PRIMARY KEY,
    id_proceso              CHAR(36)        NOT NULL,
    fase                    INTEGER         NOT NULL,
    modulo                  VARCHAR(64)     NOT NULL,
    estado                  VARCHAR(32)     NOT NULL,
    fecha_inicio            TIMESTAMPTZ,
    fecha_fin               TIMESTAMPTZ,
    equivalencias_generadas INTEGER         NOT NULL DEFAULT 0,
    error_message           VARCHAR(2000),
    CONSTRAINT uq_auxbook_copy_job_log UNIQUE (id_proceso, fase, modulo)
);

CREATE INDEX IF NOT EXISTS idx_auxbook_copy_job_log_id_proceso
    ON auxbook_copy_job_log (id_proceso);
