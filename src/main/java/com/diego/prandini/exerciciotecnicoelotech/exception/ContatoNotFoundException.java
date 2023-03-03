package com.diego.prandini.exerciciotecnicoelotech.exception;

import java.util.UUID;

public class ContatoNotFoundException extends RuntimeException {

    public ContatoNotFoundException(UUID id) {
        super("Contato não encontrado: " + id);
    }
}
