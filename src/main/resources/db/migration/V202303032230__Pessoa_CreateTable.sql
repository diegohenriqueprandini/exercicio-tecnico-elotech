CREATE TABLE IF NOT EXISTS TABLE_PESSOA (
    ID UUID NOT NULL,
    CPF CHARACTER VARYING(255),
    DATA_NASCIMENTO DATE,
    NOME CHARACTER VARYING(255),
    CONSTRAINT TABLE_PESSOA_PKEY PRIMARY KEY (ID),
    CONSTRAINT TABLE_PESSOA_CPF_UK UNIQUE (CPF)
)
