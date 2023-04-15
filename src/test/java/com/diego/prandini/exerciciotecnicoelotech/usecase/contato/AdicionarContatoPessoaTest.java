package com.diego.prandini.exerciciotecnicoelotech.usecase.contato;

import com.diego.prandini.exerciciotecnicoelotech.application.contato.AdicionarContatoPessoa;
import com.diego.prandini.exerciciotecnicoelotech.application.contato.BuscarContatoPessoa;
import com.diego.prandini.exerciciotecnicoelotech.application.pessoa.CriarPessoa;
import com.diego.prandini.exerciciotecnicoelotech.domain.repository.PessoaRepository;
import com.diego.prandini.exerciciotecnicoelotech.infra.repository.memory.PessoaRepositoryMemory;
import com.diego.prandini.exerciciotecnicoelotech.infra.system.clock.ApplicationClock;
import com.diego.prandini.exerciciotecnicoelotech.infra.system.clock.ApplicationClockMock;
import com.diego.prandini.exerciciotecnicoelotech.infra.system.security.PasswordEncoder;
import com.diego.prandini.exerciciotecnicoelotech.infra.system.security.PasswordEncoderNop;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class AdicionarContatoPessoaTest {

    private static final LocalDate TODAY_MOCK = LocalDate.of(2023, Month.MARCH, 1);

    private static final String NOME_DEFAULT = "Joao";
    private static final String CPF_DEFAULT = "37783132669";
    private static final LocalDate DATA_DE_NASCIMENTO_DEFAULT = LocalDate.of(1991, Month.NOVEMBER, 25);
    private static final String PASSWORD_DEFAULT = "Senha@123";

    private static final String CONTATO_DEFAULT = "Contato1";
    private static final String TELEFONE_DEFAULT = "44988776655";
    private static final String EMAIL_DEFAULT = "contato@email.com";

    private static final String NOVO_CONTATO_DEFAULT = "Contato2";
    private static final String NOVO_TELEFONE_DEFAULT = "44911223344";
    private static final String NOVO_EMAIL_DEFAULT = "contato2@email.com";

    private CriarPessoa criarPessoa;
    private AdicionarContatoPessoa adicionarContatoPessoa;
    private BuscarContatoPessoa buscarContatoPessoa;
    private UUID idPessoaDefault;

    @BeforeEach
    void setup() {
        ApplicationClock applicationClock = new ApplicationClockMock(TODAY_MOCK);
        PasswordEncoder passwordEncoder = new PasswordEncoderNop();
        PessoaRepository pessoaRepository = new PessoaRepositoryMemory();
        criarPessoa = new CriarPessoa(pessoaRepository, applicationClock, passwordEncoder);
        adicionarContatoPessoa = new AdicionarContatoPessoa(pessoaRepository);
        buscarContatoPessoa = new BuscarContatoPessoa(pessoaRepository);
        idPessoaDefault = criarPessoaDefaultParaAlteracoes();
    }

    @Test
    void deveAdicionarContatoAUmaPessoa() {
        AdicionarContatoPessoa.Output output = adicionarContatoPessoa.execute(idPessoaDefault, new AdicionarContatoPessoa.Input(
                NOVO_CONTATO_DEFAULT,
                NOVO_TELEFONE_DEFAULT,
                NOVO_EMAIL_DEFAULT
        ));

        assertThat(output).isNotNull();
        assertThat(output.contato()).isNotNull();
        assertThat(output.contato().id()).isNotNull();
        assertThat(output.contato().nome()).isEqualTo(NOVO_CONTATO_DEFAULT);
        assertThat(output.contato().telefone()).isEqualTo(NOVO_TELEFONE_DEFAULT);
        assertThat(output.contato().email()).isEqualTo(NOVO_EMAIL_DEFAULT);

        BuscarContatoPessoa.Output outputBuscar = buscarContatoPessoa.execute(idPessoaDefault, output.contato().id());
        assertThat(outputBuscar.contato().id()).isNotNull();
        assertThat(outputBuscar.contato().nome()).isEqualTo(NOVO_CONTATO_DEFAULT);
        assertThat(outputBuscar.contato().telefone()).isEqualTo(NOVO_TELEFONE_DEFAULT);
        assertThat(outputBuscar.contato().email()).isEqualTo(NOVO_EMAIL_DEFAULT);
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
