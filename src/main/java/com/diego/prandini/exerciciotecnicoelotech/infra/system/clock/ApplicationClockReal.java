package com.diego.prandini.exerciciotecnicoelotech.infra.system.clock;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class ApplicationClockReal implements ApplicationClock {

    @Override
    public LocalDate today() {
        return LocalDate.now();
    }

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
