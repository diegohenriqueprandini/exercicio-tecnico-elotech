package com.diego.prandini.exerciciotecnicoelotech.domain.entity;

import com.diego.prandini.exerciciotecnicoelotech.exception.PessoaCpfVazioException;
import com.diego.prandini.exerciciotecnicoelotech.exception.PessoaDataDeNascimentoFuturaException;
import com.diego.prandini.exerciciotecnicoelotech.exception.PessoaDataDeNascimentoVaziaException;
import com.diego.prandini.exerciciotecnicoelotech.exception.PessoaNomeVazioException;
import com.diego.prandini.exerciciotecnicoelotech.infra.system.ApplicationClock;
import com.diego.prandini.exerciciotecnicoelotech.utils.StringUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
public class Pessoa {

    private final UUID id;
    private String nome;
    private Cpf cpf;
    private DataDeNascimento dataDeNascimento;

    public static Pessoa of(
            UUID id,
            String nome,
            String cpf,
            LocalDate dataDeNascimento,
            ApplicationClock applicationClock
    ) {
        return new Pessoa(
                id,
                nome,
                cpf,
                dataDeNascimento,
                applicationClock
        );
    }

    private Pessoa(
            UUID id,
            String nome,
            String cpf,
            LocalDate dataDeNascimento,
            ApplicationClock applicationClock
    ) {
        this.id = id;
        setNome(nome);
        setCpf(cpf);
        setDataDeNascimento(dataDeNascimento, applicationClock);
    }

    public void setNome(String nome) {
        if (StringUtils.isBlank(nome))
            throw new PessoaNomeVazioException();
        this.nome = nome;
    }

    public void setCpf(String cpf) {
        if (StringUtils.isBlank(cpf))
            throw new PessoaCpfVazioException();
        this.cpf = new Cpf(cpf);
    }

    public void setDataDeNascimento(LocalDate dataDeNascimento, ApplicationClock applicationClock) {
        if (dataDeNascimento == null)
            throw new PessoaDataDeNascimentoVaziaException();
        DataDeNascimento dataDeNascimentoVo = new DataDeNascimento(dataDeNascimento);
        if (dataDeNascimentoVo.isFutura(applicationClock))
            throw new PessoaDataDeNascimentoFuturaException(dataDeNascimento);
        this.dataDeNascimento = dataDeNascimentoVo;
    }
}
