package com.diego.prandini.exerciciotecnicoelotech.usecase.pessoa;

import com.diego.prandini.exerciciotecnicoelotech.application.pessoa.BuscarPessoa;
import com.diego.prandini.exerciciotecnicoelotech.application.pessoa.CriarPessoa;
import com.diego.prandini.exerciciotecnicoelotech.application.pessoa.RemoverPessoa;
import com.diego.prandini.exerciciotecnicoelotech.domain.repository.PessoaRepository;
import com.diego.prandini.exerciciotecnicoelotech.exception.PessoaNotFoundException;
import com.diego.prandini.exerciciotecnicoelotech.infra.repository.memory.PessoaRepositoryMemory;
import com.diego.prandini.exerciciotecnicoelotech.infra.system.clock.ApplicationClock;
import com.diego.prandini.exerciciotecnicoelotech.infra.system.clock.ApplicationClockMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class RemoverPessoaTest {

    private static final LocalDate TODAY_MOCK = LocalDate.of(2023, Month.MARCH, 1);

    private static final String NOME_DEFAULT = "Joao";
    private static final String CPF_DEFAULT = "37783132669";
    private static final LocalDate DATA_DE_NASCIMENTO_DEFAULT = LocalDate.of(1991, Month.NOVEMBER, 25);

    private static final String CONTATO_DEFAULT = "Contato1";
    private static final String TELEFONE_DEFAULT = "44988776655";
    private static final String EMAIL_DEFAULT = "contato@email.com";

    private CriarPessoa criarPessoa;
    private RemoverPessoa removerPessoa;
    private BuscarPessoa buscarPessoa;
    private UUID idPessoaDefault;

    @BeforeEach
    void setup() {
        ApplicationClock applicationClock = new ApplicationClockMock(TODAY_MOCK);
        PessoaRepository pessoaRepository = new PessoaRepositoryMemory();
        criarPessoa = new CriarPessoa(pessoaRepository, applicationClock);
        removerPessoa = new RemoverPessoa(pessoaRepository);
        buscarPessoa = new BuscarPessoa(pessoaRepository);
        idPessoaDefault = criarPessoaDefaultParaAlteracoes();
    }

    @Test
    void deveRemoverPessoaPeloId() {
        removerPessoa.execure(idPessoaDefault);

        Throwable throwable = catchThrowable(() -> buscarPessoa.execute(idPessoaDefault));
        assertThat(throwable).isInstanceOf(PessoaNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("Pessoa não encontrada: " + idPessoaDefault);
    }

    @Test
    void idInexistenteDeveRetornarErro() {
        UUID id = UUID.randomUUID();
        Throwable throwable = catchThrowable(() -> removerPessoa.execure(id));

        assertThat(throwable).isInstanceOf(PessoaNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("Pessoa não encontrada: " + id);
    }

    @Test
    void dadoDuasPessoasDeveRemoverUmaPeloId() {
        UUID idPessoa1 = criarPessoa.execute(new CriarPessoa.Input(
                NOME_DEFAULT,
                "84873547938",
                DATA_DE_NASCIMENTO_DEFAULT,
                List.of(new CriarPessoa.ContatoInput(
                        CONTATO_DEFAULT,
                        TELEFONE_DEFAULT,
                        EMAIL_DEFAULT

                ))
        )).id();
        UUID idPessoa2 = criarPessoa.execute(new CriarPessoa.Input(
                NOME_DEFAULT,
                "06040259710",
                DATA_DE_NASCIMENTO_DEFAULT,
                List.of(new CriarPessoa.ContatoInput(
                        CONTATO_DEFAULT,
                        TELEFONE_DEFAULT,
                        EMAIL_DEFAULT

                ))
        )).id();

        removerPessoa.execure(idPessoa2);

        BuscarPessoa.Output outputPessoa1 = buscarPessoa.execute(idPessoa1);
        assertThat(outputPessoa1).isNotNull();

        Throwable throwable = catchThrowable(() -> buscarPessoa.execute(idPessoa2));
        assertThat(throwable).isInstanceOf(PessoaNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("Pessoa não encontrada: " + idPessoa2);
    }

    private UUID criarPessoaDefaultParaAlteracoes() {
        return criarPessoa.execute(new CriarPessoa.Input(
                NOME_DEFAULT,
                CPF_DEFAULT,
                DATA_DE_NASCIMENTO_DEFAULT,
                List.of(new CriarPessoa.ContatoInput(
                        CONTATO_DEFAULT,
                        TELEFONE_DEFAULT,
                        EMAIL_DEFAULT

                ))
        )).id();
    }
}
