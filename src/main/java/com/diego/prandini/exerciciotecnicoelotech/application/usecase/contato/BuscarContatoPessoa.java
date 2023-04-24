package com.diego.prandini.exerciciotecnicoelotech.application.usecase.contato;

import com.diego.prandini.exerciciotecnicoelotech.domain.entity.contato.Contato;
import com.diego.prandini.exerciciotecnicoelotech.domain.entity.pessoa.Pessoa;
import com.diego.prandini.exerciciotecnicoelotech.application.repository.PessoaRepository;
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
        return toOutput(pessoa, contato);
    }

    private void validarInput(UUID idPessoa, UUID idContato) {
        if (idPessoa == null)
            throw new IdPessoaNuloException();
        if (idContato == null)
            throw new IdContatoNuloException();
    }

    private Output toOutput(Pessoa pessoa, Contato contato) {
        ContatoOutput contatoOutput = new ContatoOutput(
                contato.getId(),
                contato.getNome(),
                contato.getTelefone(),
                contato.getEmail()
        );
        PessoaOutput pessoaOutput = new PessoaOutput(
                pessoa.getId()
        );
        return new Output(
                contatoOutput,
                pessoaOutput
        );
    }

    public record Output(
            ContatoOutput contato,
            PessoaOutput pessoa
    ) {
    }

    public record ContatoOutput(
            UUID id,
            String nome,
            String telefone,
            String email
    ) {
    }

    public record PessoaOutput(
            UUID id
    ) {
    }
}
