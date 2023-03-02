package com.diego.prandini.exerciciotecnicoelotech.api;

import com.diego.prandini.exerciciotecnicoelotech.api.support.MockMvcHandler;
import com.diego.prandini.exerciciotecnicoelotech.application.BuscarPessoa;
import com.diego.prandini.exerciciotecnicoelotech.domain.entity.Pessoa;
import com.diego.prandini.exerciciotecnicoelotech.domain.repository.PessoaRepository;
import com.diego.prandini.exerciciotecnicoelotech.infra.system.ApplicationClock;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class BuscarPessoaApiTest {

    private static final UUID ID_DEFAULT = UUID.randomUUID();
    private static final String NOME_DEFAULT = "Joao";
    private static final String CPF_DEFAULT = "37783132669";
    private static final LocalDate DATA_DE_NASCIMENTO_DEFAULT = LocalDate.of(1991, Month.NOVEMBER, 25);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private ApplicationClock applicationClock;

    @Test
    void deveBuscarPessoaPeloId() throws Exception {
        pessoaRepository.save(Pessoa.of(
                ID_DEFAULT,
                NOME_DEFAULT,
                CPF_DEFAULT,
                DATA_DE_NASCIMENTO_DEFAULT,
                applicationClock
        ));

        BuscarPessoa.Output output = doGetPessoa();

        assertThat(output).isNotNull();
        assertThat(output.id()).isNotNull();
        assertThat(output.nome()).isEqualTo(NOME_DEFAULT);
        assertThat(output.cpf()).isEqualTo(CPF_DEFAULT);
        assertThat(output.dataDeNascimento()).isEqualTo(DATA_DE_NASCIMENTO_DEFAULT);
    }

    private BuscarPessoa.Output doGetPessoa() throws Exception {
        MockMvcHandler<BuscarPessoa.Output> handler = new MockMvcHandler<>(new TypeReference<>() {
        });
        mockMvc.perform(get("/pessoas/{id}", ID_DEFAULT).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(handler);
        return handler.get();
    }
}
