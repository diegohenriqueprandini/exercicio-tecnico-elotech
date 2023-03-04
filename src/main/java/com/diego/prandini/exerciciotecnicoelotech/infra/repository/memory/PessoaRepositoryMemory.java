package com.diego.prandini.exerciciotecnicoelotech.infra.repository.memory;

import com.diego.prandini.exerciciotecnicoelotech.domain.entity.Cpf;
import com.diego.prandini.exerciciotecnicoelotech.domain.entity.EntityPage;
import com.diego.prandini.exerciciotecnicoelotech.domain.entity.Pessoa;
import com.diego.prandini.exerciciotecnicoelotech.domain.repository.PessoaRepository;
import com.diego.prandini.exerciciotecnicoelotech.exception.CpfJaExisteException;
import com.diego.prandini.exerciciotecnicoelotech.exception.PessoaNotFoundException;
import com.diego.prandini.exerciciotecnicoelotech.utils.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@ConditionalOnProperty(value = "application.inject.repository", havingValue = "memory")
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
            findByCpf(pessoa.getCpf());
            return;
        }
        data.add(pessoa);
        findByCpf(pessoa.getCpf());
    }

    @Override
    public Pessoa findById(UUID id) {
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

    @Override
    public EntityPage<Pessoa> findAll(int page, int size, String nome, Cpf cpf) {
        return new EntityPage<>(
                data.stream()
                        .filter(item -> StringUtils.isBlank(nome) || item.getNome().contains(nome))
                        .filter(item -> cpf == null || item.getNome().contains(nome))
                        .toList(),
                1,
                0
        );
    }

    @Override
    public Optional<Pessoa> findByCpf(Cpf cpf) {
        List<Pessoa> dadoCpf = this.data.stream()
                .filter(item -> item.getCpf().equals(cpf))
                .toList();
        if (dadoCpf.size() > 1)
            throw new CpfJaExisteException(cpf.get());
        return dadoCpf.stream().findFirst();
    }
}
