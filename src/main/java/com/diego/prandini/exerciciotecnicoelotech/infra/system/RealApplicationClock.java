package com.diego.prandini.exerciciotecnicoelotech.infra.system;

import java.time.LocalDate;

public class RealApplicationClock implements ApplicationClock {

    @Override
    public LocalDate today() {
        return LocalDate.now();
    }
}
