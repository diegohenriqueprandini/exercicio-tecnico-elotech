package com.diego.prandini.exerciciotecnicoelotech.usecase.pessoa;

import com.diego.prandini.exerciciotecnicoelotech.application.usecase.pessoa.AlterarPessoa;
import com.diego.prandini.exerciciotecnicoelotech.application.usecase.pessoa.BuscarPessoa;
import com.diego.prandini.exerciciotecnicoelotech.application.usecase.pessoa.CriarPessoa;
import com.diego.prandini.exerciciotecnicoelotech.application.repository.PessoaRepository;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class AlterarPessoaTest {

    private static final LocalDate TODAY_MOCK = LocalDate.of(2023, Month.MARCH, 1);

    private static final String NOME_DEFAULT = "Joao";
    private static final String CPF_DEFAULT = "37783132669";
    private static final LocalDate DATA_DE_NASCIMENTO_DEFAULT = LocalDate.of(1991, Month.NOVEMBER, 25);

    private static final String NOVO_NOME_DEFAULT = "Novo Joao";
    private static final String NOVO_CPF_DEFAULT = "25853460218";
    private static final LocalDate NOVO_DATA_DE_NASCIMENTO_DEFAULT = LocalDate.of(1995, Month.DECEMBER, 15);

    private static final String CONTATO_DEFAULT = "Contato1";
    private static final String TELEFONE_DEFAULT = "44988776655";
    private static final String EMAIL_DEFAULT = "contato@email.com";
    private static final String PASSWORD_DEFAULT = "Senha@123";

    private CriarPessoa criarPessoa;
    private AlterarPessoa alterarPessoa;
    private BuscarPessoa buscarPessoa;
    private UUID idPessoaDefault;

    @BeforeEach
    void setup() {
        ApplicationClock applicationClock = new ApplicationClockMock(TODAY_MOCK);
        PasswordEncoder passwordEncoder = new PasswordEncoderNoOp();
        PessoaRepository pessoaRepository = new PessoaRepositoryMemory();
        criarPessoa = new CriarPessoa(pessoaRepository, applicationClock, passwordEncoder);
        alterarPessoa = new AlterarPessoa(pessoaRepository, applicationClock);
        buscarPessoa = new BuscarPessoa(pessoaRepository);
        idPessoaDefault = criarPessoaDefaultParaAlteracoes();
    }

    @Test
    void deveAlterarNomeCpfEDataDeNascimentoPeloId() {
        AlterarPessoa.Input input = new AlterarPessoa.Input(
                NOVO_NOME_DEFAULT,
                NOVO_CPF_DEFAULT,
                NOVO_DATA_DE_NASCIMENTO_DEFAULT
        );
        AlterarPessoa.Output output = alterarPessoa.execute(idPessoaDefault, input);

        assertThat(output).isNotNull();
        assertThat(output.id()).isEqualTo(idPessoaDefault);
        assertThat(output.nome()).isEqualTo(NOVO_NOME_DEFAULT);
        assertThat(output.cpf()).isEqualTo(NOVO_CPF_DEFAULT);
        assertThat(output.dataDeNascimento()).isEqualTo(NOVO_DATA_DE_NASCIMENTO_DEFAULT);

        BuscarPessoa.Output outputBuscar = buscarPessoa.execute(idPessoaDefault);
        assertThat(outputBuscar).isNotNull();
        assertThat(outputBuscar.id()).isEqualTo(idPessoaDefault);
        assertThat(outputBuscar.nome()).isEqualTo(NOVO_NOME_DEFAULT);
        assertThat(outputBuscar.cpf()).isEqualTo(NOVO_CPF_DEFAULT);
        assertThat(outputBuscar.dataDeNascimento()).isEqualTo(NOVO_DATA_DE_NASCIMENTO_DEFAULT);
    }

    @Test
    void nomeNuloNaoPermitido() {
        Throwable throwable = catchThrowable(() -> {
            AlterarPessoa.Input input = new AlterarPessoa.Input(
                    null,
                    NOVO_CPF_DEFAULT,
                    NOVO_DATA_DE_NASCIMENTO_DEFAULT
            );
            alterarPessoa.execute(idPessoaDefault, input);
        });

        assertThat(throwable).isInstanceOf(NomeVazioException.class);
        assertThat(throwable.getMessage()).isEqualTo("Nome não pode ser vazio");
    }

    @Test
    void nomeComApenasEspacosNaoPermitido() {
        Throwable throwable = catchThrowable(() -> {
            AlterarPessoa.Input input = new AlterarPessoa.Input(
                    " ",
                    NOVO_CPF_DEFAULT,
                    NOVO_DATA_DE_NASCIMENTO_DEFAULT
            );
            alterarPessoa.execute(idPessoaDefault, input);
        });

        assertThat(throwable).isInstanceOf(NomeVazioException.class);
        assertThat(throwable.getMessage()).isEqualTo("Nome não pode ser vazio");
    }

    @Test
    void cpfNuloNaoPermitido() {
        Throwable throwable = catchThrowable(() -> {
            AlterarPessoa.Input input = new AlterarPessoa.Input(
                    NOVO_NOME_DEFAULT,
                    null,
                    NOVO_DATA_DE_NASCIMENTO_DEFAULT
            );
            alterarPessoa.execute(idPessoaDefault, input);
        });

        assertThat(throwable).isInstanceOf(CpfVazioException.class);
        assertThat(throwable.getMessage()).isEqualTo("Cpf não pode ser vazio");
    }

    @Test
    void cpfComApenasEspacosNaoPermitido() {
        Throwable throwable = catchThrowable(() -> {
            AlterarPessoa.Input input = new AlterarPessoa.Input(
                    NOVO_NOME_DEFAULT,
                    " ",
                    NOVO_DATA_DE_NASCIMENTO_DEFAULT
            );
            alterarPessoa.execute(idPessoaDefault, input);
        });

        assertThat(throwable).isInstanceOf(CpfInvalidoException.class);
        assertThat(throwable.getMessage()).isEqualTo("Cpf inválido: ' '");
    }

    @Test
    void cpfDeveSerValido() {
        Throwable throwable = catchThrowable(() -> {
            AlterarPessoa.Input input = new AlterarPessoa.Input(
                    NOVO_NOME_DEFAULT,
                    "25853765222",
                    NOVO_DATA_DE_NASCIMENTO_DEFAULT
            );
            alterarPessoa.execute(idPessoaDefault, input);
        });

        assertThat(throwable).isInstanceOf(CpfInvalidoException.class);
        assertThat(throwable.getMessage()).isEqualTo("Cpf inválido: '25853765222'");
    }

    @Test
    void pessoaPodeSerCriadaComCpfFormatado() {
        AlterarPessoa.Input input = new AlterarPessoa.Input(
                NOVO_NOME_DEFAULT,
                "258.534.602-18",
                NOVO_DATA_DE_NASCIMENTO_DEFAULT
        );
        AlterarPessoa.Output output = alterarPessoa.execute(idPessoaDefault, input);

        assertThat(output).isNotNull();
        assertThat(output.id()).isNotNull();
        assertThat(output.nome()).isEqualTo(NOVO_NOME_DEFAULT);
        assertThat(output.cpf()).isEqualTo(NOVO_CPF_DEFAULT);
        assertThat(output.dataDeNascimento()).isEqualTo(NOVO_DATA_DE_NASCIMENTO_DEFAULT);
    }

    @Test
    void dataDeNascimentoNaoPodeSerNula() {
        Throwable throwable = catchThrowable(() -> {
            AlterarPessoa.Input input = new AlterarPessoa.Input(
                    NOVO_NOME_DEFAULT,
                    NOVO_CPF_DEFAULT,
                    null
            );
            alterarPessoa.execute(idPessoaDefault, input);
        });

        assertThat(throwable).isInstanceOf(DataDeNascimentoVaziaException.class);
        assertThat(throwable.getMessage()).isEqualTo("Data de nascimento não pode ser vazia");
    }

    @Test
    void dataDeNascimentoNaoPodeSerFutura() {
        LocalDate dataDeNascimento = TODAY_MOCK.plusDays(1);
        Throwable throwable = catchThrowable(() -> {
            AlterarPessoa.Input input = new AlterarPessoa.Input(
                    NOVO_NOME_DEFAULT,
                    NOVO_CPF_DEFAULT,
                    dataDeNascimento
            );
            alterarPessoa.execute(idPessoaDefault, input);
        });

        assertThat(throwable).isInstanceOf(DataDeNascimentoFuturaException.class);
        assertThat(throwable.getMessage()).isEqualTo("Data de nascimento não pode ser futura: " + DateUtils.toString(dataDeNascimento));
    }

    @Test
    void dataDeNascimentoPodeSerHoje() {
        LocalDate dataDeNascimento = TODAY_MOCK;
        AlterarPessoa.Input input = new AlterarPessoa.Input(
                NOVO_NOME_DEFAULT,
                NOVO_CPF_DEFAULT,
                dataDeNascimento
        );
        AlterarPessoa.Output output = alterarPessoa.execute(idPessoaDefault, input);

        assertThat(output).isNotNull();
        assertThat(output.id()).isNotNull();
        assertThat(output.nome()).isEqualTo(NOVO_NOME_DEFAULT);
        assertThat(output.cpf()).isEqualTo(NOVO_CPF_DEFAULT);
        assertThat(output.dataDeNascimento()).isEqualTo(dataDeNascimento);
    }

    @Test
    void naoPodeAlterarCpfParaUmJaExistente() {
        criarPessoa.execute(new CriarPessoa.Input(
                NOME_DEFAULT,
                "98841337273",
                DATA_DE_NASCIMENTO_DEFAULT,
                PASSWORD_DEFAULT,
                List.of(new CriarPessoa.ContatoInput(
                        CONTATO_DEFAULT,
                        TELEFONE_DEFAULT,
                        EMAIL_DEFAULT
                ))
        ));

        Throwable throwable = catchThrowable(() -> {
            AlterarPessoa.Input input = new AlterarPessoa.Input(
                    NOME_DEFAULT,
                    "98841337273",
                    DATA_DE_NASCIMENTO_DEFAULT
            );
            alterarPessoa.execute(idPessoaDefault, input);
        });

        assertThat(throwable).isInstanceOf(CpfJaExisteException.class);
        assertThat(throwable.getMessage()).isEqualTo("Cpf já existe: 98841337273");
    }

    private UUID criarPessoaDefaultParaAlteracoes() {
        return criarPessoa.execute(new CriarPessoa.Input(
                NOME_DEFAULT,
                CPF_DEFAULT,
                DATA_DE_NASCIMENTO_DEFAULT,
                PASSWORD_DEFAULT,
                List.of(new CriarPessoa.ContatoInput(
                        CONTATO_DEFAULT,
                        TELEFONE_DEFAULT,
                        EMAIL_DEFAULT
                ))
        )).id();
    }
}
