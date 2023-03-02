package com.diego.prandini.exerciciotecnicoelotech.api;

import com.diego.prandini.exerciciotecnicoelotech.api.support.MockMvcHandler;
import com.diego.prandini.exerciciotecnicoelotech.application.AlterarPessoa;
import com.diego.prandini.exerciciotecnicoelotech.domain.entity.Pessoa;
import com.diego.prandini.exerciciotecnicoelotech.domain.repository.PessoaRepository;
import com.diego.prandini.exerciciotecnicoelotech.infra.system.ApplicationClock;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class AlterarPessoaApiTest {

    private static final String OLD_NOME_DEFAULT = "Old Joao";
    private static final String OLD_CPF_DEFAULT = "37783132669";
    private static final LocalDate OLD_DATA_DE_NASCIMENTO_DEFAULT = LocalDate.of(1991, Month.NOVEMBER, 25);

    private static final UUID ID_DEFAULT = UUID.randomUUID();
    private static final String NOVO_NOME_DEFAULT = "Novo Joao";
    private static final String NOVO_CPF_DEFAULT = "25853460218";
    private static final LocalDate NOVO_DATA_DE_NASCIMENTO_DEFAULT = LocalDate.of(1995, Month.DECEMBER, 15);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private ApplicationClock applicationClock;

    @Test
    void deveAlterarPessoa() throws Exception {
        pessoaRepository.save(Pessoa.of(
                ID_DEFAULT,
                OLD_NOME_DEFAULT,
                OLD_CPF_DEFAULT,
                OLD_DATA_DE_NASCIMENTO_DEFAULT,
                applicationClock
        ));

        AlterarPessoa.Output output = doPutPessoa();

        assertThat(output).isNotNull();
        assertThat(output.id()).isNotNull();
        assertThat(output.nome()).isEqualTo(NOVO_NOME_DEFAULT);
        assertThat(output.cpf()).isEqualTo(NOVO_CPF_DEFAULT);
        assertThat(output.dataDeNascimento()).isEqualTo(NOVO_DATA_DE_NASCIMENTO_DEFAULT);
    }

    private AlterarPessoa.Output doPutPessoa() throws Exception {
        MockMvcHandler<AlterarPessoa.Output> handler = new MockMvcHandler<>(new TypeReference<>() {
        });
        String input = JsonUtils.toJson(new AlterarPessoa.Input(
                NOVO_NOME_DEFAULT,
                NOVO_CPF_DEFAULT,
                NOVO_DATA_DE_NASCIMENTO_DEFAULT
        ));
        mockMvc.perform(put("/pessoas/{id}", ID_DEFAULT).contentType(MediaType.APPLICATION_JSON).content(input))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(handler);
        return handler.get();
    }
}
