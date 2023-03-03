package com.diego.prandini.exerciciotecnicoelotech.infra.system.clock;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ApplicationClock {

    LocalDate today();

    LocalDateTime now();
}
