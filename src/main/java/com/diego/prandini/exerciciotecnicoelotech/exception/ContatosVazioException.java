package com.diego.prandini.exerciciotecnicoelotech.exception;

public class ContatosVazioException extends RuntimeException {

    public ContatosVazioException() {
        super("Pessoa deve possuir ao menos um contato");
    }
}
