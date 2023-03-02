package com.diego.prandini.exerciciotecnicoelotech.exception;

import com.diego.prandini.exerciciotecnicoelotech.utils.DateUtils;

import java.time.LocalDate;

public class PessoaDataDeNascimentoFuturaException extends RuntimeException {

    public PessoaDataDeNascimentoFuturaException(LocalDate dataDeNascimento) {
        super("Data de Nascimento não pode ser futura: " + DateUtils.toString(dataDeNascimento));
    }
}
