CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS customer (
    id uuid DEFAULT uuid_generate_v4() NOT NULL,
    username VARCHAR NOT NULL,
    ssn VARCHAR(4096),
    phone_number VARCHAR(4096),
    PRIMARY KEY(id)
);
