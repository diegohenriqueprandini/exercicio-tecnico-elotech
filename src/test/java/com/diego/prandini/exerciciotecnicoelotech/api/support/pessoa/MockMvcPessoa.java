package com.diego.prandini.exerciciotecnicoelotech.api.support.pessoa;

import com.diego.prandini.exerciciotecnicoelotech.api.support.MockMvcHandler;
import com.diego.prandini.exerciciotecnicoelotech.application.usecase.pessoa.AlterarPessoa;
import com.diego.prandini.exerciciotecnicoelotech.application.usecase.pessoa.BuscarPessoa;
import com.diego.prandini.exerciciotecnicoelotech.application.usecase.pessoa.CriarPessoa;
import com.diego.prandini.exerciciotecnicoelotech.infra.controller.ControllerErrorData;
import com.diego.prandini.exerciciotecnicoelotech.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
public class MockMvcPessoa {

    @Autowired
    private MockMvc mockMvc;

    public BuscarPessoa.Output doGetPessoa(UUID id) throws Exception {
        MockMvcHandler<BuscarPessoa.Output> handler = new MockMvcHandler<>(new TypeReference<>() {
        });
        mockMvc.perform(get("/pessoas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(handler);
        return handler.get();
    }

    public ControllerErrorData doGetPessoaError(UUID id, ResultMatcher status) throws Exception {
        MockMvcHandler<ControllerErrorData> handler = new MockMvcHandler<>(new TypeReference<>() {
        });
        mockMvc.perform(get("/pessoas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status)
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(handler);
        return handler.get();
    }

    public CriarPessoa.Output doPostPessoa(CriarPessoa.Input input) throws Exception {
        MockMvcHandler<CriarPessoa.Output> handler = new MockMvcHandler<>(new TypeReference<>() {
        });
        mockMvc.perform(post("/pessoas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.toJson(input)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(redirectedUrlPattern("/pessoas/*"))
                .andDo(handler);
        return handler.get();
    }

    public AlterarPessoa.Output doPutPessoa(UUID id, AlterarPessoa.Input input) throws Exception {
        MockMvcHandler<AlterarPessoa.Output> handler = new MockMvcHandler<>(new TypeReference<>() {
        });
        mockMvc.perform(put("/pessoas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.toJson(input)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(handler);
        return handler.get();
    }

    public void doDeletePessoa(UUID id) throws Exception {
        mockMvc.perform(delete("/pessoas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$").doesNotExist());
    }

    public UUID criarPessoaParaAlteracoes(String nome, String cpf, LocalDate dataDeNascimento, String password, String nomeContato, String telefone, String email) throws Exception {
        CriarPessoa.Output output = doPostPessoa(new CriarPessoa.Input(
                nome,
                cpf,
                dataDeNascimento,
                password,
                List.of(new CriarPessoa.ContatoInput(
                        nomeContato,
                        telefone,
                        email
                ))
        ));
        assertThat(output).isNotNull();
        return output.id();
    }
}
