package com.diego.prandini.exerciciotecnicoelotech.exception;

public class PessoaDataDeNascimentoVaziaException extends RuntimeException {

    public PessoaDataDeNascimentoVaziaException() {
        super("Data de nascimento da pessoa não pode ser vazia");
    }
}
