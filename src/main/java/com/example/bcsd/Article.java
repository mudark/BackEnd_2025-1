package com.example.bcsd;

import java.time.LocalDateTime;

public class Article {

    private Integer author_id;
    private Integer board_id;
    private String title;
    private String content;
    private String postingDateTime;
    private String modifiedDateTime;


    public Article(Integer a, Integer b, String t, String c)
    {
        this.author_id=a;
        this.board_id=b;
        this.title=t;
        this.content=c;
        this.postingDateTime= LocalDateTime.now().toString();
        this.modifiedDateTime=this.getPostingDateTime();
    }

    public Integer getAuthor_id() {return this.author_id;}
    public Integer getBoard_id() {return this.board_id;}
    public String getTitle() {return this.title;}
    public String getContent() {return this.content;}

    public String getPostingDateTime() {return this.postingDateTime;}

    public void putPostingDateTime(String dateTime) {this.postingDateTime = dateTime;}

    public String getModifiedDateTime() {return this.modifiedDateTime;}
}
