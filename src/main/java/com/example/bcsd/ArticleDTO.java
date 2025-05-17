package com.example.bcsd;

import java.util.HashMap;

public class ArticleDTO extends HashMap {

    public ArticleDTO(String author, String board, String title,
                      String content, String createdDate, String modifiedDate)
    {
        this.put("author",author);
        this.put("board",board);
        this.put("title",title);
        this.put("content",content);
        this.put("createddate",createdDate);
        this.put("modifieddate",modifiedDate);
    }
}
