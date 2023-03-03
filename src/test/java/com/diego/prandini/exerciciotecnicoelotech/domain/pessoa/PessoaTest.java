package com.diego.prandini.exerciciotecnicoelotech.domain.pessoa;

import com.diego.prandini.exerciciotecnicoelotech.domain.entity.Contato;
import com.diego.prandini.exerciciotecnicoelotech.domain.entity.Cpf;
import com.diego.prandini.exerciciotecnicoelotech.domain.entity.DataDeNascimento;
import com.diego.prandini.exerciciotecnicoelotech.domain.entity.Pessoa;
import com.diego.prandini.exerciciotecnicoelotech.exception.ContatosVazioException;
import com.diego.prandini.exerciciotecnicoelotech.infra.system.clock.ApplicationClock;
import com.diego.prandini.exerciciotecnicoelotech.infra.system.clock.ApplicationClockMock;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class PessoaTest {

    private static final LocalDate TODAY_MOCK = LocalDate.of(2023, Month.MARCH, 1);

    private static final String NOME_DEFAULT = "Joao";
    private static final String CPF_DEFAULT = "37783132669";
    private static final LocalDate DATA_DE_NASCIMENTO_DEFAULT = LocalDate.of(1991, Month.NOVEMBER, 25);

    private static final String CONTATO_DEFAULT = "Contato1";
    private static final String TELEFONE_DEFAULT = "44988776655";
    private static final String EMAIL_DEFAULT = "contato@email.com";

    @Test
    void deveCriarUmaPessoaComUmContato() {
        ApplicationClock applicationClock = new ApplicationClockMock(TODAY_MOCK);
        Pessoa.Builder builder = new Pessoa.Builder(
                UUID.randomUUID(),
                NOME_DEFAULT,
                new Cpf(CPF_DEFAULT),
                new DataDeNascimento(DATA_DE_NASCIMENTO_DEFAULT, applicationClock)
        );
        builder.contatos(new Contato(
                UUID.randomUUID(),
                CONTATO_DEFAULT,
                TELEFONE_DEFAULT,
                EMAIL_DEFAULT
        ));
        Pessoa pessoa = builder.build();

        assertThat(pessoa).isNotNull();
        assertThat(pessoa.getId()).isNotNull();
        assertThat(pessoa.getNome()).isEqualTo(NOME_DEFAULT);
        assertThat(pessoa.getCpf()).isEqualTo(new Cpf(CPF_DEFAULT));
        assertThat(pessoa.getDataDeNascimento()).isEqualTo(new DataDeNascimento(DATA_DE_NASCIMENTO_DEFAULT, applicationClock));
        assertThat(pessoa.getContatos()).isNotNull();
        assertThat(pessoa.getContatos().size()).isEqualTo(1);
        assertThat(pessoa.getContatos().get(0)).isNotNull();
        assertThat(pessoa.getContatos().get(0).getNome()).isEqualTo(CONTATO_DEFAULT);
        assertThat(pessoa.getContatos().get(0).getTelefone()).isEqualTo(TELEFONE_DEFAULT);
        assertThat(pessoa.getContatos().get(0).getEmail()).isEqualTo(EMAIL_DEFAULT);
    }

    @Test
    void pessoaDeveTerPeloMenosUmContato() {
        ApplicationClock applicationClock = new ApplicationClockMock(TODAY_MOCK);
        UUID idPessoa = UUID.randomUUID();
        Throwable throwable = catchThrowable(() -> new Pessoa.Builder(
                idPessoa,
                NOME_DEFAULT,
                new Cpf(CPF_DEFAULT),
                new DataDeNascimento(DATA_DE_NASCIMENTO_DEFAULT, applicationClock)
        ).build());

        assertThat(throwable).isInstanceOf(ContatosVazioException.class);
        assertThat(throwable.getMessage()).isEqualTo("Deve possuir ao menos um contato");
    }

    @Test
    void naoPodeRemoverTodosOsContatosDaPessoa() {
        ApplicationClock applicationClock = new ApplicationClockMock(TODAY_MOCK);
        UUID idPessoa = UUID.randomUUID();
        Pessoa.Builder builder = new Pessoa.Builder(
                idPessoa,
                NOME_DEFAULT,
                new Cpf(CPF_DEFAULT),
                new DataDeNascimento(DATA_DE_NASCIMENTO_DEFAULT, applicationClock)
        );
        UUID idContato = UUID.randomUUID();
        builder.contatos(new Contato(
                idContato,
                CONTATO_DEFAULT,
                TELEFONE_DEFAULT,
                EMAIL_DEFAULT
        ));
        Pessoa pessoa = builder.build();

        Throwable throwable = catchThrowable(() -> pessoa.removerContato(idContato));

        assertThat(throwable).isInstanceOf(ContatosVazioException.class);
        assertThat(throwable.getMessage()).isEqualTo("Deve possuir ao menos um contato");
    }

    @Test
    void deveAlterarUmContato() {
        ApplicationClock applicationClock = new ApplicationClockMock(TODAY_MOCK);
        Pessoa.Builder builder = new Pessoa.Builder(
                UUID.randomUUID(),
                NOME_DEFAULT,
                new Cpf(CPF_DEFAULT),
                new DataDeNascimento(DATA_DE_NASCIMENTO_DEFAULT, applicationClock)
        );
        UUID idContato = UUID.randomUUID();
        builder.contatos(new Contato(
                idContato,
                CONTATO_DEFAULT,
                TELEFONE_DEFAULT,
                EMAIL_DEFAULT
        ));
        Pessoa pessoa = builder.build();
        Contato contato = new Contato(
                idContato,
                "Contato alterado",
                "44911223344",
                "email.alterado@email.com"
        );

        pessoa.alterarContato(contato);

        assertThat(pessoa.getContatos()).isNotNull();
        assertThat(pessoa.getContatos().size()).isEqualTo(1);
        assertThat(pessoa.getContatos().get(0)).isNotNull();
        assertThat(pessoa.getContatos().get(0).getNome()).isEqualTo("Contato alterado");
        assertThat(pessoa.getContatos().get(0).getTelefone()).isEqualTo("44911223344");
        assertThat(pessoa.getContatos().get(0).getEmail()).isEqualTo("email.alterado@email.com");
    }
}