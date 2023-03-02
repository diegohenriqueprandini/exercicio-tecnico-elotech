package com.diego.prandini.exerciciotecnicoelotech.exception;

public class DataDeNascimentoVaziaException extends RuntimeException {

    public DataDeNascimentoVaziaException() {
        super("Data de Nascimento não pode ser vazia");
    }
}
