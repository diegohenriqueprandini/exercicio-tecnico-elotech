package com.diego.prandini.exerciciotecnicoelotech.exception;

public class CpfVazioException extends RuntimeException {

    public CpfVazioException() {
        super("Cpf não pode ser vazio");
    }
}
