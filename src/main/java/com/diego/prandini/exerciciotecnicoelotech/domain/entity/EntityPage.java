package com.diego.prandini.exerciciotecnicoelotech.domain.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
public class EntityPage<T> {

    private final List<T> items;
    private final int totalPages;
    private final int pageNumber;

    public EntityPage(List<T> items, int totalPages, int pageNumber) {
        this.items = List.copyOf(items);
        this.totalPages = totalPages;
        this.pageNumber = pageNumber;
    }
}
