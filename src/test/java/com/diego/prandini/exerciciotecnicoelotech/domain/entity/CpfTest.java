package com.diego.prandini.exerciciotecnicoelotech.domain.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class CpfTest {

    @Test
    void deveCriarUmCpfValido() {
        Cpf cpf = new Cpf("47757818800");

        assertThat(cpf).isNotNull();
        assertThat(cpf.toString()).isEqualTo("47757818800");
    }

    @Test
    void deveRemoverAMascaraDoCpf() {
        Cpf cpf = new Cpf("477.578.188-00");

        assertThat(cpf).isNotNull();
        assertThat(cpf.toString()).isEqualTo("47757818800");
    }

    @Test
    void cpfDeveSerValido() {
        Throwable throwable = catchThrowable(() -> new Cpf("477.578.177-10"));

        assertThat(throwable).isInstanceOf(CpfInvalidoException.class);
        assertThat(throwable.getMessage()).isEqualTo("Cpf inválido: 477.578.177-10");
    }
}