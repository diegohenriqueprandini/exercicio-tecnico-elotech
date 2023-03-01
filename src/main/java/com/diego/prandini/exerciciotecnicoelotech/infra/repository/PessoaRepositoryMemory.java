package com.diego.prandini.exerciciotecnicoelotech.infra.repository;

import com.diego.prandini.exerciciotecnicoelotech.domain.entity.Pessoa;
import com.diego.prandini.exerciciotecnicoelotech.domain.repository.PessoaNotFoundException;
import com.diego.prandini.exerciciotecnicoelotech.domain.repository.PessoaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PessoaRepositoryMemory implements PessoaRepository {

    private final List<Pessoa.Data> data = new ArrayList<>();

    @Override
    public void add(Pessoa pessoa) {
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
