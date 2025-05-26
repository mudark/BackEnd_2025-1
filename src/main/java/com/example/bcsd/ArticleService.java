package com.example.bcsd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleService {
    private ArticleRepository articleRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository=articleRepository;
    }
    @Transactional(readOnly = true)
    public ArticleDTO getArticle(String id)
    {
        Article article=this.articleRepository.getArticle(Integer.parseInt(id),HttpStatus.NOT_FOUND);
        return this.turnToArticleDTO(article);
    }

    @Transactional
    public HttpStatus postArticle(ArticleReqeustDTO articleReqeustDTO) {

        Article article;
        article = turnToArticle(
                null, new Timestamp(System.currentTimeMillis()), articleReqeustDTO);
        this.articleRepository.getUser(article.getAuthor_id(),HttpStatus.BAD_REQUEST);
        this.articleRepository.getBoard(article.getBoard_id(),HttpStatus.BAD_REQUEST);
        this.articleRepository.insertArticle(article);

        return HttpStatus.OK;
    }

    @Transactional
    public HttpStatus putArticle(String id_str, ArticleReqeustDTO articleReqeustDTO)
    {
        Integer id=Integer.parseInt(id_str);
        Article article=this.turnToArticle(id, null, articleReqeustDTO);
        this.articleRepository.getUser(article.getAuthor_id(),HttpStatus.BAD_REQUEST);
        this.articleRepository.getBoard(article.getBoard_id(),HttpStatus.BAD_REQUEST);
        this.articleRepository.updateArticle(article);
        return HttpStatus.OK;
    }

    @Transactional
    public HttpStatus deleteArticle(String id)
    {
        this.articleRepository.deleteArticle(Integer.parseInt(id));
        return HttpStatus.OK;
    }

    @Transactional(readOnly = true)
    public List<ArticleDTO> getArticles(String board_id) {
        List<Article> articleList;
        try {
            articleList = articleRepository.getArticles("board_id",Integer.parseInt(board_id));
        } catch(NumberFormatException e) {
            articleList = articleRepository.getAllArticles();
        }
        List<ArticleDTO> articleDTOList=new ArrayList<ArticleDTO>();

        for(int i=0;i<articleList.size();i++) {
            articleDTOList.add(this.turnToArticleDTO(articleList.get(i)));
        }

        return articleDTOList;
    }

    @Transactional(readOnly = true)
    public ModelAndView getPosts(String board_id)
    {
        ModelAndView modelAndView=new ModelAndView();
        String boardName="전체";
        if(board_id!=null){
            boardName=this.articleRepository.getBoard(Integer.parseInt(board_id),HttpStatus.NOT_FOUND);
        }
        StringBuilder articlePosts=new StringBuilder(boardName+" 게시판");
        List<ArticleDTO> articleDTOList=this.getArticles(board_id);

        for(int i=0;i<articleDTOList.size();i++) {
            ArticleDTO articleDTO=articleDTOList.get(i);
            articlePosts.append("<br><br><h1>"+articleDTO.get("title")+"</h1><br>"+
                    articleDTO.get("author")+"<br>작성일시 : "+
                    articleDTO.get("createddate")+"<br>수정일시 : "+
                    articleDTO.get("modifieddate")+"<br>게시판 : "+
                    articleDTO.get("board")+"<br>"+
                    articleDTO.get("content"));
        }
        modelAndView.setViewName("posts");
        modelAndView.addObject("articlePosts",articlePosts.toString());

        return modelAndView;
    }

    @Transactional(readOnly = true)
    public User getUser(String id_str)
    {
        Integer id=Integer.parseInt(id_str);
        return this.articleRepository.getUser(id,HttpStatus.NOT_FOUND);
    }

    @Transactional
    public HttpStatus postUser(String id,UserRequestDTO userRequestDTO)
    {
        User user=this.turnToUser(id,userRequestDTO);
        this.articleRepository.insertUser(user);
        return HttpStatus.OK;
    }

    @Transactional
    public HttpStatus putUser(String id,UserRequestDTO userRequestDTO) {
        User user=this.turnToUser(id,userRequestDTO);
        if(this.articleRepository.getUser("email",user.getEmail()).size()==0){
            throw(new CustomException(HttpStatus.CONFLICT,"중복되는 이메일입니다."));
        }
        this.articleRepository.updateUser(user);
        return HttpStatus.OK;
    }

    @Transactional
    public HttpStatus deleteUser(String id_str)
    {
        Integer id=Integer.parseInt(id_str);
        if(this.articleRepository.getArticles("author_id",id).size()!=0){
            throw(new CustomException(
                    HttpStatus.BAD_REQUEST,
                    "해당 유저의 게시글이 남아서 해당 유저는 삭제할 수 없습니다."));
        }
        this.articleRepository.deleteUser(id);
        return HttpStatus.OK;
    }

    @Transactional
    public HttpStatus deleteBoard(String id_str) {
        Integer id=Integer.parseInt(id_str);
        if(this.articleRepository.getArticles("board_id",id).size()!=0){
            throw(new CustomException(
                    HttpStatus.BAD_REQUEST,
                    "해당 게시판에 게시글이 남아서 해당 게시판은 삭제할 수 없습니다."));
        }
        this.articleRepository.deleteBoard(id);
        return HttpStatus.OK;
    }

    @Transactional
    public String getBoard(String id_str) {
        Integer id=Integer.parseInt(id_str);
        return this.articleRepository.getBoard(id,HttpStatus.NOT_FOUND);
    }

    @Transactional
    public HttpStatus postBoard(String id,BoardDTO boardDTO) {
        this.articleRepository.postBoard(Integer.parseInt(id),boardDTO.getName());
        return HttpStatus.OK;
    }

    private Article turnToArticle(
            Integer id, Timestamp createdDate ,ArticleReqeustDTO articleReqeustDTO)
    {
        return new Article(
                id,
                Integer.parseInt(articleReqeustDTO.getAuthor_id()),
                Integer.parseInt(articleReqeustDTO.getBoard_id()),
                articleReqeustDTO.getTitle(),
                articleReqeustDTO.getContent(),
                createdDate,
                new Timestamp(System.currentTimeMillis()));
    }

    private ArticleDTO turnToArticleDTO(Article article)
    {
        String author=this.articleRepository.getUser(article.getAuthor_id(),HttpStatus.BAD_REQUEST).getName();
        String board=this.articleRepository.getBoard(article.getBoard_id(),HttpStatus.BAD_REQUEST);

        return new ArticleDTO(author,board, article.getTitle(),article.getContent(),
                article.getCreatedDate().toString(),article.getModifiedDate().toString());
    }
    private User turnToUser(String id,UserRequestDTO userRequestDTO)
    {
        return new User(
                Integer.parseInt(id),
                userRequestDTO.getName(),
                userRequestDTO.getEmail(),
                userRequestDTO.getPassword()
        );
    }
}
