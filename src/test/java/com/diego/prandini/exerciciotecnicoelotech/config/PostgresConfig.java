package com.diego.prandini.exerciciotecnicoelotech.config;

import com.diego.prandini.exerciciotecnicoelotech.infra.condition.RepositoryDatabaseCondition;
import com.diego.prandini.exerciciotecnicoelotech.infra.properties.DatabaseProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Conditional(RepositoryDatabaseCondition.class)
public class PostgresConfig {

    @Bean
    @Profile("dbtest")
    public DatabaseProperties databaseProperties(DataSourceProperties dataSourceProperties, PostgresDocker postgresDocker) {
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
