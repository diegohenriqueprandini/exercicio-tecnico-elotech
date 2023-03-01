package com.diego.prandini.exerciciotecnicoelotech.domain.entity;

import com.diego.prandini.exerciciotecnicoelotech.infra.ApplicationClock;

import java.time.LocalDate;

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
