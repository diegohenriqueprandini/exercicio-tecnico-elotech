package com.diego.prandini.exerciciotecnicoelotech.api.database.pessoa;

import com.diego.prandini.exerciciotecnicoelotech.api.support.pessoa.MockMvcPessoa;
import com.diego.prandini.exerciciotecnicoelotech.application.pessoa.BuscarPessoa;
import com.diego.prandini.exerciciotecnicoelotech.application.pessoa.CriarPessoa;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
@ActiveProfiles("dbtest")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class CriarPessoaApiTest {

    private static final String NOME_DEFAULT = "Joao";
    private static final String CPF_DEFAULT = "37783132669";
    private static final LocalDate DATA_DE_NASCIMENTO_DEFAULT = LocalDate.of(1991, Month.NOVEMBER, 25);
    private static final String PASSWORD_DEFAULT = "Senha@123";

    private static final String CONTATO_DEFAULT = "Contato1";
    private static final String TELEFONE_DEFAULT = "44988776655";
    private static final String EMAIL_DEFAULT = "contato@email.com";

    @Autowired
    private MockMvcPessoa mockMvcPessoa;

    @Test
    void deveCriarPessoa() throws Exception {
        CriarPessoa.Output output = mockMvcPessoa.doPostPessoa(new CriarPessoa.Input(
                NOME_DEFAULT,
                CPF_DEFAULT,
                DATA_DE_NASCIMENTO_DEFAULT,
                PASSWORD_DEFAULT,
                List.of(new CriarPessoa.ContatoInput(
                        CONTATO_DEFAULT,
                        TELEFONE_DEFAULT,
                        EMAIL_DEFAULT
                ))
        ));

        assertThat(output).isNotNull();
        assertThat(output.id()).isNotNull();
        assertThat(output.nome()).isEqualTo(NOME_DEFAULT);
        assertThat(output.cpf()).isEqualTo(CPF_DEFAULT);
        assertThat(output.dataDeNascimento()).isEqualTo(DATA_DE_NASCIMENTO_DEFAULT);

        BuscarPessoa.Output outputBuscar = mockMvcPessoa.doGetPessoa(output.id());
        assertThat(outputBuscar).isNotNull();
        assertThat(outputBuscar.id()).isEqualTo(output.id());
        assertThat(outputBuscar.nome()).isEqualTo(NOME_DEFAULT);
        assertThat(outputBuscar.cpf()).isEqualTo(CPF_DEFAULT);
        assertThat(outputBuscar.dataDeNascimento()).isEqualTo(DATA_DE_NASCIMENTO_DEFAULT);
    }
}
