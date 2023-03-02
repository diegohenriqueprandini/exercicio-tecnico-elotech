package com.diego.prandini.exerciciotecnicoelotech.domain.repository;

import com.diego.prandini.exerciciotecnicoelotech.domain.entity.Cpf;
import com.diego.prandini.exerciciotecnicoelotech.domain.entity.Pessoa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PessoaRepository {

    Pessoa findById(UUID id);

    void save(Pessoa pessoa);

    void remove(Pessoa pessoa);

    List<Pessoa> findAll();

    Optional<Pessoa> findByCpf(Cpf cpf);
}
