package com.diego.prandini.exerciciotecnicoelotech.domain.entity;

import com.diego.prandini.exerciciotecnicoelotech.utils.DateUtils;

import java.time.LocalDate;

public class PessoaDataDeNascimentoFuturaException extends RuntimeException {

    public PessoaDataDeNascimentoFuturaException(LocalDate dataDeNascimento) {
        super("Data de Nascimento cannot be future: " + DateUtils.toString(dataDeNascimento));
    }
}
