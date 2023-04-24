package com.diego.prandini.exerciciotecnicoelotech.application.usecase.contato;

import com.diego.prandini.exerciciotecnicoelotech.domain.entity.pessoa.Pessoa;
import com.diego.prandini.exerciciotecnicoelotech.application.repository.PessoaRepository;
import com.diego.prandini.exerciciotecnicoelotech.exception.IdContatoNuloException;
import com.diego.prandini.exerciciotecnicoelotech.exception.IdPessoaNuloException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RemoverContatoPessoa {

    private final PessoaRepository pessoaRepository;

    public void execute(UUID idPessoa, UUID idContato) {
        validarInput(idPessoa, idContato);
        Pessoa pessoa = pessoaRepository.findById(idPessoa);
        pessoa.removerContato(idContato);
        pessoaRepository.save(pessoa);
    }

    private void validarInput(UUID idPessoa, UUID idContato) {
        if (idPessoa == null)
            throw new IdPessoaNuloException();
        if (idContato == null)
            throw new IdContatoNuloException();
    }
}
