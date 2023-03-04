CREATE TABLE IF NOT EXISTS TABLE_PESSOA_CONTATOS (
    ID UUID NOT NULL,
    PESSOA_ID UUID NOT NULL,
    EMAIL CHARACTER VARYING(255),
    NOME CHARACTER VARYING(255),
    TELEFONE CHARACTER VARYING(255),
    CONSTRAINT TABLE_PESSOA_CONTATOS_PKEY PRIMARY KEY (ID),
    CONSTRAINT PESSOA_CONTATO_FK FOREIGN KEY (PESSOA_ID) REFERENCES TABLE_PESSOA (ID)
)
