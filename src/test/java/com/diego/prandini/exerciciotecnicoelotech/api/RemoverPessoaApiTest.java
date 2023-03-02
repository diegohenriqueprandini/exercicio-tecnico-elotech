package com.diego.prandini.exerciciotecnicoelotech.api;

import com.diego.prandini.exerciciotecnicoelotech.domain.entity.Pessoa;
import com.diego.prandini.exerciciotecnicoelotech.domain.repository.PessoaRepository;
import com.diego.prandini.exerciciotecnicoelotech.exception.PessoaNotFoundException;
import com.diego.prandini.exerciciotecnicoelotech.infra.system.ApplicationClock;
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
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class RemoverPessoaApiTest {

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
    void deveRemoverPessoaPeloId() throws Exception {
        pessoaRepository.save(Pessoa.of(
                ID_DEFAULT,
                NOME_DEFAULT,
                CPF_DEFAULT,
                DATA_DE_NASCIMENTO_DEFAULT,
                applicationClock
        ));

        doDeletePessoa();

        Throwable throwable = catchThrowable(() -> pessoaRepository.findById(ID_DEFAULT));
        assertThat(throwable).isInstanceOf(PessoaNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("Pessoa não encontrada: " + ID_DEFAULT);
    }

    private void doDeletePessoa() throws Exception {
        mockMvc.perform(delete("/pessoas/{id}", ID_DEFAULT).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$").doesNotExist());
    }
}
