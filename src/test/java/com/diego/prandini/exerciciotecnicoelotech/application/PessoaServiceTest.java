package com.diego.prandini.exerciciotecnicoelotech.application;

import com.diego.prandini.exerciciotecnicoelotech.domain.repository.PessoaRepository;
import com.diego.prandini.exerciciotecnicoelotech.infra.repository.PessoaRepositoryMemory;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

class PessoaServiceTest {

    @Test
    void deveCriarUmaPessoaComIdNomeCpfEDataDeNascimento() {
        PessoaRepository pessoaRepository = new PessoaRepositoryMemory();
        PessoaService pessoaService = new PessoaService(pessoaRepository);

        PessoaService.Output output = pessoaService.criar(new PessoaService.Input("Joao", "37783132669", LocalDate.of(1991, Month.NOVEMBER, 25)));

        assertThat(output).isNotNull();
        assertThat(output.id()).isNotNull();
        assertThat(output.nome()).isEqualTo("Joao");
        assertThat(output.cpf()).isEqualTo("37783132669");
        assertThat(output.dataDeNascimento()).isEqualTo(LocalDate.of(1991, Month.NOVEMBER, 25));
    }
}