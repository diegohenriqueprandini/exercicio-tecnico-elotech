package com.diego.prandini.exerciciotecnicoelotech.application;

import com.diego.prandini.exerciciotecnicoelotech.domain.entity.Pessoa;
import com.diego.prandini.exerciciotecnicoelotech.domain.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class RemoverPessoa {

    private final PessoaRepository pessoaRepository;

    public void execure(UUID id) {
        Pessoa pessoa = pessoaRepository.getOne(id);
        pessoaRepository.remove(pessoa);
    }
}
