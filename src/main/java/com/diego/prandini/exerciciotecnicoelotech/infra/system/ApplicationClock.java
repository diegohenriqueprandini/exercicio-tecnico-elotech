package com.diego.prandini.exerciciotecnicoelotech.infra.system;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ApplicationClock {

    LocalDate today();

    LocalDateTime now();
}
