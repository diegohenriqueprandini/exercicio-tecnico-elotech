package com.diego.prandini.exerciciotecnicoelotech.domain.repository;

import com.diego.prandini.exerciciotecnicoelotech.domain.entity.Cpf;
import com.diego.prandini.exerciciotecnicoelotech.domain.entity.EntityPage;
import com.diego.prandini.exerciciotecnicoelotech.domain.entity.Pessoa;

import java.util.Optional;
import java.util.UUID;

public interface PessoaRepository {

    Pessoa findById(UUID id);

    void save(Pessoa pessoa);

    void remove(Pessoa pessoa);

    EntityPage<Pessoa> findAll(int page, int size);

    Optional<Pessoa> findByCpf(Cpf cpf);
}
