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
    public static class CriarBuilder {

        private final ApplicationClock applicationClock;

        private UUID id;
        private String nome;
        private String cpf;
        private LocalDate dataDeNascimento;

        public CriarBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public CriarBuilder nome(String nome) {
            this.nome = nome;
            return this;
        }

        public CriarBuilder cpf(String cpf) {
            this.cpf = cpf;
            return this;
        }

        public CriarBuilder dataDeNascimento(LocalDate dataDeNascimento) {
            this.dataDeNascimento = dataDeNascimento;
            return this;
        }

        public Pessoa build() {
            if (StringUtils.isBlank(this.nome))
                throw new PessoaNomeVazioException();

            if (StringUtils.isBlank(this.cpf))
                throw new PessoaCpfVazioException();

            Cpf cpf = new Cpf(this.cpf);
            DataDeNascimento dataDeNascimento = new DataDeNascimento(this.dataDeNascimento);
            if (dataDeNascimento.isFutura(this.applicationClock))
                throw new PessoaDataDeNascimentoFuturaException(this.dataDeNascimento);

            return new Pessoa(
                    id,
                    this.nome,
                    cpf,
                    dataDeNascimento
            );
        }
    }

    @RequiredArgsConstructor
    public static class AlterarBuilder {

        private final ApplicationClock applicationClock;
        private final Pessoa pessoa;

        private String nome;
        private String cpf;
        private LocalDate dataDeNascimento;

        public AlterarBuilder nome(String nome) {
            this.nome = nome;
            return this;
        }

        public AlterarBuilder cpf(String cpf) {
            this.cpf = cpf;
            return this;
        }

        public AlterarBuilder dataDeNascimento(LocalDate dataDeNascimento) {
            this.dataDeNascimento = dataDeNascimento;
            return this;
        }

        public Pessoa build() {
            if (StringUtils.isBlank(this.nome))
                throw new PessoaNomeVazioException();

            if (StringUtils.isBlank(this.cpf))
                throw new PessoaCpfVazioException();

            Cpf cpf = new Cpf(this.cpf);
            DataDeNascimento dataDeNascimento = new DataDeNascimento(this.dataDeNascimento);
            if (dataDeNascimento.isFutura(this.applicationClock))
                throw new PessoaDataDeNascimentoFuturaException(this.dataDeNascimento);

            this.pessoa.setNome(this.nome);
            this.pessoa.setCpf(cpf);
            this.pessoa.setDataDeNascimento(dataDeNascimento);
            return pessoa;
        }
    }
}
