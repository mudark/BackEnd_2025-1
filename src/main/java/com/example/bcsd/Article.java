package com.example.bcsd;

import java.sql.Timestamp;

public class Article {

    private Integer id;
    private Integer author_id;
    private Integer board_id;
    private String title;
    private String content;
    private Timestamp createdDate;
    private Timestamp modifiedDate;


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
