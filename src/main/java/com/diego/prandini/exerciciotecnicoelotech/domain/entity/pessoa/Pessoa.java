package com.diego.prandini.exerciciotecnicoelotech.domain.entity.pessoa;

import com.diego.prandini.exerciciotecnicoelotech.domain.entity.contato.Contato;
import com.diego.prandini.exerciciotecnicoelotech.exception.ContatoNotFoundException;
import com.diego.prandini.exerciciotecnicoelotech.exception.ContatoNuloException;
import com.diego.prandini.exerciciotecnicoelotech.exception.ContatosVazioException;
import com.diego.prandini.exerciciotecnicoelotech.exception.CpfVazioException;
import com.diego.prandini.exerciciotecnicoelotech.exception.DataDeNascimentoVaziaException;
import com.diego.prandini.exerciciotecnicoelotech.exception.IdContatoNuloException;
import com.diego.prandini.exerciciotecnicoelotech.exception.IdPessoaNuloException;
import com.diego.prandini.exerciciotecnicoelotech.exception.NomeVazioException;
import com.diego.prandini.exerciciotecnicoelotech.exception.PasswordVazioException;
import com.diego.prandini.exerciciotecnicoelotech.utils.StringUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Comparator;
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
    private Password password;
    private final List<Contato> contatos = new ArrayList<>();

    public Pessoa(
            UUID id,
            String nome,
            Cpf cpf,
            DataDeNascimento dataDeNascimento,
            Password password,
            List<Contato> contatos
    ) {
        if (id == null)
            throw new IdPessoaNuloException();
        this.id = id;
        setNome(nome);
        setCpf(cpf);
        setDataDeNascimento(dataDeNascimento);
        setPassword(password);
        setContatos(contatos);
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

    public void setPassword(Password password) {
        if (password == null)
            throw new PasswordVazioException();
        this.password = password;
    }

    private void setContatos(List<Contato> contatos) {
        if (contatos == null || contatos.isEmpty())
            throw new ContatosVazioException();
        contatos.forEach(this::adicionarContato);
        if (getContatos().size() < 1)
            throw new ContatosVazioException();
    }

    public void adicionarContato(Contato contato) {
        if (contato == null)
            throw new ContatoNuloException();
        if (this.contatos.contains(contato))
            return;
        this.contatos.add(contato);
    }

    public void removerContato(UUID idContato) {
        if (idContato == null)
            throw new IdContatoNuloException();
        Contato contatoFound = buscarContato(idContato);
        if (this.contatos.size() < 2)
            throw new ContatosVazioException();
        this.contatos.remove(contatoFound);
    }

    public void alterarContato(Contato contato) {
        if (contato == null)
            throw new ContatoNuloException();
        Contato toUpdate = buscarContato(contato.getId());
        toUpdate.setNome(contato.getNome());
        toUpdate.setTelefone(contato.getTelefone());
        toUpdate.setEmail(contato.getEmail());
    }

    public Contato buscarContato(UUID id) {
        if (id == null)
            throw new IdContatoNuloException();
        return this.contatos.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ContatoNotFoundException(id));
    }

    public List<Contato> getContatos() {
        return List.copyOf(contatos.stream()
                .sorted(Comparator.comparing(Contato::getNome))
                .toList());
    }
}
