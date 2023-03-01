package com.diego.prandini.exerciciotecnicoelotech.domain.entity;

public class DataDeNascimentoVaziaException extends RuntimeException {

    public DataDeNascimentoVaziaException() {
        super("Data de Nascimento não pode ser vazia");
    }
}
