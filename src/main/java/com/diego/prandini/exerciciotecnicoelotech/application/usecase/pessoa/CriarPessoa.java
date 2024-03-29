package com.diego.prandini.exerciciotecnicoelotech.application.usecase.pessoa;

import com.diego.prandini.exerciciotecnicoelotech.domain.entity.contato.Contato;
import com.diego.prandini.exerciciotecnicoelotech.domain.entity.pessoa.Cpf;
import com.diego.prandini.exerciciotecnicoelotech.domain.entity.pessoa.DataDeNascimento;
import com.diego.prandini.exerciciotecnicoelotech.domain.entity.pessoa.Password;
import com.diego.prandini.exerciciotecnicoelotech.domain.entity.pessoa.Pessoa;
import com.diego.prandini.exerciciotecnicoelotech.application.repository.PessoaRepository;
import com.diego.prandini.exerciciotecnicoelotech.exception.ContatosVazioException;
import com.diego.prandini.exerciciotecnicoelotech.exception.CpfJaExisteException;
import com.diego.prandini.exerciciotecnicoelotech.exception.InputNuloException;
import com.diego.prandini.exerciciotecnicoelotech.infra.system.clock.ApplicationClock;
import com.diego.prandini.exerciciotecnicoelotech.infra.system.security.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CriarPessoa {

    private final PessoaRepository pessoaRepository;
    private final ApplicationClock applicationClock;
    private final PasswordEncoder passwordEncoder;

    public Output execute(Input input) {
        validarInput(input);
        validarCpfUnico(input);
        Pessoa pessoa = criarPessoa(input);
        pessoaRepository.save(pessoa);
        Pessoa pessoaSaved = pessoaRepository.findById(pessoa.getId());
        return toOutput(pessoaSaved);
    }

    private void validarInput(Input input) {
        if (input == null)
            throw new InputNuloException();
        if (input.contatos == null)
            throw new ContatosVazioException();
    }

    private void validarCpfUnico(Input input) {
        Optional<Pessoa> found = pessoaRepository.findByCpf(new Cpf(input.cpf));
        if (found.isPresent())
            throw new CpfJaExisteException(input.cpf);
    }

    private Pessoa criarPessoa(Input input) {
        return new Pessoa(
                UUID.randomUUID(),
                input.nome,
                new Cpf(input.cpf),
                new DataDeNascimento(input.dataDeNascimento, applicationClock),
                new Password(input.password, passwordEncoder),
                input.contatos.stream()
                        .map(item -> new Contato(
                                UUID.randomUUID(),
                                item.nome,
                                item.telefone,
                                item.email
                        ))
                        .collect(Collectors.toList())
        );
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
            LocalDate dataDeNascimento,
            String password,
            List<ContatoInput> contatos
    ) {
    }

    public record ContatoInput(
            String nome,
            String telefone,
            String email
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
