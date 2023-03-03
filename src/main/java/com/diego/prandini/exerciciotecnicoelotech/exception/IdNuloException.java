package com.diego.prandini.exerciciotecnicoelotech.exception;

public class IdNuloException extends RuntimeException {

    public IdNuloException() {
        super("Id não pode ser nulo");
    }
}
