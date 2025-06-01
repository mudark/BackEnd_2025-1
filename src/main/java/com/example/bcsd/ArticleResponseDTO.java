package com.example.bcsd;

public class ArticleResponseDTO {

    private String author;
    private String board;
    private String title;
    private String content;

    private String createdDate;

    private String modifiedDate;

    public ArticleResponseDTO(String author, String board, String title,
                      String content, String createdDate, String modifiedDate)
    {
        this.author=author;
        this.board=board;
        this.title=title;
        this.content=content;
        this.createdDate=createdDate;
        this.modifiedDate=modifiedDate;
    }

    public String getAuthor() {
        return author;
    }

    public String getBoard() {
        return board;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }
}
