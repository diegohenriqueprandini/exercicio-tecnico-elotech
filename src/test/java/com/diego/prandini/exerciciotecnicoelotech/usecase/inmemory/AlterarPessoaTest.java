package com.diego.prandini.exerciciotecnicoelotech.usecase.inmemory;

import com.diego.prandini.exerciciotecnicoelotech.application.AlterarPessoa;
import com.diego.prandini.exerciciotecnicoelotech.domain.entity.Pessoa;
import com.diego.prandini.exerciciotecnicoelotech.domain.repository.PessoaRepository;
import com.diego.prandini.exerciciotecnicoelotech.exception.CpfInvalidoException;
import com.diego.prandini.exerciciotecnicoelotech.exception.DataDeNascimentoVaziaException;
import com.diego.prandini.exerciciotecnicoelotech.exception.PessoaCpfJaExisteException;
import com.diego.prandini.exerciciotecnicoelotech.exception.CpfVazioException;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class AlterarPessoaTest {

    private static final LocalDate TODAY_MOCK = LocalDate.of(2023, Month.MARCH, 1);

    private static final String OLD_NOME_DEFAULT = "Old Joao";
    private static final String OLD_CPF_DEFAULT = "37783132669";
    private static final LocalDate OLD_DATA_DE_NASCIMENTO_DEFAULT = LocalDate.of(1991, Month.NOVEMBER, 25);

    private static final UUID ID_DEFAULT = UUID.randomUUID();
    private static final String NOVO_NOME_DEFAULT = "Novo Joao";
    private static final String NOVO_CPF_DEFAULT = "25853460218";
    private static final LocalDate NOVO_DATA_DE_NASCIMENTO_DEFAULT = LocalDate.of(1995, Month.DECEMBER, 15);

    private ApplicationClock applicationClock;
    private PessoaRepository pessoaRepository;
    private AlterarPessoa alterarPessoa;

    @BeforeEach
    void setup() {
        applicationClock = new ApplicationClockMock(TODAY_MOCK);
        pessoaRepository = new PessoaRepositoryMemory();
        pessoaRepository.save(Pessoa.of(
                ID_DEFAULT,
                OLD_NOME_DEFAULT,
                OLD_CPF_DEFAULT,
                OLD_DATA_DE_NASCIMENTO_DEFAULT,
                applicationClock
        ));
        alterarPessoa = new AlterarPessoa(pessoaRepository, applicationClock);
    }

    @Test
    void deveAlterarNomeCpfEDataDeNascimentoPeloId() {
        AlterarPessoa.Input input = new AlterarPessoa.Input(
                NOVO_NOME_DEFAULT,
                NOVO_CPF_DEFAULT,
                NOVO_DATA_DE_NASCIMENTO_DEFAULT
        );
        AlterarPessoa.Output output = alterarPessoa.execute(ID_DEFAULT, input);

        assertThat(output).isNotNull();
        assertThat(output.id()).isEqualTo(ID_DEFAULT);
        assertThat(output.nome()).isEqualTo(NOVO_NOME_DEFAULT);
        assertThat(output.cpf()).isEqualTo(NOVO_CPF_DEFAULT);
        assertThat(output.dataDeNascimento()).isEqualTo(NOVO_DATA_DE_NASCIMENTO_DEFAULT);
    }

    @Test
    void nomeNuloNaoPermitido() {
        Throwable throwable = catchThrowable(() -> {
            AlterarPessoa.Input input = new AlterarPessoa.Input(
                    null,
                    NOVO_CPF_DEFAULT,
                    NOVO_DATA_DE_NASCIMENTO_DEFAULT
            );
            alterarPessoa.execute(ID_DEFAULT, input);
        });

        assertThat(throwable).isInstanceOf(PessoaNomeVazioException.class);
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
            alterarPessoa.execute(ID_DEFAULT, input);
        });

        assertThat(throwable).isInstanceOf(PessoaNomeVazioException.class);
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
            alterarPessoa.execute(ID_DEFAULT, input);
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
            alterarPessoa.execute(ID_DEFAULT, input);
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
            alterarPessoa.execute(ID_DEFAULT, input);
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
        AlterarPessoa.Output output = alterarPessoa.execute(ID_DEFAULT, input);

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
            alterarPessoa.execute(ID_DEFAULT, input);
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
            alterarPessoa.execute(ID_DEFAULT, input);
        });

        assertThat(throwable).isInstanceOf(PessoaDataDeNascimentoFuturaException.class);
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
        AlterarPessoa.Output output = alterarPessoa.execute(ID_DEFAULT, input);

        assertThat(output).isNotNull();
        assertThat(output.id()).isNotNull();
        assertThat(output.nome()).isEqualTo(NOVO_NOME_DEFAULT);
        assertThat(output.cpf()).isEqualTo(NOVO_CPF_DEFAULT);
        assertThat(output.dataDeNascimento()).isEqualTo(dataDeNascimento);
    }

    @Test
    void naoPodeAlterarCpfParaUmJaExistente() {
        pessoaRepository.save(Pessoa.of(
                UUID.randomUUID(),
                NOVO_NOME_DEFAULT,
                "98841337273",
                NOVO_DATA_DE_NASCIMENTO_DEFAULT,
                applicationClock
        ));

        Throwable throwable = catchThrowable(() -> {
            AlterarPessoa.Input input = new AlterarPessoa.Input(
                    OLD_NOME_DEFAULT,
                    "98841337273",
                    OLD_DATA_DE_NASCIMENTO_DEFAULT
            );
            alterarPessoa.execute(ID_DEFAULT, input);
        });

        assertThat(throwable).isInstanceOf(PessoaCpfJaExisteException.class);
        assertThat(throwable.getMessage()).isEqualTo("Cpf já existe: 98841337273");
    }
}
