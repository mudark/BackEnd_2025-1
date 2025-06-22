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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="board_id", nullable = false)
    private Board board;

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
            User author,
            Board board,
            String title,
            String content,
            Timestamp createdDate,
            Timestamp modifiedDate)
    {
        this.id=id;
        this.author=author;
        this.board=board;
        this.title=title;
        this.content=content;
        this.createdDate=createdDate;
        this.modifiedDate=modifiedDate;
        //this.board.add(this);
        //this.author.add(this);
    }
    public Integer getID() {return this.id;}
    public User getAuthor() {return this.author;}

    public void setAuthor(User author) {
        this.author = author;
    }

    public Board getBoard() {return this.board;}

    public void setBoard(Board board) {
        this.board = board;
    }

    public String getTitle() {return this.title;}

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {return this.content;}

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreatedDate() {return this.createdDate;}

    public Timestamp getModifiedDate() {return this.modifiedDate;}

    public void setModifiedDate(Timestamp modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
