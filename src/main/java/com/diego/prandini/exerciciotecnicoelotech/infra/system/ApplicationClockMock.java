package com.diego.prandini.exerciciotecnicoelotech.infra.system;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ApplicationClockMock implements ApplicationClock {

    private final LocalDateTime now;

    public ApplicationClockMock(LocalDate today) {
        this.now = today.atStartOfDay();
    }

    public ApplicationClockMock(LocalDateTime now) {
        this.now = now;
    }

    @Override
    public LocalDate today() {
        return this.now.toLocalDate();
    }

    @Override
    public LocalDateTime now() {
        return now;
    }
}
