package com.example.bcsd;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="board")
public class Board {

    @Id
    @Column(name="id")
    private Integer id;

    @Column(name="name")
    private String name;

    @OneToMany(mappedBy = "board"
            ,fetch = FetchType.LAZY
            ,orphanRemoval = true
            ,cascade = CascadeType.ALL)
    private List<Article> articleList;

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

    public void add(Article article) {this.articleList.add(article);}

    public Integer indexOf(Article article) {
        return this.articleList.indexOf(article);
    }

    public void set(Integer index, Article article) {
        this.articleList.set(index,article);
    }

    public List<Article> getArticleList() {
        return articleList;
    }
}
