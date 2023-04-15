package com.diego.prandini.exerciciotecnicoelotech.exception;

public class PasswordVazioException extends RuntimeException {

    public PasswordVazioException() {
        super("Password não pode ser vazio");
    }
}
