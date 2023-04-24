package com.diego.prandini.exerciciotecnicoelotech.infra.repository.mongo;

import com.diego.prandini.exerciciotecnicoelotech.infra.repository.database.PessoaTable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PessoaMongoRepository extends
        MongoRepository<PessoaTable, UUID> {

    Optional<PessoaTable> findByCpf(String cpf);

    Page<PessoaTable> findByNomeContainingIgnoreCaseAndCpf(String nome, String cpf, Pageable pageable);

    Page<PessoaTable> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    Page<PessoaTable> findByCpf(String nomecpf, Pageable pageable);
}
