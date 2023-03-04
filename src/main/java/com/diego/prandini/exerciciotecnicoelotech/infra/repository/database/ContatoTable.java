package com.diego.prandini.exerciciotecnicoelotech.infra.repository.database;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ContatoTable {

    private UUID id;
    private String nome;
    private String telefone;
    private String email;
}
