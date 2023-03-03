package com.diego.prandini.exerciciotecnicoelotech.exception;

public class CpfJaExisteException extends RuntimeException {

    public CpfJaExisteException(String cpf) {
        super("Cpf já existe: " + cpf);
    }
}
