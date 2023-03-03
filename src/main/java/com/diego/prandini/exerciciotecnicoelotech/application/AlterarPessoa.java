package com.diego.prandini.exerciciotecnicoelotech.application;

import com.diego.prandini.exerciciotecnicoelotech.domain.entity.Cpf;
import com.diego.prandini.exerciciotecnicoelotech.domain.entity.DataDeNascimento;
import com.diego.prandini.exerciciotecnicoelotech.domain.entity.Pessoa;
import com.diego.prandini.exerciciotecnicoelotech.domain.repository.PessoaRepository;
import com.diego.prandini.exerciciotecnicoelotech.exception.CpfJaExisteException;
import com.diego.prandini.exerciciotecnicoelotech.exception.InputNuloException;
import com.diego.prandini.exerciciotecnicoelotech.infra.system.ApplicationClock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AlterarPessoa {

    private final PessoaRepository pessoaRepository;
    private final ApplicationClock applicationClock;

    public Output execute(UUID id, Input input) {
        validarInput(id, input);
        Pessoa pessoa = pessoaRepository.findById(id);
        validarCpfUnico(input, pessoa);
        pessoa.setNome(input.nome);
        pessoa.setCpf(new Cpf(input.cpf));
        pessoa.setDataDeNascimento(new DataDeNascimento(input.dataDeNascimento, applicationClock));
        pessoaRepository.save(pessoa);
        Pessoa saved = pessoaRepository.findById(id);
        return toOutput(saved);
    }

    private void validarInput(UUID id, Input input) {
        if (id == null || input == null)
            throw new InputNuloException();
    }

    private void validarCpfUnico(Input input, Pessoa pessoa) {
        Optional<Pessoa> pessoaComMesmoCpf = pessoaRepository.findByCpf(new Cpf(input.cpf));
        if (pessoaComMesmoCpf.isPresent() && !pessoaComMesmoCpf.get().getId().equals(pessoa.getId()))
            throw new CpfJaExisteException(input.cpf);
    }

    private Output toOutput(Pessoa pessoa) {
        return new Output(
                pessoa.getId(),
                pessoa.getNome(),
                pessoa.getCpf().get(),
                pessoa.getDataDeNascimento().get()
        );
    }

    public record Input(
            String nome,
            String cpf,
            LocalDate dataDeNascimento
    ) {
    }

    public record Output(
            UUID id,
            String nome,
            String cpf,
            LocalDate dataDeNascimento
    ) {
    }
}
