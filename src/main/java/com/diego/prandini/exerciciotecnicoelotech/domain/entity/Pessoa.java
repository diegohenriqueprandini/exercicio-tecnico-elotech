package com.diego.prandini.exerciciotecnicoelotech.domain.entity;

import com.diego.prandini.exerciciotecnicoelotech.infra.ApplicationClock;
import com.diego.prandini.exerciciotecnicoelotech.utils.CpfValidador;
import com.diego.prandini.exerciciotecnicoelotech.utils.StringUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

import static com.diego.prandini.exerciciotecnicoelotech.utils.CpfValidador.cpfSemMascara;

@Data
public class Pessoa {

    private final UUID id;
    private String nome;
    private String cpf;
    private LocalDate dataDeNascimento;

    public static Pessoa fromData(Data data) {
        return new Pessoa(
                data.id,
                data.nome,
                data.cpf,
                data.dataDeNascimento
        );
    }

    public Data toData() {
        return new Data(
                this.id,
                this.nome,
                this.cpf,
                this.dataDeNascimento
        );
    }

    private Pessoa(Builder builder) {
        this.id = builder.id;
        this.nome = builder.nome;
        this.cpf = builder.cpf;
        this.dataDeNascimento = builder.dataDeNascimento;
    }

    private Pessoa(UUID id, String nome, String cpf, LocalDate dataDeNascimento) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.dataDeNascimento = dataDeNascimento;
    }

    public record Data(UUID id, String nome, String cpf, LocalDate dataDeNascimento) {
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
            if (StringUtils.isBlank(nome))
                throw new PessoaNomeEmptyException();

            if (StringUtils.isBlank(cpf))
                throw new PessoaCpfEmptyException();

            if (!CpfValidador.isCpfValido(cpf))
                throw new PessoaCpfInvalidoException(cpf);

            if (dataDeNascimento.isAfter(applicationClock.today()))
                throw new PessoaDataDeNascimentoFuturaException(dataDeNascimento);

            cpf = cpfSemMascara(cpf);
            return new Pessoa(this);
        }

    }
}
