package com.diego.prandini.exerciciotecnicoelotech.application;

import com.diego.prandini.exerciciotecnicoelotech.domain.entity.Cpf;
import com.diego.prandini.exerciciotecnicoelotech.domain.entity.Pessoa;
import com.diego.prandini.exerciciotecnicoelotech.domain.repository.PessoaRepository;
import com.diego.prandini.exerciciotecnicoelotech.exception.PessoaCpfJaExisteException;
import com.diego.prandini.exerciciotecnicoelotech.infra.system.ApplicationClock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CriarPessoa {

    private final PessoaRepository pessoaRepository;
    private final ApplicationClock applicationClock;

    public Output execute(Input input) {
        validarCpfUnico(input);
        Pessoa pessoa = Pessoa.of(
                UUID.randomUUID(),
                input.nome,
                input.cpf,
                input.dataDeNascimento,
                applicationClock
        );
        pessoaRepository.save(pessoa);
        Pessoa pessoaSaved = pessoaRepository.findById(pessoa.getId());
        return toOutput(pessoaSaved);
    }

    private void validarCpfUnico(Input input) {
        Optional<Pessoa> found = pessoaRepository.findByCpf(new Cpf(input.cpf));
        if (found.isPresent())
            throw new PessoaCpfJaExisteException(input.cpf);
    }

    private Output toOutput(Pessoa pessoa) {
        return new Output(
                pessoa.getId(),
                pessoa.getNome(),
                pessoa.getCpf().get(),
                pessoa.getDataDeNascimento().get()
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
