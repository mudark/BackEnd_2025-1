package com.example.bcsd;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="board")
public class Board {

    @Id
    @Column(name="id")
    private Integer id;

    @Column(name="name")
    private String name;

    public Board() {}

    public Board(Integer id, String name) {
        this.id=id;
        this.name=name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
