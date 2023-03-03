package com.diego.prandini.exerciciotecnicoelotech.application.contato;

import com.diego.prandini.exerciciotecnicoelotech.domain.entity.Contato;
import com.diego.prandini.exerciciotecnicoelotech.domain.entity.Pessoa;
import com.diego.prandini.exerciciotecnicoelotech.domain.repository.PessoaRepository;
import com.diego.prandini.exerciciotecnicoelotech.exception.IdContatoNuloException;
import com.diego.prandini.exerciciotecnicoelotech.exception.IdPessoaNuloException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BuscarContatoPessoa {

    private final PessoaRepository pessoaRepository;

    public Output execute(UUID idPessoa, UUID idContato) {
        validarInput(idPessoa, idContato);
        Pessoa pessoa = pessoaRepository.findById(idPessoa);
        Contato contato = pessoa.buscarContato(idContato);
        return toOutput(contato);
    }

    private void validarInput(UUID idPessoa, UUID idContato) {
        if (idPessoa == null)
            throw new IdPessoaNuloException();
        if (idContato == null)
            throw new IdContatoNuloException();
    }

    private Output toOutput(Contato contato) {
        return new Output(
                contato.getId(),
                contato.getNome(),
                contato.getTelefone(),
                contato.getEmail()
        );
    }

    public record Output(
            UUID id,
            String nome,
            String telefone,
            String email
    ) {
    }
}
