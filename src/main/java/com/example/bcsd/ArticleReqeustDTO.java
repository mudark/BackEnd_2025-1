package com.example.bcsd;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class ArticleReqeustDTO  {

    @NotBlank(message = "작성자 id는 null 값이 불가능합니다.")
    private String author_id;
    @NotBlank(message = "게시글 id는 null 값이 불가능합니다.")
    private String board_id;
    @NotBlank(message = "제목은 null 값이 불가능합니다.")
    private String title;
    @NotBlank(message = "내용은 null 값이 불가능합니다.")
    private String content;

    public ArticleReqeustDTO(
            String a,
            String b,
            String t,
            String c
    ){
        author_id=a;
        board_id=b;
        title=t;
        content=c;
    }
    public String getAuthor_id() {
        return author_id;
    }
    public String getBoard_id() {
        return board_id;
    }
    public String getTitle() {
        return title;
    }
    public String getContent() {
        return content;
    }
}
