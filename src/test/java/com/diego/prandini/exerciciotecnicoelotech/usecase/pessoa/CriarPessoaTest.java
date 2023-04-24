package com.diego.prandini.exerciciotecnicoelotech.usecase.pessoa;

import com.diego.prandini.exerciciotecnicoelotech.application.usecase.pessoa.BuscarPessoa;
import com.diego.prandini.exerciciotecnicoelotech.application.usecase.pessoa.CriarPessoa;
import com.diego.prandini.exerciciotecnicoelotech.application.repository.PessoaRepository;
import com.diego.prandini.exerciciotecnicoelotech.exception.ContatosVazioException;
import com.diego.prandini.exerciciotecnicoelotech.exception.CpfInvalidoException;
import com.diego.prandini.exerciciotecnicoelotech.exception.CpfJaExisteException;
import com.diego.prandini.exerciciotecnicoelotech.exception.CpfVazioException;
import com.diego.prandini.exerciciotecnicoelotech.exception.DataDeNascimentoFuturaException;
import com.diego.prandini.exerciciotecnicoelotech.exception.DataDeNascimentoVaziaException;
import com.diego.prandini.exerciciotecnicoelotech.exception.NomeVazioException;
import com.diego.prandini.exerciciotecnicoelotech.infra.repository.memory.PessoaRepositoryMemory;
import com.diego.prandini.exerciciotecnicoelotech.infra.system.clock.ApplicationClock;
import com.diego.prandini.exerciciotecnicoelotech.infra.system.clock.ApplicationClockMock;
import com.diego.prandini.exerciciotecnicoelotech.infra.system.security.PasswordEncoder;
import com.diego.prandini.exerciciotecnicoelotech.infra.system.security.PasswordEncoderNoOp;
import com.diego.prandini.exerciciotecnicoelotech.utils.DateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class CriarPessoaTest {

    private static final LocalDate TODAY_MOCK = LocalDate.of(2023, Month.MARCH, 1);

    private static final String NOME_DEFAULT = "Joao";
    private static final String CPF_DEFAULT = "37783132669";
    private static final LocalDate DATA_DE_NASCIMENTO_DEFAULT = LocalDate.of(1991, Month.NOVEMBER, 25);
    private static final String PASSWORD_DEFAULT = "Senha@123";

    private static final String CONTATO_DEFAULT = "Contato1";
    private static final String TELEFONE_DEFAULT = "44988776655";
    private static final String EMAIL_DEFAULT = "contato@email.com";
    private static final List<CriarPessoa.ContatoInput> CONTATOS_DEFAULT = List.of(new CriarPessoa.ContatoInput(
            CONTATO_DEFAULT,
            TELEFONE_DEFAULT,
            EMAIL_DEFAULT
    ));

    private CriarPessoa criarPessoa;
    private BuscarPessoa buscarPessoa;

    @BeforeEach
    void setup() {
        ApplicationClock applicationClock = new ApplicationClockMock(TODAY_MOCK);
        PasswordEncoder passwordEncoder = new PasswordEncoderNoOp();
        PessoaRepository pessoaRepository = new PessoaRepositoryMemory();
        criarPessoa = new CriarPessoa(pessoaRepository, applicationClock, passwordEncoder);
        buscarPessoa = new BuscarPessoa(pessoaRepository);
    }

    @Test
    void deveCriarUmaPessoaComNomeCpfDataDeNascimentoEUmContato() {
        CriarPessoa.Input input = new CriarPessoa.Input(
                NOME_DEFAULT,
                CPF_DEFAULT,
                DATA_DE_NASCIMENTO_DEFAULT,
                PASSWORD_DEFAULT,
                CONTATOS_DEFAULT
        );
        CriarPessoa.Output output = criarPessoa.execute(input);

        assertThat(output).isNotNull();
        assertThat(output.id()).isNotNull();
        assertThat(output.nome()).isEqualTo(NOME_DEFAULT);
        assertThat(output.cpf()).isEqualTo(CPF_DEFAULT);
        assertThat(output.dataDeNascimento()).isEqualTo(DATA_DE_NASCIMENTO_DEFAULT);

        BuscarPessoa.Output buscarOutput = buscarPessoa.execute(output.id());
        assertThat(buscarOutput).isNotNull();
        assertThat(buscarOutput.id()).isEqualTo(output.id());
        assertThat(buscarOutput.nome()).isEqualTo(NOME_DEFAULT);
        assertThat(buscarOutput.cpf()).isEqualTo(CPF_DEFAULT);
        assertThat(buscarOutput.dataDeNascimento()).isEqualTo(DATA_DE_NASCIMENTO_DEFAULT);
    }

    @Test
    void nomeNuloNaoPermitido() {
        Throwable throwable = catchThrowable(() -> {
            CriarPessoa.Input input = new CriarPessoa.Input(
                    null,
                    CPF_DEFAULT,
                    DATA_DE_NASCIMENTO_DEFAULT,
                    PASSWORD_DEFAULT,
                    CONTATOS_DEFAULT
            );
            criarPessoa.execute(input);
        });

        assertThat(throwable).isInstanceOf(NomeVazioException.class);
        assertThat(throwable.getMessage()).isEqualTo("Nome não pode ser vazio");
    }

    @Test
    void nomeComApenasEspacosNaoPermitido() {
        Throwable throwable = catchThrowable(() -> {
            CriarPessoa.Input input = new CriarPessoa.Input(
                    " ",
                    CPF_DEFAULT,
                    DATA_DE_NASCIMENTO_DEFAULT,
                    PASSWORD_DEFAULT,
                    CONTATOS_DEFAULT
            );
            criarPessoa.execute(input);
        });

        assertThat(throwable).isInstanceOf(NomeVazioException.class);
        assertThat(throwable.getMessage()).isEqualTo("Nome não pode ser vazio");
    }

    @Test
    void cpfNaoPodeSerNulo() {
        Throwable throwable = catchThrowable(() -> {
            CriarPessoa.Input input = new CriarPessoa.Input(
                    NOME_DEFAULT,
                    null,
                    DATA_DE_NASCIMENTO_DEFAULT,
                    PASSWORD_DEFAULT,
                    CONTATOS_DEFAULT
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
                    DATA_DE_NASCIMENTO_DEFAULT,
                    PASSWORD_DEFAULT,
                    CONTATOS_DEFAULT
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
                    DATA_DE_NASCIMENTO_DEFAULT,
                    PASSWORD_DEFAULT,
                    CONTATOS_DEFAULT
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
                DATA_DE_NASCIMENTO_DEFAULT,
                PASSWORD_DEFAULT,
                CONTATOS_DEFAULT
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
                    null,
                    PASSWORD_DEFAULT,
                    CONTATOS_DEFAULT
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
                    dataDeNascimento,
                    PASSWORD_DEFAULT,
                    CONTATOS_DEFAULT
            );
            criarPessoa.execute(input);
        });

        assertThat(throwable).isInstanceOf(DataDeNascimentoFuturaException.class);
        assertThat(throwable.getMessage()).isEqualTo("Data de nascimento não pode ser futura: " + DateUtils.toString(dataDeNascimento));
    }

    @Test
    void dataDeNascimentoPodeSerHoje() {
        LocalDate dataDeNascimento = TODAY_MOCK;
        CriarPessoa.Input input = new CriarPessoa.Input(
                NOME_DEFAULT,
                CPF_DEFAULT,
                dataDeNascimento,
                PASSWORD_DEFAULT,
                CONTATOS_DEFAULT
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
                DATA_DE_NASCIMENTO_DEFAULT,
                PASSWORD_DEFAULT,
                CONTATOS_DEFAULT
        ));

        Throwable throwable = catchThrowable(() -> criarPessoa.execute(new CriarPessoa.Input(
                NOME_DEFAULT,
                CPF_DEFAULT,
                DATA_DE_NASCIMENTO_DEFAULT,
                PASSWORD_DEFAULT,
                CONTATOS_DEFAULT
        )));

        assertThat(throwable).isInstanceOf(CpfJaExisteException.class);
        assertThat(throwable.getMessage()).isEqualTo("Cpf já existe: " + CPF_DEFAULT);
    }

    @Test
    void deveCadastrarDuasPessoasComCpfsDiferentes() {
        CriarPessoa.Output pessoa1 = criarPessoa.execute(new CriarPessoa.Input(
                NOME_DEFAULT,
                CPF_DEFAULT,
                DATA_DE_NASCIMENTO_DEFAULT,
                PASSWORD_DEFAULT,
                CONTATOS_DEFAULT
        ));

        CriarPessoa.Output pessoa2 = criarPessoa.execute(new CriarPessoa.Input(
                NOME_DEFAULT,
                "47621474602",
                DATA_DE_NASCIMENTO_DEFAULT,
                PASSWORD_DEFAULT,
                CONTATOS_DEFAULT
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

    @Test
    void naoDeveCriarPessoaSemContato() {
        CriarPessoa.Input input = new CriarPessoa.Input(
                NOME_DEFAULT,
                CPF_DEFAULT,
                DATA_DE_NASCIMENTO_DEFAULT,
                PASSWORD_DEFAULT,
                null
        );

        Throwable throwable = catchThrowable(() -> criarPessoa.execute(input));

        assertThat(throwable).isInstanceOf(ContatosVazioException.class);
        assertThat(throwable.getMessage()).isEqualTo("Deve possuir ao menos um contato");
    }
}