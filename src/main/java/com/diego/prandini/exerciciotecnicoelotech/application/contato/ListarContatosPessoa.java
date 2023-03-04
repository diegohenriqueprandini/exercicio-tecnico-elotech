package com.diego.prandini.exerciciotecnicoelotech.application.contato;

import com.diego.prandini.exerciciotecnicoelotech.domain.entity.contato.Contato;
import com.diego.prandini.exerciciotecnicoelotech.domain.entity.pessoa.Pessoa;
import com.diego.prandini.exerciciotecnicoelotech.domain.repository.PessoaRepository;
import com.diego.prandini.exerciciotecnicoelotech.exception.IdPessoaNuloException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ListarContatosPessoa {

    private final PessoaRepository pessoaRepository;

    public Output execute(UUID idPessoa) {
        validarInput(idPessoa);
        Pessoa pessoa = pessoaRepository.findById(idPessoa);
        List<Contato> contatos = pessoa.getContatos();
        return toOutput(pessoa, contatos);
    }

    private void validarInput(UUID idPessoa) {
        if (idPessoa == null)
            throw new IdPessoaNuloException();
    }

    private Output toOutput(Pessoa pessoa, List<Contato> contatos) {
        List<ContatoOutput> contatosOutput = contatos.stream()
                .map(item -> new ContatoOutput(
                        item.getId(),
                        item.getNome(),
                        item.getTelefone(),
                        item.getEmail()
                )).toList();
        PessoaOutput pessoaOutput = new PessoaOutput(
                pessoa.getId()
        );
        return new Output(
                contatosOutput,
                pessoaOutput
        );
    }

    public record Output(
            List<ContatoOutput> contatos,
            PessoaOutput pessoa
    ) {
    }

    public record PessoaOutput(
            UUID id
    ) {
    }

    public record ContatoOutput(
            UUID id,
            String nome,
            String telefone,
            String email
    ) {
    }
}
