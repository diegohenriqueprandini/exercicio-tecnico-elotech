package com.diego.prandini.exerciciotecnicoelotech.infra.properties;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class DatabaseProperties {

    private final String url;
    private final String username;
    private final String password;
}
