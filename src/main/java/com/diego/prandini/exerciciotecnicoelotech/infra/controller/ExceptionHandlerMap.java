package com.diego.prandini.exerciciotecnicoelotech.infra.controller;

import com.diego.prandini.exerciciotecnicoelotech.exception.ContatoNotFoundException;
import com.diego.prandini.exerciciotecnicoelotech.exception.PessoaNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class ExceptionHandlerMap {

    public static final List<Class<? extends RuntimeException>> NOT_FOUND = new ArrayList<>();

    static {
        NOT_FOUND.add(PessoaNotFoundException.class);
        NOT_FOUND.add(ContatoNotFoundException.class);
    }
}
