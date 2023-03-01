package com.diego.prandini.exerciciotecnicoelotech.domain.entity;

public class PessoaCpfInvalidoException extends RuntimeException {
    public PessoaCpfInvalidoException(String cpf) {
        super("CPF invalid: " + cpf);
    }
}
