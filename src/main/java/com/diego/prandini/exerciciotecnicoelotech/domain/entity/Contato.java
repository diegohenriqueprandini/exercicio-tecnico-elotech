package com.diego.prandini.exerciciotecnicoelotech.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Contato {

    private final UUID id;
    private String nome;
    private String telefone;
    private String email;
}
