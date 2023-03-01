package com.diego.prandini.exerciciotecnicoelotech.domain.entity;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class Pessoa {

    private final UUID id;
    private String nome;
    private String cpf;
    private LocalDate dataDeNascimento;

    public Pessoa(UUID id, String nome, String cpf, LocalDate dataDeNascimento) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.dataDeNascimento = dataDeNascimento;
    }

    public static Pessoa fromData(PessoaData data) {
        return new Pessoa(
                data.id,
                data.nome,
                data.cpf,
                data.dataDeNascimento
        );
    }

    public PessoaData toData() {
        return new PessoaData(
                this.id,
                this.nome,
                this.cpf,
                this.dataDeNascimento
        );
    }

    public record PessoaData(UUID id, String nome, String cpf, LocalDate dataDeNascimento) {
    }
}
