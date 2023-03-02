package com.diego.prandini.exerciciotecnicoelotech.domain.entity;

import com.diego.prandini.exerciciotecnicoelotech.exception.DataDeNascimentoVaziaException;
import com.diego.prandini.exerciciotecnicoelotech.infra.system.ApplicationClock;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode
public class DataDeNascimento {

    private final LocalDate value;

    public DataDeNascimento(LocalDate value) {
        if (value == null)
            throw new DataDeNascimentoVaziaException();
        this.value = value;
    }

    public LocalDate getDate() {
        return this.value;
    }

    public boolean isFutura(ApplicationClock applicationClock) {
        return value.isAfter(applicationClock.today());
    }
}
