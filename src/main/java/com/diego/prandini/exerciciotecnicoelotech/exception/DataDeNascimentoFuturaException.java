package com.diego.prandini.exerciciotecnicoelotech.exception;

import com.diego.prandini.exerciciotecnicoelotech.utils.DateUtils;

import java.time.LocalDate;

public class DataDeNascimentoFuturaException extends RuntimeException {

    public DataDeNascimentoFuturaException(LocalDate dataDeNascimento) {
        super("Data de nascimento não pode ser futura: " + DateUtils.toString(dataDeNascimento));
    }
}
