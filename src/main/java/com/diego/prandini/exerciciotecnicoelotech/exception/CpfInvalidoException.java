package com.diego.prandini.exerciciotecnicoelotech.exception;

public class CpfInvalidoException extends RuntimeException {

    public CpfInvalidoException(String cpf) {
        super("Cpf inválido: " + cpf);
    }
}
