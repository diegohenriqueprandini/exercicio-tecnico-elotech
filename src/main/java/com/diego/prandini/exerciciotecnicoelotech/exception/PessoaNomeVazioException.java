package com.diego.prandini.exerciciotecnicoelotech.exception;

public class PessoaNomeVazioException extends RuntimeException {

    public PessoaNomeVazioException() {
        super("Nome da pessoa não pode ser vazio");
    }
}
