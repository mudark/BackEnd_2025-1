package com.example.bcsd;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ArticleService {
    private ArticleRepository articleRepository;
    private Integer articleID;

    public ArticleService() {
        this.articleRepository= new ArticleRepository();
        this.articleID=0;
    }
    @Transactional(readOnly = true)
    public ArticleDTO getArticle(String id)
    {
        Article article=this.articleRepository.getArticle(Integer.parseInt(id));
        if(this.checkValid(article)==false) {
            return null;
        }
        return this.turnToArticleDTO(article);
    }

    @Transactional
    public HttpStatus postArticle(HashMap<String,String> map) {

        Article article;
        try {
            article = turnToArticle(this.articleID, new Timestamp(System.currentTimeMillis()), map);
        } catch (NumberFormatException e) {
            return HttpStatus.BAD_REQUEST;
        }
        System.out.println("\nboard_id : "+article.getBoard_id()+"\nauthor_id : "+article.getAuthor_id());
        if (this.checkValid(article)==false) {
            return HttpStatus.BAD_REQUEST;
        }
        this.articleID = this.articleRepository.insertArticle(article);
        return HttpStatus.OK;
    }

    @Transactional
    public HttpStatus putArticle(String id_str, HashMap<String,String> map)
    {
        Integer id=Integer.parseInt(id_str);
        Article article=this.articleRepository.getArticle(id);
        if(this.checkValid(article)==false) {
            return HttpStatus.NOT_FOUND;
        }
        try {
            article = this.turnToArticle(id, null, map);
            if(this.checkValid(article)==false) {
                return HttpStatus.BAD_REQUEST;
            }
            this.articleRepository.updateArticle(article);
        }catch(SQLIntegrityConstraintViolationException e){
            return HttpStatus.BAD_REQUEST;
        }
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
            boardName=this.articleRepository.getBoard(Integer.parseInt(board_id));
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
        return this.articleRepository.getUser(id);
    }

    @Transactional
    public HttpStatus postUser(String id,HashMap<String,String> map)
    {
        User user=new User(Integer.parseInt(id),map.get("name"),map.get("email"),map.get("password"));
        if(checkValid(user)==false) {
            return HttpStatus.BAD_REQUEST;
        }
        if(checkNoConflict(user)==false) {
            return HttpStatus.CONFLICT;
        }
        this.articleRepository.insertUser(user);
        return HttpStatus.OK;
    }

    @Transactional
    public HttpStatus putUser(String id,HashMap<String,String> map) {
        User user=new User(Integer.parseInt(id),map.get("name"),map.get("email"),map.get("password"));
        if(checkValid(user)==false) {
            return HttpStatus.BAD_REQUEST;
        }
        if(this.articleRepository.getUser(user.getId())==null) {
            return HttpStatus.NOT_FOUND;
        }
        if(checkNoConflict(user)==false) {
            return HttpStatus.CONFLICT;
        }
        this.articleRepository.updateUser(user);
        return HttpStatus.OK;
    }

    @Transactional
    public HttpStatus deleteUser(String id_str)
    {
        Integer id=Integer.parseInt(id_str);
        List<Article> articleList=this.articleRepository.getArticles("author_id",id);
        if(articleList.size()!=0){
            return HttpStatus.BAD_REQUEST;
        }
        this.articleRepository.deleteUser(id);
        return HttpStatus.OK;
    }

    @Transactional
    public HttpStatus deleteBoard(String id_str) {
        Integer id=Integer.parseInt(id_str);
        List<Article> articleList=this.articleRepository.getArticles("board_id",id);
        if(articleList.size()!=0) {
            return HttpStatus.BAD_REQUEST;
        }
        this.articleRepository.deleteBoard(id);
        return HttpStatus.OK;
    }

    @Transactional
    public String getBoard(String id_str) {
        Integer id=Integer.parseInt(id_str);
        return this.articleRepository.getBoard(id);
    }

    @Transactional
    public HttpStatus postBoard(HashMap<String,String> map) {

        if(
                map.get("id")==null||
                map.get("name")==null
        ) {
            return HttpStatus.BAD_REQUEST;
        }
        Integer id=Integer.parseInt(map.get("id"));
        String name=map.get("name");
        this.articleRepository.postBoard(id,name);
        return HttpStatus.OK;
    }

    private Article turnToArticle(Integer id, Timestamp createdDate ,HashMap<String,String> map)
    {
        return new Article(
                id,
                Integer.parseInt(map.get("author_id")),
                Integer.parseInt(map.get("board_id")),
                map.get("title"),
                map.get("content"),
                createdDate,
                new Timestamp(System.currentTimeMillis()));
    }

    private ArticleDTO turnToArticleDTO(Article article)
    {
        String author=this.articleRepository.getUser(article.getAuthor_id()).getName();
        String board=this.articleRepository.getBoard(article.getBoard_id());

        return new ArticleDTO(author,board, article.getTitle(),article.getContent(),
                article.getCreatedDate().toString(),article.getModifiedDate().toString());
    }
    private Boolean checkValid(Article article){
        try {
            if (
                    article == null ||
                    //article.getAuthor_id() == null ||
                    //article.getBoard_id() == null ||
                    article.getTitle() == null ||
                    article.getContent() == null ||
                    this.articleRepository.getUser(article.getAuthor_id()) == null ||
                    this.articleRepository.getBoard(article.getBoard_id()) == null
            ) {
                return false;
            }
        } catch(IndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }
    private Boolean checkValid(User user) {

        if (
                user == null ||
                user.getId()==null ||
                user.getName()==null ||
                user.getEmail()==null ||
                user.getPassword()==null
        ) {
            return false;
        }
        return true;
    }
    private Boolean checkNoConflict(User user) {
        List<User> emailUserList=this.articleRepository.getUser("email", user.getEmail());
        if(emailUserList.size()!=0){
            return false;
        }
        if(this.articleRepository.getUser(user.getId())!=null) {
            return false;
        }
        return true;
    }
}
