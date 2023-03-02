package com.diego.prandini.exerciciotecnicoelotech.domain.entity;

import com.diego.prandini.exerciciotecnicoelotech.exception.PessoaCpfVazioException;
import com.diego.prandini.exerciciotecnicoelotech.exception.PessoaDataDeNascimentoFuturaException;
import com.diego.prandini.exerciciotecnicoelotech.exception.PessoaNomeVazioException;
import com.diego.prandini.exerciciotecnicoelotech.infra.system.ApplicationClock;
import com.diego.prandini.exerciciotecnicoelotech.utils.StringUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class Pessoa {

    private final UUID id;
    private String nome;
    private Cpf cpf;
    private DataDeNascimento dataDeNascimento;

    public static Pessoa fromData(Data data) {
        Cpf cpf = new Cpf(data.cpf);
        DataDeNascimento dataDeNascimento = new DataDeNascimento(data.dataDeNascimento);
        return new Pessoa(
                data.id,
                data.nome,
                cpf,
                dataDeNascimento
        );
    }

    private Pessoa(Builder builder) {
        if (StringUtils.isBlank(builder.nome))
            throw new PessoaNomeVazioException();

        if (StringUtils.isBlank(builder.cpf))
            throw new PessoaCpfVazioException();

        Cpf cpf = new Cpf(builder.cpf);
        DataDeNascimento dataDeNascimento = new DataDeNascimento(builder.dataDeNascimento);
        if (dataDeNascimento.isFutura(builder.applicationClock))
            throw new PessoaDataDeNascimentoFuturaException(builder.dataDeNascimento);

        this.id = builder.id;
        this.nome = builder.nome;
        this.cpf = cpf;
        this.dataDeNascimento = dataDeNascimento;
    }

    private Pessoa(UUID id, String nome, Cpf cpf, DataDeNascimento dataDeNascimento) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.dataDeNascimento = dataDeNascimento;
    }

    public Data toData() {
        return new Data(
                this.id,
                this.nome,
                this.cpf.toString(),
                this.dataDeNascimento.getDate()
        );
    }

    public record Data(
            UUID id,
            String nome,
            String cpf,
            LocalDate dataDeNascimento
    ) {
    }

    @RequiredArgsConstructor
    public static class Builder {

        private final ApplicationClock applicationClock;

        private UUID id;
        private String nome;
        private String cpf;
        private LocalDate dataDeNascimento;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder nome(String nome) {
            this.nome = nome;
            return this;
        }

        public Builder cpf(String cpf) {
            this.cpf = cpf;
            return this;
        }

        public Builder dataDeNascimento(LocalDate dataDeNascimento) {
            this.dataDeNascimento = dataDeNascimento;
            return this;
        }

        public Pessoa build() {
            return new Pessoa(this);
        }
    }
}
