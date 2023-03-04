package com.diego.prandini.exerciciotecnicoelotech.infra.repository.database;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    Page<PessoaTable> findByNomeContainingIgnoreCaseAndCpf(String nome, String cpf, Pageable pageable);

    Page<PessoaTable> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    Page<PessoaTable> findByCpf(String nomecpf, Pageable pageable);
}
