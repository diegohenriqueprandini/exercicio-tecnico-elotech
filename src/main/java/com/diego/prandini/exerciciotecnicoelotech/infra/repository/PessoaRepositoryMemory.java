package com.diego.prandini.exerciciotecnicoelotech.infra.repository;

import com.diego.prandini.exerciciotecnicoelotech.domain.entity.Pessoa;
import com.diego.prandini.exerciciotecnicoelotech.domain.repository.PessoaRepository;
import com.diego.prandini.exerciciotecnicoelotech.exception.PessoaNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PessoaRepositoryMemory implements PessoaRepository {

    private final List<Pessoa.Data> data = new ArrayList<>();

    @Override
    public void save(Pessoa pessoa) {
        Optional<Pessoa.Data> found = this.data.stream()
                .filter(item -> item.id().equals(pessoa.getId()))
                .findFirst();
        if (found.isPresent()) {
            data.remove(found.get());
            data.add(pessoa.toData());
            return;
        }
        data.add(pessoa.toData());
    }

    @Override
    public Pessoa getOne(UUID id) {
        Pessoa.Data data = this.data.stream()
                .filter(item -> item.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new PessoaNotFoundException(id));
        return Pessoa.fromData(data);
    }
}
