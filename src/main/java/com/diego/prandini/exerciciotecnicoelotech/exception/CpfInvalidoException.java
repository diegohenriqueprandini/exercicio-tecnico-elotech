package com.diego.prandini.exerciciotecnicoelotech.exception;

public class CpfInvalidoException extends RuntimeException {

    public CpfInvalidoException(String cpf) {
        super(String.format("Cpf inválido: '%s'", cpf));
    }
}
