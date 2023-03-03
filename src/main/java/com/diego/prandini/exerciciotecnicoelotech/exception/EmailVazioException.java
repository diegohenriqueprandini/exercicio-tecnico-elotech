package com.diego.prandini.exerciciotecnicoelotech.exception;

public class EmailVazioException extends RuntimeException {

    public EmailVazioException() {
        super("E-mail não pode ser vazio");
    }
}
