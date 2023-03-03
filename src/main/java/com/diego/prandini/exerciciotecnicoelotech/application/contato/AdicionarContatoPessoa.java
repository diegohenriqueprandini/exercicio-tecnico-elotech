package com.diego.prandini.exerciciotecnicoelotech.application.contato;

import com.diego.prandini.exerciciotecnicoelotech.domain.entity.Contato;
import com.diego.prandini.exerciciotecnicoelotech.domain.entity.Pessoa;
import com.diego.prandini.exerciciotecnicoelotech.domain.repository.PessoaRepository;
import com.diego.prandini.exerciciotecnicoelotech.exception.IdPessoaNuloException;
import com.diego.prandini.exerciciotecnicoelotech.exception.InputNuloException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AdicionarContatoPessoa {

    private final PessoaRepository pessoaRepository;

    public Output execute(UUID idPessoa, Input input) {
        validarInput(idPessoa, input);
        Pessoa pessoa = pessoaRepository.findById(idPessoa);
        UUID id = adicionarContato(pessoa, input);
        pessoaRepository.save(pessoa);
        Pessoa pessoaSaved = pessoaRepository.findById(idPessoa);
        Contato contatoSaved = pessoaSaved.buscarContato(id);
        return toOutput(pessoaSaved, contatoSaved);
    }

    private void validarInput(UUID idPessoa, Input input) {
        if (idPessoa == null)
            throw new IdPessoaNuloException();
        if (input == null)
            throw new InputNuloException();
    }

    private UUID adicionarContato(Pessoa pessoa, Input input) {
        UUID id = UUID.randomUUID();
        Contato contato = new Contato(
                id,
                input.nome,
                input.telefone,
                input.email
        );
        pessoa.adicionarContato(contato);
        return id;
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
