package com.diego.prandini.exerciciotecnicoelotech.api;

import com.diego.prandini.exerciciotecnicoelotech.api.support.MockMvcPessoa;
import com.diego.prandini.exerciciotecnicoelotech.application.AlterarPessoa;
import com.diego.prandini.exerciciotecnicoelotech.application.BuscarPessoa;
import com.diego.prandini.exerciciotecnicoelotech.application.CriarPessoa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class AlterarPessoaApiTest {

    private static final String NOME_DEFAULT = "Joao";
    private static final String CPF_DEFAULT = "37783132669";
    private static final LocalDate DATA_DE_NASCIMENTO_DEFAULT = LocalDate.of(1991, Month.NOVEMBER, 25);

    private static final String NOVO_NOME_DEFAULT = "Novo Joao";
    private static final String NOVO_CPF_DEFAULT = "25853460218";
    private static final LocalDate NOVO_DATA_DE_NASCIMENTO_DEFAULT = LocalDate.of(1995, Month.DECEMBER, 15);

    private static final String CONTATO_DEFAULT = "Contato1";
    private static final String TELEFONE_DEFAULT = "44988776655";
    private static final String EMAIL_DEFAULT = "contato@email.com";

    @Autowired
    private MockMvcPessoa mockMvcPessoa;

    private UUID idPessoaDefault;

    @BeforeEach
    void setup() throws Exception {
        idPessoaDefault = criarPessoaParaAlteracoes();
    }

    @Test
    void deveAlterarPessoa() throws Exception {
        AlterarPessoa.Output output = mockMvcPessoa.doPutPessoa(idPessoaDefault, new AlterarPessoa.Input(
                NOVO_NOME_DEFAULT,
                NOVO_CPF_DEFAULT,
                NOVO_DATA_DE_NASCIMENTO_DEFAULT
        ));

        assertThat(output).isNotNull();
        assertThat(output.id()).isNotNull();
        assertThat(output.nome()).isEqualTo(NOVO_NOME_DEFAULT);
        assertThat(output.cpf()).isEqualTo(NOVO_CPF_DEFAULT);
        assertThat(output.dataDeNascimento()).isEqualTo(NOVO_DATA_DE_NASCIMENTO_DEFAULT);

        BuscarPessoa.Output outputBuscar = mockMvcPessoa.doGetPessoa(idPessoaDefault);
        assertThat(outputBuscar).isNotNull();
        assertThat(outputBuscar.id()).isEqualTo(idPessoaDefault);
        assertThat(outputBuscar.nome()).isEqualTo(NOVO_NOME_DEFAULT);
        assertThat(outputBuscar.cpf()).isEqualTo(NOVO_CPF_DEFAULT);
        assertThat(outputBuscar.dataDeNascimento()).isEqualTo(NOVO_DATA_DE_NASCIMENTO_DEFAULT);
    }

    private UUID criarPessoaParaAlteracoes() throws Exception {
        CriarPessoa.Output output = mockMvcPessoa.doPostPessoa(new CriarPessoa.Input(
                NOME_DEFAULT,
                CPF_DEFAULT,
                DATA_DE_NASCIMENTO_DEFAULT,
                List.of(new CriarPessoa.ContatoInput(
                        CONTATO_DEFAULT,
                        TELEFONE_DEFAULT,
                        EMAIL_DEFAULT

                ))
        ));
        assertThat(output).isNotNull();
        return output.id();
    }
}
