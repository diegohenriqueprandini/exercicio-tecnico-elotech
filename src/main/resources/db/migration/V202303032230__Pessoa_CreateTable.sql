CREATE TABLE IF NOT EXISTS TABLE_PESSOA (
    ID UUID NOT NULL,
    NOME CHARACTER VARYING(255),
    CPF CHARACTER VARYING(255),
    DATA_NASCIMENTO DATE,
    CONSTRAINT TABLE_PESSOA_PKEY PRIMARY KEY (ID),
    CONSTRAINT TABLE_PESSOA_CPF_UK UNIQUE (CPF)
)
