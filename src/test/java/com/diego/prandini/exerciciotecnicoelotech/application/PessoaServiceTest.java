package com.diego.prandini.exerciciotecnicoelotech.application;

import com.diego.prandini.exerciciotecnicoelotech.domain.entity.PessoaCpfVazioException;
import com.diego.prandini.exerciciotecnicoelotech.domain.entity.CpfInvalidoException;
import com.diego.prandini.exerciciotecnicoelotech.domain.entity.PessoaDataDeNascimentoFuturaException;
import com.diego.prandini.exerciciotecnicoelotech.domain.entity.PessoaNomeVazioException;
import com.diego.prandini.exerciciotecnicoelotech.domain.repository.PessoaRepository;
import com.diego.prandini.exerciciotecnicoelotech.infra.ApplicationClock;
import com.diego.prandini.exerciciotecnicoelotech.infra.ApplicationClockMock;
import com.diego.prandini.exerciciotecnicoelotech.infra.repository.PessoaRepositoryMemory;
import com.diego.prandini.exerciciotecnicoelotech.utils.DateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class PessoaServiceTest {

    public static final LocalDate DATA_DE_NASCIMENTO_DEFAULT = LocalDate.of(1991, Month.NOVEMBER, 25);
    public static final LocalDate TODAY_MOCK = LocalDate.of(2023, Month.MARCH, 1);

    private PessoaService pessoaService;

    @BeforeEach
    void setup() {
        PessoaRepository pessoaRepository = new PessoaRepositoryMemory();
        ApplicationClock applicationClock = new ApplicationClockMock(TODAY_MOCK);
        this.pessoaService = new PessoaService(pessoaRepository, applicationClock);
    }

    @Test
    void deveCriarUmaPessoaComNomeCpfEDataDeNascimento() {
        PessoaService.Output output = this.pessoaService.criar(new PessoaService.Input("Joao", "37783132669", DATA_DE_NASCIMENTO_DEFAULT));

        assertThat(output).isNotNull();
        assertThat(output.id()).isNotNull();
        assertThat(output.nome()).isEqualTo("Joao");
        assertThat(output.cpf()).isEqualTo("37783132669");
        assertThat(output.dataDeNascimento()).isEqualTo(DATA_DE_NASCIMENTO_DEFAULT);
    }

    @Test
    void nomeNuloNaoPermitido() {
        Throwable throwable = catchThrowable(() -> {
            PessoaService.Input pessoaSemNome = new PessoaService.Input(null, "37783132669", DATA_DE_NASCIMENTO_DEFAULT);
            this.pessoaService.criar(pessoaSemNome);
        });

        assertThat(throwable).isInstanceOf(PessoaNomeVazioException.class);
        assertThat(throwable.getMessage()).isEqualTo("Nome da pessoa não pode ser vazio");
    }

    @Test
    void nomeComApenasEspacosNaoPermitido() {
        Throwable throwable = catchThrowable(() -> {
            PessoaService.Input pessoaSemNome = new PessoaService.Input(" ", "37783132669", DATA_DE_NASCIMENTO_DEFAULT);
            this.pessoaService.criar(pessoaSemNome);
        });

        assertThat(throwable).isInstanceOf(PessoaNomeVazioException.class);
        assertThat(throwable.getMessage()).isEqualTo("Nome da pessoa não pode ser vazio");
    }

    @Test
    void cpfNuloNaoPermitido() {
        Throwable throwable = catchThrowable(() -> {
            PessoaService.Input pessoaSemCpf = new PessoaService.Input("Joao", null, DATA_DE_NASCIMENTO_DEFAULT);
            this.pessoaService.criar(pessoaSemCpf);
        });

        assertThat(throwable).isInstanceOf(PessoaCpfVazioException.class);
        assertThat(throwable.getMessage()).isEqualTo("Cpf da pessoa não pode ser vazio");
    }

    @Test
    void cpfComApenasEspacosNaoPermitido() {
        Throwable throwable = catchThrowable(() -> {
            PessoaService.Input pessoaSemCpf = new PessoaService.Input("Joao", " ", DATA_DE_NASCIMENTO_DEFAULT);
            this.pessoaService.criar(pessoaSemCpf);
        });

        assertThat(throwable).isInstanceOf(PessoaCpfVazioException.class);
        assertThat(throwable.getMessage()).isEqualTo("Cpf da pessoa não pode ser vazio");
    }

    @Test
    void cpfDeveSerValido() {
        Throwable throwable = catchThrowable(() -> {
            PessoaService.Input pessoaSemCpf = new PessoaService.Input("Joao", "37785134669", DATA_DE_NASCIMENTO_DEFAULT);
            this.pessoaService.criar(pessoaSemCpf);
        });

        assertThat(throwable).isInstanceOf(CpfInvalidoException.class);
        assertThat(throwable.getMessage()).isEqualTo("Cpf inválido: 37785134669");
    }

    @Test
    void pessoaPodeSerCriadaComCpfFormatado() {
        PessoaService.Output output = this.pessoaService.criar(new PessoaService.Input("Joao", "377.831.326-69", DATA_DE_NASCIMENTO_DEFAULT));

        assertThat(output).isNotNull();
        assertThat(output.id()).isNotNull();
        assertThat(output.nome()).isEqualTo("Joao");
        assertThat(output.cpf()).isEqualTo("37783132669");
        assertThat(output.dataDeNascimento()).isEqualTo(DATA_DE_NASCIMENTO_DEFAULT);
    }

    @Test
    void dataDeNascimentoNaoPodeSerFutura() {
        LocalDate dataDeNascimento = TODAY_MOCK.plusDays(1);
        Throwable throwable = catchThrowable(() -> {
            PessoaService.Input pessoaSemCpf = new PessoaService.Input("Joao", "37783132669", dataDeNascimento);
            this.pessoaService.criar(pessoaSemCpf);
        });

        assertThat(throwable).isInstanceOf(PessoaDataDeNascimentoFuturaException.class);
        assertThat(throwable.getMessage()).isEqualTo("Data de Nascimento não pode ser futura: " + DateUtils.toString(dataDeNascimento));
    }

    @Test
    void setDataDeNascimentoPodeSerHoje() {
        LocalDate dataDeNascimento = TODAY_MOCK;
        PessoaService.Output output = this.pessoaService.criar(new PessoaService.Input("Joao", "377.831.326-69", dataDeNascimento));

        assertThat(output).isNotNull();
        assertThat(output.id()).isNotNull();
        assertThat(output.nome()).isEqualTo("Joao");
        assertThat(output.cpf()).isEqualTo("37783132669");
        assertThat(output.dataDeNascimento()).isEqualTo(dataDeNascimento);
    }
}