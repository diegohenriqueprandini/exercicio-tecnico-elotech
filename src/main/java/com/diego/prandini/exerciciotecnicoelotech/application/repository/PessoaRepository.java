package com.diego.prandini.exerciciotecnicoelotech.application.repository;

import com.diego.prandini.exerciciotecnicoelotech.domain.entity.pessoa.Cpf;
import com.diego.prandini.exerciciotecnicoelotech.domain.entity.pessoa.Pessoa;

import java.util.Optional;
import java.util.UUID;

public interface PessoaRepository {

    Pessoa findById(UUID id);

    void save(Pessoa pessoa);

    void remove(Pessoa pessoa);

    EntityPage<Pessoa> findAll(int page, int size, String nome, Cpf cpf);

    Optional<Pessoa> findByCpf(Cpf cpf);
}
