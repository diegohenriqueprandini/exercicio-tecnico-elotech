package com.diego.prandini.exerciciotecnicoelotech.domain.entity;

public class PessoaCpfVazioException extends RuntimeException {

    public PessoaCpfVazioException() {
        super("Cpf da pessoa não pode ser vazio");
    }
}
