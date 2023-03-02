package com.diego.prandini.exerciciotecnicoelotech.application;

import com.diego.prandini.exerciciotecnicoelotech.domain.entity.Pessoa;
import com.diego.prandini.exerciciotecnicoelotech.domain.repository.PessoaRepository;
import com.diego.prandini.exerciciotecnicoelotech.infra.system.ApplicationClock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CriarPessoa {

    private final PessoaRepository pessoaRepository;
    private final ApplicationClock applicationClock;

    public Output execute(Input input) {
        UUID id = UUID.randomUUID();
        Pessoa pessoa = Pessoa.of(
                id,
                input.nome,
                input.cpf,
                input.dataDeNascimento,
                applicationClock
        );
        pessoaRepository.save(pessoa);
        Pessoa pessoaSaved = pessoaRepository.getOne(id);
        return toOutput(pessoaSaved);
    }

    private Output toOutput(Pessoa pessoa) {
        return new Output(
                pessoa.getId(),
                pessoa.getNome(),
                pessoa.getCpf().toString(),
                pessoa.getDataDeNascimento().getDate()
        );
    }

    public record Input(
            String nome,
            String cpf,
            LocalDate dataDeNascimento
    ) {
    }

    public record Output(
            UUID id,
            String nome,
            String cpf,
            LocalDate dataDeNascimento
    ) {
    }
}
