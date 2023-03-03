package com.diego.prandini.exerciciotecnicoelotech.domain.entity;

import com.diego.prandini.exerciciotecnicoelotech.exception.DataDeNascimentoFuturaException;
import com.diego.prandini.exerciciotecnicoelotech.exception.DataDeNascimentoVaziaException;
import com.diego.prandini.exerciciotecnicoelotech.infra.system.clock.ApplicationClock;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@EqualsAndHashCode
public class DataDeNascimento {

    private final LocalDate value;

    public DataDeNascimento(LocalDate value, ApplicationClock applicationClock) {
        if (value == null)
            throw new DataDeNascimentoVaziaException();
        if (value.isAfter(applicationClock.today()))
            throw new DataDeNascimentoFuturaException(value);
        this.value = value;
    }

    public LocalDate get() {
        return this.value;
    }
}
