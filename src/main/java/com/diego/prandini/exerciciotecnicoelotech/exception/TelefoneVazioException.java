package com.diego.prandini.exerciciotecnicoelotech.exception;

public class TelefoneVazioException extends RuntimeException {

    public TelefoneVazioException() {
        super("Telefone não pode ser vazio");
    }
}
