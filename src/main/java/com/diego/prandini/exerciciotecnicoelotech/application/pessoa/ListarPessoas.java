package com.diego.prandini.exerciciotecnicoelotech.application.pessoa;

import com.diego.prandini.exerciciotecnicoelotech.domain.entity.Pessoa;
import com.diego.prandini.exerciciotecnicoelotech.domain.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ListarPessoas {

    private final PessoaRepository pessoaRepository;

    public Output execute() {
        List<Pessoa> pessoas = pessoaRepository.findAll();
        return toOutput(pessoas);
    }

    private Output toOutput(List<Pessoa> pessoas) {
        List<PessoaOutput> pessoasOutput = pessoas.stream()
                .map(item -> new PessoaOutput(
                        item.getId(),
                        item.getNome(),
                        item.getCpf().get(),
                        item.getDataDeNascimento().get()
                )).toList();
        return new Output(
                pessoasOutput
        );
    }

    public record Output(
            List<PessoaOutput> pessoas
    ) {
    }

    public record PessoaOutput(
            UUID id,
            String nome,
            String cpf,
            LocalDate dataDeNascimento
    ) {
    }
}
