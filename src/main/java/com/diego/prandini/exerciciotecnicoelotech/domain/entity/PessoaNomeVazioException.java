package com.diego.prandini.exerciciotecnicoelotech.domain.entity;

public class PessoaNomeVazioException extends RuntimeException {

    public PessoaNomeVazioException() {
        super("Nome da pessoa não pode ser vazio");
    }
}
