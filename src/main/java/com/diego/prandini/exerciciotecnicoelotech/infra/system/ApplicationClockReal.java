package com.diego.prandini.exerciciotecnicoelotech.infra.system;

import java.time.LocalDate;

public class ApplicationClockReal implements ApplicationClock {

    @Override
    public LocalDate today() {
        return LocalDate.now();
    }
}
