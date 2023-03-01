package com.diego.prandini.exerciciotecnicoelotech.application;

import com.diego.prandini.exerciciotecnicoelotech.domain.entity.Pessoa;
import com.diego.prandini.exerciciotecnicoelotech.domain.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@RequiredArgsConstructor
public class PessoaService {

    private final PessoaRepository pessoaRepository;

    public Output criar(Input input) {
        UUID id = UUID.randomUUID();
        Pessoa pessoa = new Pessoa(id, input.nome, input.cpf, input.dataDeNascimento);
        pessoaRepository.add(pessoa);
        Pessoa pessoaSaved = pessoaRepository.getOne(id);
        return toOutput(pessoaSaved);
    }

    private Output toOutput(Pessoa pessoa) {
        return new Output(
                pessoa.getId(),
                pessoa.getNome(),
                pessoa.getCpf(),
                pessoa.getDataDeNascimento()
        );
    }

    public record Input(String nome, String cpf, LocalDate dataDeNascimento) {
    }

    public record Output(UUID id, String nome, String cpf, LocalDate dataDeNascimento) {
    }
}
