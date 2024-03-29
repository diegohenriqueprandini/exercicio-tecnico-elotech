package com.diego.prandini.exerciciotecnicoelotech.application.usecase.contato;

import com.diego.prandini.exerciciotecnicoelotech.domain.entity.contato.Contato;
import com.diego.prandini.exerciciotecnicoelotech.domain.entity.pessoa.Pessoa;
import com.diego.prandini.exerciciotecnicoelotech.application.repository.PessoaRepository;
import com.diego.prandini.exerciciotecnicoelotech.exception.IdContatoNuloException;
import com.diego.prandini.exerciciotecnicoelotech.exception.IdPessoaNuloException;
import com.diego.prandini.exerciciotecnicoelotech.exception.InputNuloException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AlterarContatoPessoa {

    private final PessoaRepository pessoaRepository;

    public Output execute(UUID idPessoa, UUID idContato, Input input) {
        validarInput(idPessoa, idContato, input);
        Pessoa pessoa = pessoaRepository.findById(idPessoa);
        alterarContato(pessoa, idContato, input);
        pessoaRepository.save(pessoa);
        Pessoa pessoaSaved = pessoaRepository.findById(idPessoa);
        Contato contatoSaved = pessoaSaved.buscarContato(idContato);
        return toOutput(pessoaSaved, contatoSaved);
    }

    private void validarInput(UUID idPessoa, UUID idContato, Input input) {
        if (idPessoa == null)
            throw new IdPessoaNuloException();
        if (idContato == null)
            throw new IdContatoNuloException();
        if (input == null)
            throw new InputNuloException();
    }

    private void alterarContato(Pessoa pessoa, UUID idContato, Input input) {
        pessoa.alterarContato(new Contato(
                idContato,
                input.nome,
                input.telefone,
                input.email
        ));
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

    public record Input(
            String nome,
            String telefone,
            String email
    ) {
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
