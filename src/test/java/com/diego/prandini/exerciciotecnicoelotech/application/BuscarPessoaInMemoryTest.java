package com.diego.prandini.exerciciotecnicoelotech.application;

import com.diego.prandini.exerciciotecnicoelotech.domain.entity.Pessoa;
import com.diego.prandini.exerciciotecnicoelotech.domain.repository.PessoaRepository;
import com.diego.prandini.exerciciotecnicoelotech.exception.PessoaNotFoundException;
import com.diego.prandini.exerciciotecnicoelotech.infra.repository.PessoaRepositoryMemory;
import com.diego.prandini.exerciciotecnicoelotech.infra.system.ApplicationClock;
import com.diego.prandini.exerciciotecnicoelotech.infra.system.ApplicationClockMock;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class BuscarPessoaInMemoryTest {

    private static final LocalDate TODAY_MOCK = LocalDate.of(2023, Month.MARCH, 1);

    private static final String NOME_DEFAULT = "Joao";
    private static final String CPF_DEFAULT = "37783132669";
    private static final LocalDate DATA_DE_NASCIMENTO_DEFAULT = LocalDate.of(1991, Month.NOVEMBER, 25);

    @Test
    void deveBuscarUmaPessoaPeloId() {
        ApplicationClock applicationClock = new ApplicationClockMock(TODAY_MOCK);
        PessoaRepository pessoaRepository = new PessoaRepositoryMemory();

        UUID id = UUID.randomUUID();
        pessoaRepository.save(Pessoa.of(
                id,
                NOME_DEFAULT,
                CPF_DEFAULT,
                DATA_DE_NASCIMENTO_DEFAULT,
                applicationClock
        ));

        BuscarPessoa buscarPessoa = new BuscarPessoa(pessoaRepository);
        BuscarPessoa.Output output = buscarPessoa.execute(id);

        assertThat(output).isNotNull();
        assertThat(output.id()).isNotNull();
        assertThat(output.nome()).isEqualTo(NOME_DEFAULT);
        assertThat(output.cpf()).isEqualTo(CPF_DEFAULT);
        assertThat(output.dataDeNascimento()).isEqualTo(DATA_DE_NASCIMENTO_DEFAULT);
    }

    @Test
    void idInexistenteDeveRetornarErro() {
        PessoaRepository pessoaRepository = new PessoaRepositoryMemory();
        BuscarPessoa buscarPessoa = new BuscarPessoa(pessoaRepository);

        UUID id = UUID.randomUUID();

        Throwable throwable = catchThrowable(() -> buscarPessoa.execute(id));

        assertThat(throwable).isInstanceOf(PessoaNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("Pessoa não encontrada: " + id);
    }
}
