package com.diego.prandini.exerciciotecnicoelotech.domain.entity.contato;

import com.diego.prandini.exerciciotecnicoelotech.exception.EmailInvalidoException;
import com.diego.prandini.exerciciotecnicoelotech.exception.EmailVazioException;
import com.diego.prandini.exerciciotecnicoelotech.exception.IdContatoNuloException;
import com.diego.prandini.exerciciotecnicoelotech.exception.NomeVazioException;
import com.diego.prandini.exerciciotecnicoelotech.exception.TelefoneInvalidoException;
import com.diego.prandini.exerciciotecnicoelotech.exception.TelefoneVazioException;
import com.diego.prandini.exerciciotecnicoelotech.utils.EmailUtils;
import com.diego.prandini.exerciciotecnicoelotech.utils.StringUtils;
import com.diego.prandini.exerciciotecnicoelotech.utils.TelefoneUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
public class Contato {

    private final UUID id;
    private String nome;
    private String telefone;
    private String email;

    public Contato(UUID id, String nome, String telefone, String email) {
        if (id == null)
            throw new IdContatoNuloException();
        this.id = id;
        setNome(nome);
        setTelefone(telefone);
        setEmail(email);
    }

    public void setNome(String nome) {
        if (StringUtils.isBlank(nome))
            throw new NomeVazioException();
        this.nome = nome;
    }

    public void setTelefone(String telefone) {
        if (telefone == null)
            throw new TelefoneVazioException();
        if (!TelefoneUtils.isTelefoneValido(telefone))
            throw new TelefoneInvalidoException(telefone);
        this.telefone = telefone;
    }

    public void setEmail(String email) {
        if (email == null)
            throw new EmailVazioException();
        if (!EmailUtils.isEmailValido(email))
            throw new EmailInvalidoException(email);
        this.email = email;
    }
}
