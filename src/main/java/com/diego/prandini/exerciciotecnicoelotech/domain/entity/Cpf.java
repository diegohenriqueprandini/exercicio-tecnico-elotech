package com.diego.prandini.exerciciotecnicoelotech.domain.entity;

import com.diego.prandini.exerciciotecnicoelotech.exception.CpfInvalidoException;
import com.diego.prandini.exerciciotecnicoelotech.exception.CpfVazioException;
import com.diego.prandini.exerciciotecnicoelotech.utils.CpfUtils;
import com.diego.prandini.exerciciotecnicoelotech.utils.StringUtils;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Cpf {

    private final String value;

    public Cpf(String value) {
        if (value == null)
            throw new CpfVazioException();
        if (StringUtils.isBlank(value))
            throw new CpfInvalidoException(value);
        if (!CpfUtils.isCpfValido(value))
            throw new CpfInvalidoException(value);
        this.value = CpfUtils.cpfSemMascara(value);
    }

    public String get() {
        return this.value;
    }
}
