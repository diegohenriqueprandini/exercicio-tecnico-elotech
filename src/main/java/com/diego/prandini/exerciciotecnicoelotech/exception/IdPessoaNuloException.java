package com.diego.prandini.exerciciotecnicoelotech.exception;

public class IdPessoaNuloException extends RuntimeException {

    public IdPessoaNuloException() {
        super("Id da pessoa não pode ser nulo");
    }
}
