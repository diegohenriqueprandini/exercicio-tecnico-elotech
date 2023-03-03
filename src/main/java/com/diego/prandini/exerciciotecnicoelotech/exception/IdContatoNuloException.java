package com.diego.prandini.exerciciotecnicoelotech.exception;

public class IdContatoNuloException extends RuntimeException {

    public IdContatoNuloException() {
        super("Id do contato não pode ser nulo");
    }
}
