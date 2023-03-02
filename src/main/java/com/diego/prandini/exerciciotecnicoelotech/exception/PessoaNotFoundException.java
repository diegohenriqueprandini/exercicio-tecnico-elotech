package com.diego.prandini.exerciciotecnicoelotech.exception;

import java.util.UUID;

public class PessoaNotFoundException extends RuntimeException {

    public PessoaNotFoundException(UUID id) {
        super("Pessoa não encontrada: " + id);
    }
}
