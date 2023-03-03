package com.diego.prandini.exerciciotecnicoelotech.exception;

public class ContatoNuloException extends RuntimeException {

    public ContatoNuloException() {
        super("Contato não pode ser nulo");
    }
}
