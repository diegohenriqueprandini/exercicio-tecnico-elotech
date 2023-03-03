package com.diego.prandini.exerciciotecnicoelotech.domain.entity;

import com.diego.prandini.exerciciotecnicoelotech.exception.ContatoNotFoundException;
import com.diego.prandini.exerciciotecnicoelotech.exception.ContatoNuloException;
import com.diego.prandini.exerciciotecnicoelotech.exception.ContatosVazioException;
import com.diego.prandini.exerciciotecnicoelotech.exception.CpfVazioException;
import com.diego.prandini.exerciciotecnicoelotech.exception.DataDeNascimentoVaziaException;
import com.diego.prandini.exerciciotecnicoelotech.exception.IdNuloException;
import com.diego.prandini.exerciciotecnicoelotech.exception.NomeVazioException;
import com.diego.prandini.exerciciotecnicoelotech.utils.StringUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
public class Pessoa {

    private final UUID id;
    private String nome;
    private Cpf cpf;
    private DataDeNascimento dataDeNascimento;
    private final List<Contato> contatos = new ArrayList<>();

    private Pessoa(
            UUID id,
            String nome,
            Cpf cpf,
            DataDeNascimento dataDeNascimento
    ) {
        if (id == null)
            throw new IdNuloException();
        this.id = id;
        setNome(nome);
        setCpf(cpf);
        setDataDeNascimento(dataDeNascimento);
    }

    public void setNome(String nome) {
        if (StringUtils.isBlank(nome))
            throw new NomeVazioException();
        this.nome = nome;
    }

    public void setCpf(Cpf cpf) {
        if (cpf == null)
            throw new CpfVazioException();
        this.cpf = cpf;
    }

    public void setDataDeNascimento(DataDeNascimento dataDeNascimento) {
        if (dataDeNascimento == null)
            throw new DataDeNascimentoVaziaException();
        this.dataDeNascimento = dataDeNascimento;
    }

    public void adicionarContato(Contato contato) {
        if (contato == null)
            throw new ContatoNuloException();
        if (contato.getId() == null)
            throw new ContatoNuloException();
        if (this.contatos.contains(contato))
            return;
        this.contatos.add(contato);
    }

    public void removerContato(UUID idContato) {
        if (idContato == null)
            throw new ContatoNotFoundException(idContato);
        Contato contato = this.contatos.stream()
                .filter(item -> item.getId().equals(idContato))
                .findFirst()
                .orElseThrow(() -> new ContatoNotFoundException(idContato));
        if (this.contatos.size() < 2)
            throw new ContatosVazioException();
        this.contatos.remove(contato);
    }

    public void alterarContato(Contato contato) {
        if (contato == null)
            throw new ContatoNuloException();
        if (contato.getId() == null)
            throw new ContatoNotFoundException(null);
        Contato toUpdate = this.contatos.stream()
                .filter(item -> item.getId().equals(contato.getId()))
                .findFirst()
                .orElseThrow(() -> new ContatoNotFoundException(contato.getId()));
        toUpdate.setNome(contato.getNome());
        toUpdate.setTelefone(contato.getTelefone());
        toUpdate.setEmail(contato.getEmail());
    }

    @RequiredArgsConstructor
    public static class Builder {

        private final UUID id;
        private final String nome;
        private final Cpf cpf;
        private final DataDeNascimento dataDeNascimento;
        private final List<Contato> contatos = new ArrayList<>();

        public Builder contatos(Contato contato) {
            contatos.add(contato);
            return this;
        }

        public Pessoa build() {
            Pessoa pessoa = new Pessoa(
                    id,
                    nome,
                    cpf,
                    dataDeNascimento
            );
            contatos.forEach(pessoa::adicionarContato);
            if (pessoa.getContatos().size() < 1)
                throw new ContatosVazioException();
            return pessoa;
        }
    }
}
