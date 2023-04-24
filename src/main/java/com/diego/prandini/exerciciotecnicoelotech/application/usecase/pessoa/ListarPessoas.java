package com.diego.prandini.exerciciotecnicoelotech.application.usecase.pessoa;

import com.diego.prandini.exerciciotecnicoelotech.domain.entity.pessoa.Cpf;
import com.diego.prandini.exerciciotecnicoelotech.application.repository.EntityPage;
import com.diego.prandini.exerciciotecnicoelotech.domain.entity.pessoa.Pessoa;
import com.diego.prandini.exerciciotecnicoelotech.application.repository.PessoaRepository;
import com.diego.prandini.exerciciotecnicoelotech.exception.InputNuloException;
import com.diego.prandini.exerciciotecnicoelotech.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ListarPessoas {

    private final PessoaRepository pessoaRepository;

    public Output execute(Input input) {
        validarInput(input);
        EntityPage<Pessoa> pessoas = pessoaRepository.findAll(
                input.pagination.page,
                input.pagination.size,
                input.filter.nome,
                StringUtils.isBlank(input.filter.cpf) ? null : new Cpf(input.filter.cpf)
        );
        return toOutput(pessoas);
    }

    private void validarInput(Input input) {
        if (input == null)
            throw new InputNuloException();
        if (input.pagination == null)
            throw new InputNuloException();
        if (input.filter == null)
            throw new InputNuloException();
    }

    private Output toOutput(EntityPage<Pessoa> pessoas) {
        List<PessoaOutput> pessoasOutput = pessoas.getItems().stream()
                .map(item -> new PessoaOutput(
                        item.getId(),
                        item.getNome(),
                        item.getCpf().get(),
                        item.getDataDeNascimento().get()
                )).toList();
        PaginationOutput paginationOutput = new PaginationOutput(
                pessoas.getTotalPages(),
                pessoas.getPageNumber()
        );
        return new Output(
                pessoasOutput,
                paginationOutput
        );
    }

    public record Input(
            PaginationInput pagination,
            FilterInput filter
    ) {
    }

    public record PaginationInput(
            int page,
            int size
    ) {
    }

    public record FilterInput(
            String nome,
            String cpf
    ) {
    }

    public record Output(
            List<PessoaOutput> pessoas,
            PaginationOutput pagination
    ) {
    }

    public record PessoaOutput(
            UUID id,
            String nome,
            String cpf,
            LocalDate dataDeNascimento
    ) {
    }

    public record PaginationOutput(
            int totalPages,
            int pageNumber
    ) {
    }
}
