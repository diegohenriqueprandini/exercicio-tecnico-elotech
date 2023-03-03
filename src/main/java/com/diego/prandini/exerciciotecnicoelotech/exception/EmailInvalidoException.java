package com.diego.prandini.exerciciotecnicoelotech.exception;

public class EmailInvalidoException extends RuntimeException {

    public EmailInvalidoException(String email) {
        super(String.format("E-mail inválido: '%s'", email));
    }
}
