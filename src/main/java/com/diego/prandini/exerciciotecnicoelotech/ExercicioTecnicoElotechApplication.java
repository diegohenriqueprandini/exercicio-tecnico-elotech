package com.diego.prandini.exerciciotecnicoelotech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class
})
public class ExercicioTecnicoElotechApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExercicioTecnicoElotechApplication.class, args);
    }

}
