package com.diego.prandini.exerciciotecnicoelotech.domain;

import com.diego.prandini.exerciciotecnicoelotech.domain.entity.DataDeNascimento;
import com.diego.prandini.exerciciotecnicoelotech.infra.system.ApplicationClock;
import com.diego.prandini.exerciciotecnicoelotech.infra.system.ApplicationClockMock;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

class DataDeNascimentoTest {

    private static final LocalDate DATA_DE_NASCIMENTO_DEFAULT = LocalDate.of(1991, Month.NOVEMBER, 25);
    private static final LocalDate TODAY_MOCK = LocalDate.of(2023, Month.MARCH, 1);

    @Test
    void deveCriarDataDeNascimento() {
        DataDeNascimento dataDeNascimento = new DataDeNascimento(DATA_DE_NASCIMENTO_DEFAULT);

        assertThat(dataDeNascimento).isNotNull();
        assertThat(dataDeNascimento.get()).isEqualTo(DATA_DE_NASCIMENTO_DEFAULT);
    }

    @Test
    void dataDeNascimentoDeveSaberSeFutura() {
        ApplicationClock applicationClock = new ApplicationClockMock(TODAY_MOCK);

        DataDeNascimento dataDeNascimento = new DataDeNascimento(TODAY_MOCK.plusDays(1));

        assertThat(dataDeNascimento.isFutura(applicationClock)).isTrue();
    }

    @Test
    void dataDeNascimentoDeveSaberSeNaoFutura() {
        ApplicationClock applicationClock = new ApplicationClockMock(TODAY_MOCK);

        DataDeNascimento dataDeNascimento = new DataDeNascimento(DATA_DE_NASCIMENTO_DEFAULT);

        assertThat(dataDeNascimento.isFutura(applicationClock)).isFalse();
    }
}