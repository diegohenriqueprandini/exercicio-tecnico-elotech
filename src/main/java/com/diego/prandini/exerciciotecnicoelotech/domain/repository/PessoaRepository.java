package com.diego.prandini.exerciciotecnicoelotech.domain.repository;

import com.diego.prandini.exerciciotecnicoelotech.domain.entity.Pessoa;

import java.util.UUID;

public interface PessoaRepository {
    void add(Pessoa pessoa);

    Pessoa getOne(UUID id);
}
