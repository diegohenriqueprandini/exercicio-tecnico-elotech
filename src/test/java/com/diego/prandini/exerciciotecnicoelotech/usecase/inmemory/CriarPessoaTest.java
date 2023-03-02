package com.diego.prandini.exerciciotecnicoelotech.usecase.inmemory;

import com.diego.prandini.exerciciotecnicoelotech.application.CriarPessoa;
import com.diego.prandini.exerciciotecnicoelotech.domain.repository.PessoaRepository;
import com.diego.prandini.exerciciotecnicoelotech.exception.CpfInvalidoException;
import com.diego.prandini.exerciciotecnicoelotech.exception.CpfVazioException;
import com.diego.prandini.exerciciotecnicoelotech.exception.DataDeNascimentoVaziaException;
import com.diego.prandini.exerciciotecnicoelotech.exception.PessoaCpfJaExisteException;
import com.diego.prandini.exerciciotecnicoelotech.exception.PessoaDataDeNascimentoFuturaException;
import com.diego.prandini.exerciciotecnicoelotech.exception.PessoaNomeVazioException;
import com.diego.prandini.exerciciotecnicoelotech.infra.repository.PessoaRepositoryMemory;
import com.diego.prandini.exerciciotecnicoelotech.infra.system.ApplicationClock;
import com.diego.prandini.exerciciotecnicoelotech.infra.system.ApplicationClockMock;
import com.diego.prandini.exerciciotecnicoelotech.utils.DateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class CriarPessoaTest {

    private static final LocalDate TODAY_MOCK = LocalDate.of(2023, Month.MARCH, 1);

    private static final String NOME_DEFAULT = "Joao";
    private static final String CPF_DEFAULT = "37783132669";
    private static final LocalDate DATA_DE_NASCIMENTO_DEFAULT = LocalDate.of(1991, Month.NOVEMBER, 25);

    private CriarPessoa criarPessoa;

    @BeforeEach
    void setup() {
        ApplicationClock applicationClock = new ApplicationClockMock(TODAY_MOCK);
        PessoaRepository pessoaRepository = new PessoaRepositoryMemory();
        criarPessoa = new CriarPessoa(pessoaRepository, applicationClock);
    }

    @Test
    void deveCriarUmaPessoaComNomeCpfEDataDeNascimento() {
        CriarPessoa.Input input = new CriarPessoa.Input(
                NOME_DEFAULT,
                CPF_DEFAULT,
                DATA_DE_NASCIMENTO_DEFAULT
        );
        CriarPessoa.Output output = criarPessoa.execute(input);

        assertThat(output).isNotNull();
        assertThat(output.id()).isNotNull();
        assertThat(output.nome()).isEqualTo(NOME_DEFAULT);
        assertThat(output.cpf()).isEqualTo(CPF_DEFAULT);
        assertThat(output.dataDeNascimento()).isEqualTo(DATA_DE_NASCIMENTO_DEFAULT);
    }

    @Test
    void nomeNuloNaoPermitido() {
        Throwable throwable = catchThrowable(() -> {
            CriarPessoa.Input input = new CriarPessoa.Input(
                    null,
                    CPF_DEFAULT,
                    DATA_DE_NASCIMENTO_DEFAULT
            );
            criarPessoa.execute(input);
        });

        assertThat(throwable).isInstanceOf(PessoaNomeVazioException.class);
        assertThat(throwable.getMessage()).isEqualTo("Nome não pode ser vazio");
    }

    @Test
    void nomeComApenasEspacosNaoPermitido() {
        Throwable throwable = catchThrowable(() -> {
            CriarPessoa.Input input = new CriarPessoa.Input(
                    " ",
                    CPF_DEFAULT,
                    DATA_DE_NASCIMENTO_DEFAULT
            );
            criarPessoa.execute(input);
        });

        assertThat(throwable).isInstanceOf(PessoaNomeVazioException.class);
        assertThat(throwable.getMessage()).isEqualTo("Nome não pode ser vazio");
    }

    @Test
    void cpfNaoPodeSerNulo() {
        Throwable throwable = catchThrowable(() -> {
            CriarPessoa.Input input = new CriarPessoa.Input(
                    NOME_DEFAULT,
                    null,
                    DATA_DE_NASCIMENTO_DEFAULT
            );
            criarPessoa.execute(input);
        });

        assertThat(throwable).isInstanceOf(CpfVazioException.class);
        assertThat(throwable.getMessage()).isEqualTo("Cpf não pode ser vazio");
    }

    @Test
    void cpfNaoPodeSerComApenasEspacos() {
        Throwable throwable = catchThrowable(() -> {
            CriarPessoa.Input input = new CriarPessoa.Input(
                    NOME_DEFAULT,
                    " ",
                    DATA_DE_NASCIMENTO_DEFAULT
            );
            criarPessoa.execute(input);
        });

        assertThat(throwable).isInstanceOf(CpfInvalidoException.class);
        assertThat(throwable.getMessage()).isEqualTo("Cpf inválido: ' '");
    }

    @Test
    void cpfDeveSerValido() {
        Throwable throwable = catchThrowable(() -> {
            CriarPessoa.Input input = new CriarPessoa.Input(
                    NOME_DEFAULT,
                    "37785134669",
                    DATA_DE_NASCIMENTO_DEFAULT
            );
            criarPessoa.execute(input);
        });

        assertThat(throwable).isInstanceOf(CpfInvalidoException.class);
        assertThat(throwable.getMessage()).isEqualTo("Cpf inválido: '37785134669'");
    }

    @Test
    void pessoaPodeSerCriadaComCpfFormatado() {
        CriarPessoa.Input input = new CriarPessoa.Input(
                NOME_DEFAULT,
                "377.831.326-69",
                DATA_DE_NASCIMENTO_DEFAULT
        );
        CriarPessoa.Output output = criarPessoa.execute(input);

        assertThat(output).isNotNull();
        assertThat(output.id()).isNotNull();
        assertThat(output.nome()).isEqualTo(NOME_DEFAULT);
        assertThat(output.cpf()).isEqualTo(CPF_DEFAULT);
        assertThat(output.dataDeNascimento()).isEqualTo(DATA_DE_NASCIMENTO_DEFAULT);
    }

    @Test
    void dataDeNascimentoNaoPodeSerNula() {
        Throwable throwable = catchThrowable(() -> {
            CriarPessoa.Input input = new CriarPessoa.Input(
                    NOME_DEFAULT,
                    CPF_DEFAULT,
                    null
            );
            criarPessoa.execute(input);
        });

        assertThat(throwable).isInstanceOf(DataDeNascimentoVaziaException.class);
        assertThat(throwable.getMessage()).isEqualTo("Data de nascimento não pode ser vazia");
    }

    @Test
    void dataDeNascimentoNaoPodeSerFutura() {
        LocalDate dataDeNascimento = TODAY_MOCK.plusDays(1);
        Throwable throwable = catchThrowable(() -> {
            CriarPessoa.Input input = new CriarPessoa.Input(
                    NOME_DEFAULT,
                    CPF_DEFAULT,
                    dataDeNascimento
            );
            criarPessoa.execute(input);
        });

        assertThat(throwable).isInstanceOf(PessoaDataDeNascimentoFuturaException.class);
        assertThat(throwable.getMessage()).isEqualTo("Data de nascimento não pode ser futura: " + DateUtils.toString(dataDeNascimento));
    }

    @Test
    void dataDeNascimentoPodeSerHoje() {
        LocalDate dataDeNascimento = TODAY_MOCK;
        CriarPessoa.Input input = new CriarPessoa.Input(
                NOME_DEFAULT,
                CPF_DEFAULT,
                dataDeNascimento
        );
        CriarPessoa.Output output = criarPessoa.execute(input);

        assertThat(output).isNotNull();
        assertThat(output.id()).isNotNull();
        assertThat(output.nome()).isEqualTo(NOME_DEFAULT);
        assertThat(output.cpf()).isEqualTo(CPF_DEFAULT);
        assertThat(output.dataDeNascimento()).isEqualTo(dataDeNascimento);
    }

    @Test
    void deveCadastrarDuasPessoasIguais() {
        criarPessoa.execute(new CriarPessoa.Input(
                NOME_DEFAULT,
                CPF_DEFAULT,
                DATA_DE_NASCIMENTO_DEFAULT
        ));

        Throwable throwable = catchThrowable(() -> criarPessoa.execute(new CriarPessoa.Input(
                NOME_DEFAULT,
                CPF_DEFAULT,
                DATA_DE_NASCIMENTO_DEFAULT
        )));

        assertThat(throwable).isInstanceOf(PessoaCpfJaExisteException.class);
        assertThat(throwable.getMessage()).isEqualTo("Cpf já existe: " + CPF_DEFAULT);
    }

    @Test
    void deveCadastrarDuasPessoasComCpfsDiferentes() {
        CriarPessoa.Output pessoa1 = criarPessoa.execute(new CriarPessoa.Input(
                NOME_DEFAULT,
                CPF_DEFAULT,
                DATA_DE_NASCIMENTO_DEFAULT
        ));

        CriarPessoa.Output pessoa2 = criarPessoa.execute(new CriarPessoa.Input(
                NOME_DEFAULT,
                "47621474602",
                DATA_DE_NASCIMENTO_DEFAULT
        ));

        assertThat(pessoa1).isNotNull();
        assertThat(pessoa1.id()).isNotNull();
        assertThat(pessoa1.nome()).isEqualTo(NOME_DEFAULT);
        assertThat(pessoa1.cpf()).isEqualTo(CPF_DEFAULT);
        assertThat(pessoa1.dataDeNascimento()).isEqualTo(DATA_DE_NASCIMENTO_DEFAULT);

        assertThat(pessoa2).isNotNull();
        assertThat(pessoa2.id()).isNotNull();
        assertThat(pessoa2.nome()).isEqualTo(NOME_DEFAULT);
        assertThat(pessoa2.cpf()).isEqualTo("47621474602");
        assertThat(pessoa2.dataDeNascimento()).isEqualTo(DATA_DE_NASCIMENTO_DEFAULT);
    }
}