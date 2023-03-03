package com.diego.prandini.exerciciotecnicoelotech.exception;

public class NomeVazioException extends RuntimeException {

    public NomeVazioException() {
        super("Nome não pode ser vazio");
    }
}
