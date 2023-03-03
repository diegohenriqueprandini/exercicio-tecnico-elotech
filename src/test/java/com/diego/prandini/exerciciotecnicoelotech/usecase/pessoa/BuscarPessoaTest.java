package com.diego.prandini.exerciciotecnicoelotech.usecase.pessoa;

import com.diego.prandini.exerciciotecnicoelotech.application.pessoa.BuscarPessoa;
import com.diego.prandini.exerciciotecnicoelotech.application.pessoa.CriarPessoa;
import com.diego.prandini.exerciciotecnicoelotech.domain.repository.PessoaRepository;
import com.diego.prandini.exerciciotecnicoelotech.exception.PessoaNotFoundException;
import com.diego.prandini.exerciciotecnicoelotech.infra.repository.pessoa.PessoaRepositoryMemory;
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

public class BuscarPessoaTest {

    private static final LocalDate TODAY_MOCK = LocalDate.of(2023, Month.MARCH, 1);

    private static final String NOME_DEFAULT = "Joao";
    private static final String CPF_DEFAULT = "37783132669";
    private static final LocalDate DATA_DE_NASCIMENTO_DEFAULT = LocalDate.of(1991, Month.NOVEMBER, 25);

    private static final String CONTATO_DEFAULT = "Contato1";
    private static final String TELEFONE_DEFAULT = "44988776655";
    private static final String EMAIL_DEFAULT = "contato@email.com";

    private CriarPessoa criarPessoa;
    private BuscarPessoa buscarPessoa;
    private UUID idPessoaDefault;

    @BeforeEach
    void setup() {
        ApplicationClock applicationClock = new ApplicationClockMock(TODAY_MOCK);
        PessoaRepository pessoaRepository = new PessoaRepositoryMemory();
        criarPessoa = new CriarPessoa(pessoaRepository, applicationClock);
        buscarPessoa = new BuscarPessoa(pessoaRepository);
        idPessoaDefault = criarPessoaDefaultParaAlteracoes(new CriarPessoa.Input(
                NOME_DEFAULT,
                CPF_DEFAULT,
                DATA_DE_NASCIMENTO_DEFAULT,
                List.of(new CriarPessoa.ContatoInput(
                        CONTATO_DEFAULT,
                        TELEFONE_DEFAULT,
                        EMAIL_DEFAULT

                ))
        ));
    }

    @Test
    void deveBuscarUmaPessoaPeloId() {
        BuscarPessoa.Output output = buscarPessoa.execute(idPessoaDefault);

        assertThat(output).isNotNull();
        assertThat(output.id()).isNotNull();
        assertThat(output.nome()).isEqualTo(NOME_DEFAULT);
        assertThat(output.cpf()).isEqualTo(CPF_DEFAULT);
        assertThat(output.dataDeNascimento()).isEqualTo(DATA_DE_NASCIMENTO_DEFAULT);
    }

    @Test
    void idInexistenteDeveRetornarErro() {
        UUID id = UUID.randomUUID();
        Throwable throwable = catchThrowable(() -> buscarPessoa.execute(id));

        assertThat(throwable).isInstanceOf(PessoaNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("Pessoa não encontrada: " + id);
    }

    private UUID criarPessoaDefaultParaAlteracoes(CriarPessoa.Input input) {
        CriarPessoa.Output output = criarPessoa.execute(input);
        assertThat(output).isNotNull();
        return output.id();
    }
}
