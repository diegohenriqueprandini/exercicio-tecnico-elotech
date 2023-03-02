package com.diego.prandini.exerciciotecnicoelotech.application;

import com.diego.prandini.exerciciotecnicoelotech.domain.repository.PessoaRepository;
import com.diego.prandini.exerciciotecnicoelotech.exception.CpfInvalidoException;
import com.diego.prandini.exerciciotecnicoelotech.exception.PessoaCpfVazioException;
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

class CriarPessoaInMemoryTest {

    private static final LocalDate TODAY_MOCK = LocalDate.of(2023, Month.MARCH, 1);

    private static final String NOME_DEFAULT = "Joao";
    private static final String CPF_DEFAULT = "37783132669";
    private static final LocalDate DATA_DE_NASCIMENTO_DEFAULT = LocalDate.of(1991, Month.NOVEMBER, 25);

    private CriarPessoa criarPessoa;

    @BeforeEach
    void setup() {
        PessoaRepository pessoaRepository = new PessoaRepositoryMemory();
        ApplicationClock applicationClock = new ApplicationClockMock(TODAY_MOCK);
        criarPessoa = new CriarPessoa(pessoaRepository, applicationClock);
    }

    @Test
    void deveCriarUmaPessoaComNomeCpfEDataDeNascimento() {
        CriarPessoa.Input input = new CriarPessoa.Input(NOME_DEFAULT, CPF_DEFAULT, DATA_DE_NASCIMENTO_DEFAULT);
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
            CriarPessoa.Input input = new CriarPessoa.Input(null, CPF_DEFAULT, DATA_DE_NASCIMENTO_DEFAULT);
            criarPessoa.execute(input);
        });

        assertThat(throwable).isInstanceOf(PessoaNomeVazioException.class);
        assertThat(throwable.getMessage()).isEqualTo("Nome da pessoa não pode ser vazio");
    }

    @Test
    void nomeComApenasEspacosNaoPermitido() {
        Throwable throwable = catchThrowable(() -> {
            CriarPessoa.Input input = new CriarPessoa.Input(" ", CPF_DEFAULT, DATA_DE_NASCIMENTO_DEFAULT);
            criarPessoa.execute(input);
        });

        assertThat(throwable).isInstanceOf(PessoaNomeVazioException.class);
        assertThat(throwable.getMessage()).isEqualTo("Nome da pessoa não pode ser vazio");
    }

    @Test
    void cpfNuloNaoPermitido() {
        Throwable throwable = catchThrowable(() -> {
            CriarPessoa.Input input = new CriarPessoa.Input(NOME_DEFAULT, null, DATA_DE_NASCIMENTO_DEFAULT);
            criarPessoa.execute(input);
        });

        assertThat(throwable).isInstanceOf(PessoaCpfVazioException.class);
        assertThat(throwable.getMessage()).isEqualTo("Cpf da pessoa não pode ser vazio");
    }

    @Test
    void cpfComApenasEspacosNaoPermitido() {
        Throwable throwable = catchThrowable(() -> {
            CriarPessoa.Input input = new CriarPessoa.Input(NOME_DEFAULT, " ", DATA_DE_NASCIMENTO_DEFAULT);
            criarPessoa.execute(input);
        });

        assertThat(throwable).isInstanceOf(PessoaCpfVazioException.class);
        assertThat(throwable.getMessage()).isEqualTo("Cpf da pessoa não pode ser vazio");
    }

    @Test
    void cpfDeveSerValido() {
        Throwable throwable = catchThrowable(() -> {
            CriarPessoa.Input input = new CriarPessoa.Input(NOME_DEFAULT, "37785134669", DATA_DE_NASCIMENTO_DEFAULT);
            criarPessoa.execute(input);
        });

        assertThat(throwable).isInstanceOf(CpfInvalidoException.class);
        assertThat(throwable.getMessage()).isEqualTo("Cpf inválido: 37785134669");
    }

    @Test
    void pessoaPodeSerCriadaComCpfFormatado() {
        CriarPessoa.Input input = new CriarPessoa.Input(NOME_DEFAULT, "377.831.326-69", DATA_DE_NASCIMENTO_DEFAULT);
        CriarPessoa.Output output = criarPessoa.execute(input);

        assertThat(output).isNotNull();
        assertThat(output.id()).isNotNull();
        assertThat(output.nome()).isEqualTo(NOME_DEFAULT);
        assertThat(output.cpf()).isEqualTo(CPF_DEFAULT);
        assertThat(output.dataDeNascimento()).isEqualTo(DATA_DE_NASCIMENTO_DEFAULT);
    }

    @Test
    void dataDeNascimentoNaoPodeSerFutura() {
        LocalDate dataDeNascimento = TODAY_MOCK.plusDays(1);
        Throwable throwable = catchThrowable(() -> {
            CriarPessoa.Input input = new CriarPessoa.Input(NOME_DEFAULT, CPF_DEFAULT, dataDeNascimento);
            criarPessoa.execute(input);
        });

        assertThat(throwable).isInstanceOf(PessoaDataDeNascimentoFuturaException.class);
        assertThat(throwable.getMessage()).isEqualTo("Data de Nascimento não pode ser futura: " + DateUtils.toString(dataDeNascimento));
    }

    @Test
    void dataDeNascimentoPodeSerHoje() {
        LocalDate dataDeNascimento = TODAY_MOCK;
        CriarPessoa.Input input = new CriarPessoa.Input(NOME_DEFAULT, CPF_DEFAULT, dataDeNascimento);
        CriarPessoa.Output output = criarPessoa.execute(input);

        assertThat(output).isNotNull();
        assertThat(output.id()).isNotNull();
        assertThat(output.nome()).isEqualTo(NOME_DEFAULT);
        assertThat(output.cpf()).isEqualTo(CPF_DEFAULT);
        assertThat(output.dataDeNascimento()).isEqualTo(dataDeNascimento);
    }

    @Test
    void deveCadastrarDuasPessoasIguais() {
        CriarPessoa.Output output1 = criarPessoa.execute(new CriarPessoa.Input(NOME_DEFAULT, CPF_DEFAULT, DATA_DE_NASCIMENTO_DEFAULT));
        CriarPessoa.Output output2 = criarPessoa.execute(new CriarPessoa.Input(NOME_DEFAULT, CPF_DEFAULT, DATA_DE_NASCIMENTO_DEFAULT));

        assertThat(output1).isNotNull();
        assertThat(output1.id()).isNotNull();
        assertThat(output1.nome()).isEqualTo(NOME_DEFAULT);
        assertThat(output1.cpf()).isEqualTo(CPF_DEFAULT);
        assertThat(output1.dataDeNascimento()).isEqualTo(DATA_DE_NASCIMENTO_DEFAULT);

        assertThat(output2).isNotNull();
        assertThat(output2.id()).isNotNull();
        assertThat(output2.nome()).isEqualTo(NOME_DEFAULT);
        assertThat(output2.cpf()).isEqualTo(CPF_DEFAULT);
        assertThat(output2.dataDeNascimento()).isEqualTo(DATA_DE_NASCIMENTO_DEFAULT);
    }
}