package com.diego.prandini.exerciciotecnicoelotech.domain.contato;

import com.diego.prandini.exerciciotecnicoelotech.domain.entity.contato.Contato;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ContatoTest {

    private static final String CONTATO_DEFAULT = "Contato1";
    private static final String TELEFONE_DEFAULT = "44988776655";
    private static final String EMAIL_DEFAULT = "contato@email.com";

    @Test
    void deveCriarUmContato() {
        UUID id = UUID.randomUUID();
        Contato contato = new Contato(
                id,
                CONTATO_DEFAULT,
                TELEFONE_DEFAULT,
                EMAIL_DEFAULT
        );

        assertThat(contato.getId()).isEqualTo(id);
        assertThat(contato.getNome()).isEqualTo(CONTATO_DEFAULT);
        assertThat(contato.getTelefone()).isEqualTo(TELEFONE_DEFAULT);
        assertThat(contato.getEmail()).isEqualTo(EMAIL_DEFAULT);
    }
}