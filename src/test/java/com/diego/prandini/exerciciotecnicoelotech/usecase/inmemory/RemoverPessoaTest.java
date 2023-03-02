package com.diego.prandini.exerciciotecnicoelotech.usecase.inmemory;

import com.diego.prandini.exerciciotecnicoelotech.application.RemoverPessoa;
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

public class RemoverPessoaTest {

    private static final LocalDate TODAY_MOCK = LocalDate.of(2023, Month.MARCH, 1);

    private static final String NOME_DEFAULT = "Joao";
    private static final String CPF_DEFAULT = "37783132669";
    private static final LocalDate DATA_DE_NASCIMENTO_DEFAULT = LocalDate.of(1991, Month.NOVEMBER, 25);

    @Test
    void deveRemoverPessoaPeloId() {
        PessoaRepository pessoaRepository = new PessoaRepositoryMemory();

        UUID id = criarPessoa(pessoaRepository);

        RemoverPessoa removerPessoa = new RemoverPessoa(pessoaRepository);
        removerPessoa.execure(id);

        Throwable throwable = catchThrowable(() -> pessoaRepository.getOne(id));
        assertThat(throwable).isInstanceOf(PessoaNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("Pessoa não encontrada: " + id);
    }

    @Test
    void idInexistenteDeveRetornarErro() {
        PessoaRepository pessoaRepository = new PessoaRepositoryMemory();

        UUID id = UUID.randomUUID();

        RemoverPessoa removerPessoa = new RemoverPessoa(pessoaRepository);

        Throwable throwable = catchThrowable(() -> removerPessoa.execure(id));
        assertThat(throwable).isInstanceOf(PessoaNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("Pessoa não encontrada: " + id);
    }

    @Test
    void deveRemoverAPessoaCertaPeloID() {
        PessoaRepository pessoaRepository = new PessoaRepositoryMemory();

        UUID idPessoa1 = criarPessoa(pessoaRepository);
        UUID idPessoa2 = criarPessoa(pessoaRepository);

        RemoverPessoa removerPessoa = new RemoverPessoa(pessoaRepository);
        removerPessoa.execure(idPessoa2);

        Pessoa pessoa1 = pessoaRepository.getOne(idPessoa1);
        assertThat(pessoa1).isNotNull();

        Throwable throwable = catchThrowable(() -> pessoaRepository.getOne(idPessoa2));
        assertThat(throwable).isInstanceOf(PessoaNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("Pessoa não encontrada: " + idPessoa2);
    }

    private UUID criarPessoa(PessoaRepository pessoaRepository) {
        ApplicationClock applicationClock = new ApplicationClockMock(TODAY_MOCK);
        UUID id = UUID.randomUUID();
        pessoaRepository.save(Pessoa.of(
                id,
                NOME_DEFAULT,
                CPF_DEFAULT,
                DATA_DE_NASCIMENTO_DEFAULT,
                applicationClock
        ));
        return id;
    }
}
