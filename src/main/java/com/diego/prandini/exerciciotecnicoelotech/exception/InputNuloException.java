package com.diego.prandini.exerciciotecnicoelotech.exception;

public class InputNuloException extends RuntimeException {

    public InputNuloException() {
        super("Input não pode ser nulo");
    }
}
