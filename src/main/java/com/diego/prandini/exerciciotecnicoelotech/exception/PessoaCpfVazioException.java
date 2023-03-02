package com.diego.prandini.exerciciotecnicoelotech.exception;

public class PessoaCpfVazioException extends RuntimeException {

    public PessoaCpfVazioException() {
        super("Cpf da pessoa não pode ser vazio");
    }
}
