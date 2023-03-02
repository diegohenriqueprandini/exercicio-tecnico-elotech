package com.diego.prandini.exerciciotecnicoelotech.infra.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("application")
public class ApplicationProperties {

    private String title;
    private String version;
}
