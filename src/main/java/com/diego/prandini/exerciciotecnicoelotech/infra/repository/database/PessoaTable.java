package com.diego.prandini.exerciciotecnicoelotech.infra.repository.database;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity(name = "TABLE_PESSOA")
@NoArgsConstructor
@AllArgsConstructor
public class PessoaTable {

    @Id
    private UUID id;
    private String nome;
    @Column(unique = true)
    private String cpf;
    @Column(name = "DATA_NASCIMENTO")
    private LocalDate dataDeNascimento;
    private String password;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "TABLE_PESSOA_CONTATOS",
            joinColumns = @JoinColumn(name = "PESSOA_ID"),
            foreignKey = @ForeignKey(name = "PESSOA_CONTATO_FK"))
    private List<ContatoTable> contatos = new ArrayList<>();
}
