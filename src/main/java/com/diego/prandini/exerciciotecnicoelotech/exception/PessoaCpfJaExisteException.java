package com.diego.prandini.exerciciotecnicoelotech.exception;

public class PessoaCpfJaExisteException extends RuntimeException {

    public PessoaCpfJaExisteException(String cpf) {
        super("Cpf já existe: " + cpf);
    }
}
