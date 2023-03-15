package com.diego.prandini.exerciciotecnicoelotech.config;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.diego.prandini.exerciciotecnicoelotech.utils.TestUtils.doUntil;

public class PostgresDocker {

    private static final String IMAGE = "postgres:15.2-alpine";

    private final GenericContainer<?> container;
    private final String url;

    public PostgresDocker(String username, String password) {
        this.container = new GenericContainer(DockerImageName.parse(IMAGE))
                .withExposedPorts(5432)
                .withEnv("POSTGRES_USER", username)
                .withEnv("POSTGRES_PASSWORD", password)
                .withEnv("POSTGRES_DB", "exercicio-tecnico-elotech");
        this.container.start();
        this.container.waitingFor(Wait.forLogMessage(".*Container " + IMAGE + " started.*", 1));
        String host = container.getHost();
        Integer port = container.getFirstMappedPort();
        this.url = String.format("jdbc:postgresql://%s:%s/exercicio-tecnico-elotech", host, port);
        waitDbConnectionIsValid(username, password);
    }

    private void waitDbConnectionIsValid(String username, String password) {
        try (
                Connection connection = DriverManager.getConnection(url, username, password)
        ) {
            doUntil(() -> connection.isValid(10), 10000, "Postgres database connection test");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getUrl() {
        return url;
    }

    public void stop() {
        container.stop();
    }
}
