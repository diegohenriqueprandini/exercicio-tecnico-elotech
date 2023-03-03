package com.diego.prandini.exerciciotecnicoelotech.exception;

public class ContatosVazioException extends RuntimeException {

    public ContatosVazioException() {
        super("Deve possuir ao menos um contato");
    }
}
