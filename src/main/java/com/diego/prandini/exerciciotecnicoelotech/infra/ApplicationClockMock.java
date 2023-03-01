package com.diego.prandini.exerciciotecnicoelotech.infra;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RequiredArgsConstructor
public class ApplicationClockMock implements ApplicationClock {

    private final LocalDate today;

    @Override
    public LocalDate today() {
        return today;
    }
}
