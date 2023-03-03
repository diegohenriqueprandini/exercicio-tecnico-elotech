package com.diego.prandini.exerciciotecnicoelotech.application;

import com.diego.prandini.exerciciotecnicoelotech.domain.entity.Pessoa;
import com.diego.prandini.exerciciotecnicoelotech.domain.repository.PessoaRepository;
import com.diego.prandini.exerciciotecnicoelotech.exception.InputNuloException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BuscarPessoa {

    private final PessoaRepository pessoaRepository;

    public Output execute(UUID id) {
        validarInput(id);
        Pessoa pessoa = pessoaRepository.findById(id);
        return toOutput(pessoa);
    }

    private void validarInput(UUID id) {
        if (id == null)
            throw new InputNuloException();
    }

    private Output toOutput(Pessoa pessoa) {
        return new Output(
                pessoa.getId(),
                pessoa.getNome(),
                pessoa.getCpf().get(),
                pessoa.getDataDeNascimento().get()
        );
    }

    public record Output(
            UUID id,
            String nome,
            String cpf,
            LocalDate dataDeNascimento
    ) {
    }
}
