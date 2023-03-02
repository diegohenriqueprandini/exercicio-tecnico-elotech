package com.diego.prandini.exerciciotecnicoelotech.domain.entity;

import com.diego.prandini.exerciciotecnicoelotech.infra.system.ApplicationClock;
import com.diego.prandini.exerciciotecnicoelotech.infra.system.ApplicationClockMock;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PessoaTest {

    private static final LocalDate TODAY_MOCK = LocalDate.of(2023, Month.MARCH, 1);

    private static final UUID ID_DEFAULT = UUID.randomUUID();
    private static final String NOME_DEFAULT = "Joao";
    private static final String CPF_DEFAULT = "37783132669";
    private static final LocalDate DATA_DE_NASCIMENTO_DEFAULT = LocalDate.of(1991, Month.NOVEMBER, 25);

    @Test
    void deveCriarUmaPessoaPeloBuiler() {
        ApplicationClock applicationClock = new ApplicationClockMock(TODAY_MOCK);
        UUID id = UUID.randomUUID();
        Pessoa pessoa = new Pessoa.CriarBuilder(applicationClock)
                .id(id)
                .nome(NOME_DEFAULT)
                .cpf(CPF_DEFAULT)
                .dataDeNascimento(DATA_DE_NASCIMENTO_DEFAULT)
                .build();

        assertThat(pessoa).isNotNull();
        assertThat(pessoa.getId()).isNotNull();
        assertThat(pessoa.getNome()).isEqualTo(NOME_DEFAULT);
        assertThat(pessoa.getCpf()).isEqualTo(new Cpf(CPF_DEFAULT));
        assertThat(pessoa.getDataDeNascimento()).isEqualTo(new DataDeNascimento(DATA_DE_NASCIMENTO_DEFAULT));
    }

    @Test
    void deveCriarUmaPessoaPeloData() {
        UUID id = UUID.randomUUID();
        Pessoa pessoa = Pessoa.fromData(new Pessoa.Data(
                id,
                NOME_DEFAULT,
                CPF_DEFAULT,
                DATA_DE_NASCIMENTO_DEFAULT
        ));

        assertThat(pessoa).isNotNull();
        assertThat(pessoa.getId()).isNotNull();
        assertThat(pessoa.getNome()).isEqualTo(NOME_DEFAULT);
        assertThat(pessoa.getCpf()).isEqualTo(new Cpf(CPF_DEFAULT));
        assertThat(pessoa.getDataDeNascimento()).isEqualTo(new DataDeNascimento(DATA_DE_NASCIMENTO_DEFAULT));
    }

    @Test
    void deveConverterUmaPessoaEmData() {
        Pessoa.Data data = new Pessoa.Data(
                ID_DEFAULT,
                NOME_DEFAULT,
                CPF_DEFAULT,
                DATA_DE_NASCIMENTO_DEFAULT
        );
        Pessoa pessoa = Pessoa.fromData(data);

        assertThat(pessoa.toData()).isEqualTo(data);
    }
}