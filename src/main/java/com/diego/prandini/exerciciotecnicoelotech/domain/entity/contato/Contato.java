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
import lombok.Data;

import java.util.UUID;

@Data
public class Contato {

    private final UUID id;
    private String nome;
    private String telefone;
    private String email;

    public Contato(UUID id, String nome, String telefone, String email) {
        if (id == null)
            throw new IdContatoNuloException();
        if (StringUtils.isBlank(nome))
            throw new NomeVazioException();
        if (telefone == null)
            throw new TelefoneVazioException();
        if (!TelefoneUtils.isTelefoneValido(telefone))
            throw new TelefoneInvalidoException(telefone);
        if (email == null)
            throw new EmailVazioException();
        if (!EmailUtils.isEmailValido(email))
            throw new EmailInvalidoException(email);

        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
    }
}
