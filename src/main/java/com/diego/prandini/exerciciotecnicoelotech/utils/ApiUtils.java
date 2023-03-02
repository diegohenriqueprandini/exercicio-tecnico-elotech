package com.diego.prandini.exerciciotecnicoelotech.utils;

import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

public class ApiUtils {

    public static URI createUri(UUID id, String resource) {
        return UriComponentsBuilder.newInstance()
                .path(String.format("%s/{id}", resource))
                .buildAndExpand(id)
                .toUri();
    }
}
