package com.diego.prandini.exerciciotecnicoelotech.exception;

public class TelefoneInvalidoException extends RuntimeException {

    public TelefoneInvalidoException(String telefone) {
        super(String.format("Telefone inválido: '%s'", telefone));
    }
}
