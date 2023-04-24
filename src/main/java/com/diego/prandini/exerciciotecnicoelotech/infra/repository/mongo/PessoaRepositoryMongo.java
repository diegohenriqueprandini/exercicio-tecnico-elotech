package com.diego.prandini.exerciciotecnicoelotech.infra.repository.mongo;

import com.diego.prandini.exerciciotecnicoelotech.application.repository.EntityPage;
import com.diego.prandini.exerciciotecnicoelotech.domain.entity.contato.Contato;
import com.diego.prandini.exerciciotecnicoelotech.domain.entity.pessoa.Cpf;
import com.diego.prandini.exerciciotecnicoelotech.domain.entity.pessoa.DataDeNascimento;
import com.diego.prandini.exerciciotecnicoelotech.domain.entity.pessoa.Password;
import com.diego.prandini.exerciciotecnicoelotech.domain.entity.pessoa.Pessoa;
import com.diego.prandini.exerciciotecnicoelotech.application.repository.PessoaRepository;
import com.diego.prandini.exerciciotecnicoelotech.exception.PessoaNotFoundException;
import com.diego.prandini.exerciciotecnicoelotech.infra.condition.RepositoryConditionMongo;
import com.diego.prandini.exerciciotecnicoelotech.infra.repository.database.ContatoTable;
import com.diego.prandini.exerciciotecnicoelotech.infra.repository.database.PessoaTable;
import com.diego.prandini.exerciciotecnicoelotech.infra.system.clock.ApplicationClock;
import com.diego.prandini.exerciciotecnicoelotech.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Conditional(RepositoryConditionMongo.class)
public class PessoaRepositoryMongo implements PessoaRepository {

    private final PessoaMongoRepository pessoaJpaRepository;
    private final ApplicationClock applicationClock;

    @Override
    public void save(Pessoa pessoa) {
        PessoaTable pessoaTable = new PessoaTable(
                pessoa.getId(),
                pessoa.getNome(),
                pessoa.getCpf().get(),
                pessoa.getDataDeNascimento().get(),
                pessoa.getPassword().get(),
                pessoa.getContatos().stream()
                        .map(item -> new ContatoTable(
                                item.getId(),
                                item.getNome(),
                                item.getTelefone(),
                                item.getEmail()
                        )).toList()
        );
        pessoaJpaRepository.save(pessoaTable);
    }

    @Override
    public Pessoa findById(UUID id) {
        PessoaTable pessoaTable = pessoaJpaRepository.findById(id)
                .orElseThrow(() -> new PessoaNotFoundException(id));
        return toPessoa(pessoaTable);
    }

    @Override
    public void remove(Pessoa pessoa) {
        pessoaJpaRepository.deleteById(pessoa.getId());
    }

    @Override
    public EntityPage<Pessoa> findAll(int page, int size, String nome, Cpf cpf) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("nome"));
        Page<PessoaTable> pessoas = findAllPageAndFilter(pageRequest, nome, cpf);
        return new EntityPage<>(
                pessoas.map(this::toPessoa).toList(),
                pessoas.getTotalPages(),
                pessoas.getNumber()
        );
    }

    private Page<PessoaTable> findAllPageAndFilter(PageRequest pageRequest, String nome, Cpf cpf) {
        if (!StringUtils.isBlank(nome) && cpf != null)
            return pessoaJpaRepository.findByNomeContainingIgnoreCaseAndCpf(nome, cpf.get(), pageRequest);
        if (!StringUtils.isBlank(nome))
            return pessoaJpaRepository.findByNomeContainingIgnoreCase(nome, pageRequest);
        if (cpf != null)
            return pessoaJpaRepository.findByCpf(cpf.get(), pageRequest);
        return pessoaJpaRepository.findAll(pageRequest);
    }

    @Override
    public Optional<Pessoa> findByCpf(Cpf cpf) {
        return pessoaJpaRepository.findByCpf(cpf.get())
                .map(this::toPessoa);
    }

    private Pessoa toPessoa(PessoaTable pessoaTable) {
        return new Pessoa(
                pessoaTable.getId(),
                pessoaTable.getNome(),
                new Cpf(pessoaTable.getCpf()),
                new DataDeNascimento(pessoaTable.getDataDeNascimento(), applicationClock),
                new Password(pessoaTable.getPassword()),
                pessoaTable.getContatos().stream()
                        .map(item -> new Contato(
                                item.getId(),
                                item.getNome(),
                                item.getTelefone(),
                                item.getEmail()
                        ))
                        .collect(Collectors.toList())
        );
    }
}
