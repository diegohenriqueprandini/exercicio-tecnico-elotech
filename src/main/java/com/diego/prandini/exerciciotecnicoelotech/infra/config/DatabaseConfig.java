package com.diego.prandini.exerciciotecnicoelotech.infra.config;

import com.diego.prandini.exerciciotecnicoelotech.infra.condition.RepositoryConditionDatabase;
import com.diego.prandini.exerciciotecnicoelotech.infra.properties.DatabaseProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@Conditional(RepositoryConditionDatabase.class)
public class DatabaseConfig {

    @Bean
    public DatabaseProperties databaseProperties(DataSourceProperties dataSourceProperties) {
        return new DatabaseProperties(
                dataSourceProperties.getUrl(),
                dataSourceProperties.getUsername(),
                dataSourceProperties.getPassword()
        );
    }

    @Bean
    public DataSource database(DatabaseProperties databaseProperties) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(databaseProperties.getUrl());
        config.setUsername(databaseProperties.getUsername());
        config.setPassword(databaseProperties.getPassword());
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        return new HikariDataSource(config);
    }
}
