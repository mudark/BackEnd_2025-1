package com.example.bcsd;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name="article")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="author_id")
    private Integer author_id;

    @Column(name="board_id")
    private Integer board_id;

    @Column(name="title")
    private String title;

    @Column(name="content")
    private String content;
    //@Temporal
    @Column(name="created_date")
    private Timestamp createdDate;

    @Column(name="modified_date")
    private Timestamp modifiedDate;

    public Article() {}

    public Article(
            Integer id,
            Integer author_id,
            Integer board_id,
            String title,
            String content,
            Timestamp createdDate,
            Timestamp modifiedDate)
    {
        this.id=id;
        this.author_id=author_id;
        this.board_id=board_id;
        this.title=title;
        this.content=content;
        this.createdDate=createdDate;
        this.modifiedDate=modifiedDate;
    }
    public Integer getID() {return this.id;}
    public Integer getAuthor_id() {return this.author_id;}
    public Integer getBoard_id() {return this.board_id;}
    public String getTitle() {return this.title;}
    public String getContent() {return this.content;}

    public Timestamp getCreatedDate() {return this.createdDate;}

    public Timestamp getModifiedDate() {return this.modifiedDate;}
}
