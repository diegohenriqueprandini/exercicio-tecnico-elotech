package com.diego.prandini.exerciciotecnicoelotech.application.pessoa;

import com.diego.prandini.exerciciotecnicoelotech.domain.entity.contato.Contato;
import com.diego.prandini.exerciciotecnicoelotech.domain.entity.pessoa.Cpf;
import com.diego.prandini.exerciciotecnicoelotech.domain.entity.pessoa.DataDeNascimento;
import com.diego.prandini.exerciciotecnicoelotech.domain.entity.pessoa.Pessoa;
import com.diego.prandini.exerciciotecnicoelotech.domain.repository.PessoaRepository;
import com.diego.prandini.exerciciotecnicoelotech.exception.ContatosVazioException;
import com.diego.prandini.exerciciotecnicoelotech.exception.CpfJaExisteException;
import com.diego.prandini.exerciciotecnicoelotech.exception.InputNuloException;
import com.diego.prandini.exerciciotecnicoelotech.infra.system.clock.ApplicationClock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CriarPessoa {

    private final PessoaRepository pessoaRepository;
    private final ApplicationClock applicationClock;

    public Output execute(Input input) {
        validarInput(input);
        validarCpfUnico(input);
        Pessoa.Builder builder = criarBuilder(input);
        adicionarContatos(builder, input.contatos);
        Pessoa pessoa = builder.build();
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

    private Pessoa.Builder criarBuilder(Input input) {
        return new Pessoa.Builder(
                UUID.randomUUID(),
                input.nome,
                new Cpf(input.cpf),
                new DataDeNascimento(input.dataDeNascimento, applicationClock)
        );
    }

    private void adicionarContatos(Pessoa.Builder builder, List<ContatoInput> contatosInput) {
        contatosInput.stream()
                .map(item -> new Contato(
                        UUID.randomUUID(),
                        item.nome,
                        item.telefone,
                        item.email
                )).forEach(builder::contatos);
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
