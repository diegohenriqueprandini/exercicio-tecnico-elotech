package com.diego.prandini.exerciciotecnicoelotech.infra.repository;

import com.diego.prandini.exerciciotecnicoelotech.domain.entity.Pessoa;
import com.diego.prandini.exerciciotecnicoelotech.domain.repository.PessoaRepository;
import com.diego.prandini.exerciciotecnicoelotech.exception.PessoaNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PessoaRepositoryMemory implements PessoaRepository {

    private final List<Pessoa> data = new ArrayList<>();

    @Override
    public void save(Pessoa pessoa) {
        Optional<Pessoa> found = this.data.stream()
                .filter(item -> item.getId().equals(pessoa.getId()))
                .findFirst();
        if (found.isPresent()) {
            data.remove(found.get());
            data.add(pessoa);
            return;
        }
        data.add(pessoa);
    }

    @Override
    public Pessoa getOne(UUID id) {
        return this.data.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new PessoaNotFoundException(id));
    }

    @Override
    public void remove(Pessoa pessoa) {
        this.data.stream()
                .filter(item -> item.getId().equals(pessoa.getId()))
                .findFirst()
                .ifPresent(this.data::remove);
    }
}
