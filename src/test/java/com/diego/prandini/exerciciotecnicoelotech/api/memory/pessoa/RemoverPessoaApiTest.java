package com.diego.prandini.exerciciotecnicoelotech.api.memory.pessoa;

import com.diego.prandini.exerciciotecnicoelotech.api.support.pessoa.MockMvcPessoa;
import com.diego.prandini.exerciciotecnicoelotech.infra.controller.ControllerErrorData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.LocalDate;
import java.time.Month;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class RemoverPessoaApiTest {

    private static final String NOME_DEFAULT = "Joao";
    private static final String CPF_DEFAULT = "37783132669";
    private static final LocalDate DATA_DE_NASCIMENTO_DEFAULT = LocalDate.of(1991, Month.NOVEMBER, 25);

    private static final String CONTATO_DEFAULT = "Contato1";
    private static final String TELEFONE_DEFAULT = "44988776655";
    private static final String EMAIL_DEFAULT = "contato@email.com";

    @Autowired
    private MockMvcPessoa mockMvcPessoa;

    private UUID idPessoaDefault;

    @BeforeEach
    void setup() throws Exception {
        idPessoaDefault = mockMvcPessoa.criarPessoaParaAlteracoes(
                NOME_DEFAULT,
                CPF_DEFAULT,
                DATA_DE_NASCIMENTO_DEFAULT,
                CONTATO_DEFAULT,
                TELEFONE_DEFAULT,
                EMAIL_DEFAULT
        );
    }

    @Test
    void deveRemoverPessoaPeloId() throws Exception {
        mockMvcPessoa.doDeletePessoa(idPessoaDefault);

        ResultMatcher notFound = status().isNotFound();
        ControllerErrorData output = mockMvcPessoa.doGetPessoaError(idPessoaDefault, notFound);
        assertThat(output).isNotNull();
        assertThat(output.getTimestamp()).isNotNull();
        assertThat(output.getMessage()).isEqualTo("Pessoa não encontrada: " + idPessoaDefault);
        assertThat(output.getDetail()).isEqualTo("method=GET,uri=/pessoas/" + idPessoaDefault);
    }
}
