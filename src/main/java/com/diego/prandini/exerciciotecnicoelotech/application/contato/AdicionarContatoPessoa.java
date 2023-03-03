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
        UUID id = UUID.randomUUID();
        Contato contato = new Contato(
                id,
                input.nome,
                input.telefone,
                input.email
        );
        pessoa.adicionarContato(contato);
        pessoaRepository.save(pessoa);
        Pessoa pessoaSaved = pessoaRepository.findById(pessoa.getId());
        Contato contatoSaved = pessoaSaved.buscarContato(id);
        return toOutput(pessoaSaved, contatoSaved);
    }

    private void validarInput(UUID idPessoa, Input input) {
        if (idPessoa == null)
            throw new IdPessoaNuloException();
        if (input == null)
            throw new InputNuloException();
    }

    private Output toOutput(Pessoa pessoa, Contato contato) {
        return new Output(
                contato.getId(),
                pessoa.getId(),
                contato.getNome(),
                contato.getTelefone(),
                contato.getEmail()
        );
    }

    public record Input(
            String nome,
            String telefone,
            String email
    ) {
    }

    public record Output(
            UUID id,
            UUID idPessoa,
            String nome,
            String telefone,
            String email
    ) {
    }
}
