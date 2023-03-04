package com.diego.prandini.exerciciotecnicoelotech.infra.repository.database;

import com.diego.prandini.exerciciotecnicoelotech.domain.entity.Contato;
import com.diego.prandini.exerciciotecnicoelotech.domain.entity.Cpf;
import com.diego.prandini.exerciciotecnicoelotech.domain.entity.DataDeNascimento;
import com.diego.prandini.exerciciotecnicoelotech.domain.entity.Pessoa;
import com.diego.prandini.exerciciotecnicoelotech.domain.repository.PessoaRepository;
import com.diego.prandini.exerciciotecnicoelotech.exception.PessoaNotFoundException;
import com.diego.prandini.exerciciotecnicoelotech.infra.system.clock.ApplicationClock;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(value = "application.inject.repository", havingValue = "database")
public class PessoaRepositoryDatabase implements PessoaRepository {

    private final PessoaJpaRepository pessoaJpaRepository;
    private final ApplicationClock applicationClock;

    @Override
    public void save(Pessoa pessoa) {
        PessoaTable pessoaTable = new PessoaTable(
                pessoa.getId(),
                pessoa.getNome(),
                pessoa.getCpf().get(),
                pessoa.getDataDeNascimento().get(),
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
    public List<Pessoa> findAll() {
        return pessoaJpaRepository.findAll().stream()
                .map(this::toPessoa)
                .toList();
    }

    @Override
    public Optional<Pessoa> findByCpf(Cpf cpf) {
        return pessoaJpaRepository.findByCpf(cpf.get())
                .map(this::toPessoa);
    }

    private Pessoa toPessoa(PessoaTable pessoaTable) {
        Pessoa.Builder builder = new Pessoa.Builder(
                pessoaTable.getId(),
                pessoaTable.getNome(),
                new Cpf(pessoaTable.getCpf()),
                new DataDeNascimento(pessoaTable.getDataDeNascimento(), applicationClock)
        );
        pessoaTable.getContatos().stream()
                .map(item -> new Contato(
                        item.getId(),
                        item.getNome(),
                        item.getTelefone(),
                        item.getEmail()
                )).forEach(builder::contatos);
        return builder.build();
    }
}
