package com.diego.prandini.exerciciotecnicoelotech.infra.repository.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PessoaJpaRepository extends
        JpaRepository<PessoaTable, UUID>,
        PagingAndSortingRepository<PessoaTable, UUID> {

    Optional<PessoaTable> findByCpf(String cpf);
}
