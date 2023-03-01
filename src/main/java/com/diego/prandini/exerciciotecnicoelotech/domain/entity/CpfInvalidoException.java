package com.diego.prandini.exerciciotecnicoelotech.domain.entity;

public class CpfInvalidoException extends RuntimeException {

    public CpfInvalidoException(String cpf) {
        super("Cpf inválido: " + cpf);
    }
}