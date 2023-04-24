package com.diego.prandini.exerciciotecnicoelotech.application.usecase.pessoa;

import com.diego.prandini.exerciciotecnicoelotech.domain.entity.pessoa.Pessoa;
import com.diego.prandini.exerciciotecnicoelotech.application.repository.PessoaRepository;
import com.diego.prandini.exerciciotecnicoelotech.exception.IdPessoaNuloException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RemoverPessoa {

    private final PessoaRepository pessoaRepository;

    public void execure(UUID id) {
        validarInput(id);
        Pessoa pessoa = pessoaRepository.findById(id);
        pessoaRepository.remove(pessoa);
    }

    private void validarInput(UUID id) {
        if (id == null)
            throw new IdPessoaNuloException();
    }
}
