CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS customer (
    id uuid DEFAULT uuid_generate_v4() NOT NULL,
    username VARCHAR NOT NULL,
    ssn BYTEA,
    phone_number BYTEA,
    PRIMARY KEY(id)
);
