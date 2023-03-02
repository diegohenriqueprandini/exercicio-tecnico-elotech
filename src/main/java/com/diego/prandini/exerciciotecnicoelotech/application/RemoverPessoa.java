package com.diego.prandini.exerciciotecnicoelotech.application;

import com.diego.prandini.exerciciotecnicoelotech.domain.entity.Pessoa;
import com.diego.prandini.exerciciotecnicoelotech.domain.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RemoverPessoa {

    private final PessoaRepository pessoaRepository;

    public void execure(UUID id) {
        Pessoa pessoa = pessoaRepository.getOne(id);
        pessoaRepository.remove(pessoa);
    }
}
