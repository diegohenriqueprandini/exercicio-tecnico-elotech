package com.diego.prandini.exerciciotecnicoelotech.config;

import com.diego.prandini.exerciciotecnicoelotech.infra.condition.RepositoryConditionDatabase;
import com.diego.prandini.exerciciotecnicoelotech.infra.properties.DatabaseProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@Conditional(RepositoryConditionDatabase.class)
public class PostgresConfig {

    @Bean
    @Primary
    public DatabaseProperties databasePropertiesDocker(DataSourceProperties dataSourceProperties, PostgresDocker postgresDocker) {
        return new DatabaseProperties(
                postgresDocker.getUrl(),
                dataSourceProperties.getUsername(),
                dataSourceProperties.getPassword()
        );
    }

    @Bean(destroyMethod = "stop")
    public PostgresDocker postgresDocker(DataSourceProperties dataSourceProperties) {
        return new PostgresDocker(
                dataSourceProperties.getUsername(),
                dataSourceProperties.getPassword()
        );
    }
}
