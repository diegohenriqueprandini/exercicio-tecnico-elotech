package com.diego.prandini.exerciciotecnicoelotech.domain;

import com.diego.prandini.exerciciotecnicoelotech.domain.entity.DataDeNascimento;
import com.diego.prandini.exerciciotecnicoelotech.exception.DataDeNascimentoFuturaException;
import com.diego.prandini.exerciciotecnicoelotech.infra.system.ApplicationClock;
import com.diego.prandini.exerciciotecnicoelotech.infra.system.ApplicationClockMock;
import com.diego.prandini.exerciciotecnicoelotech.utils.DateUtils;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class DataDeNascimentoTest {

    private static final LocalDate TODAY_MOCK = LocalDate.of(2023, Month.MARCH, 1);
    private static final LocalDate DATA_DE_NASCIMENTO_DEFAULT = LocalDate.of(1991, Month.NOVEMBER, 25);

    @Test
    void deveCriarDataDeNascimento() {
        ApplicationClock applicationClock = new ApplicationClockMock(TODAY_MOCK);

        DataDeNascimento dataDeNascimento = new DataDeNascimento(DATA_DE_NASCIMENTO_DEFAULT, applicationClock);

        assertThat(dataDeNascimento.get()).isEqualTo(DATA_DE_NASCIMENTO_DEFAULT);
    }

    @Test
    void dataDeNascimentoNaoPodeSerFutura() {
        ApplicationClock applicationClock = new ApplicationClockMock(TODAY_MOCK);
        LocalDate dataDeNascimento = TODAY_MOCK.plusDays(1);
        Throwable throwable = catchThrowable(() -> new DataDeNascimento(dataDeNascimento, applicationClock));

        assertThat(throwable).isInstanceOf(DataDeNascimentoFuturaException.class);
        assertThat(throwable.getMessage()).isEqualTo("Data de nascimento não pode ser futura: " + DateUtils.toString(dataDeNascimento));
    }

    @Test
    void dataDeNascimentoPodeSerHoje() {
        ApplicationClock applicationClock = new ApplicationClockMock(TODAY_MOCK);

        DataDeNascimento dataDeNascimento = new DataDeNascimento(TODAY_MOCK, applicationClock);

        assertThat(dataDeNascimento.get()).isEqualTo(TODAY_MOCK);
    }
}