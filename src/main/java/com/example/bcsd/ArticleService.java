package com.example.bcsd;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

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
        return this.turnToArticleDTO(article);
    }

    @Transactional
    public void postArticle(HashMap<String,String> map) {

        this.articleID=this.articleRepository.insertArticle(
                this.turnToArticle(this.articleID, null, map));
    }

    @Transactional
    public void putArticle(String id, HashMap<String,String> map)
    {
        this.articleRepository.updateArticle(
                this.turnToArticle(Integer.parseInt(id),null,map));
    }

    @Transactional
    public void deleteArticle(String id)
    {
        this.articleRepository.deleteArticle(Integer.parseInt(id));
    }

    @Transactional(readOnly = true)
    public List<ArticleDTO> getAllArticles(String board_id) {
        List<Article> articleList;
        try {
            articleList = articleRepository.getBoardArticles(Integer.parseInt(board_id));
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
        List<ArticleDTO> articleDTOList=this.getAllArticles(board_id);

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
    public User getUser(String id)
    {
        return this.articleRepository.getUser(Integer.parseInt(id));
    }

    @Transactional
    public void postUser(String id,HashMap<String,String> map)
    {
        this.articleRepository.insertUser(
                new User(Integer.parseInt(id),map.get("name"),map.get("email"),map.get("password")));
    }

    @Transactional
    public void deleteUser(String id)
    {
        this.articleRepository.deleteUser(Integer.parseInt(id));
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
}
