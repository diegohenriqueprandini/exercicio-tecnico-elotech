package com.diego.prandini.exerciciotecnicoelotech.api;

import com.diego.prandini.exerciciotecnicoelotech.api.support.MockMvcHandler;
import com.diego.prandini.exerciciotecnicoelotech.application.CriarPessoa;
import com.diego.prandini.exerciciotecnicoelotech.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class CriarPessoaApiTest {

    private static final String NOME_DEFAULT = "Joao";
    private static final String CPF_DEFAULT = "37783132669";
    private static final LocalDate DATA_DE_NASCIMENTO_DEFAULT = LocalDate.of(1991, Month.NOVEMBER, 25);

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveCriarPessoa() throws Exception {
        CriarPessoa.Output output = doPostPessoa();

        assertThat(output).isNotNull();
        assertThat(output.id()).isNotNull();
        assertThat(output.nome()).isEqualTo(NOME_DEFAULT);
        assertThat(output.cpf()).isEqualTo(CPF_DEFAULT);
        assertThat(output.dataDeNascimento()).isEqualTo(DATA_DE_NASCIMENTO_DEFAULT);
    }

    private CriarPessoa.Output doPostPessoa() throws Exception {
        MockMvcHandler<CriarPessoa.Output> handler = new MockMvcHandler<>(new TypeReference<>() {
        });
        String input = JsonUtils.toJson(new CriarPessoa.Input(
                NOME_DEFAULT,
                CPF_DEFAULT,
                DATA_DE_NASCIMENTO_DEFAULT
        ));
        mockMvc.perform(post("/pessoas").contentType(MediaType.APPLICATION_JSON).content(input))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(redirectedUrlPattern("/pessoas/*"))
                .andDo(handler);
        return handler.get();
    }
}
