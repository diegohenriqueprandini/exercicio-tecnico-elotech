package com.diego.prandini.exerciciotecnicoelotech.domain.entity;

import com.diego.prandini.exerciciotecnicoelotech.utils.CpfUtils;

public class Cpf {

    private final String value;

    public Cpf(String value) {
        if (!CpfUtils.isCpfValido(value))
            throw new CpfInvalidoException(value);
        this.value = CpfUtils.cpfSemMascara(value);
    }

    @Override
    public String toString() {
        return this.value;
    }
}
