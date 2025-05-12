package com.example.bcsd;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ArticleService {
    private ArticleRepository articleRepository;

    public ArticleService()
    {
        this.articleRepository= new ArticleRepository();
    }

    public ArticleDTO getArticle(String id)
    {
        Article article=this.articleRepository.getArticle(Integer.parseInt(id));

        return this.turnToArticleDTO(article);
    }

    public void postArticle(HashMap<String,String> map)
    {
        this.articleRepository.putArticle(this.turnToArticle(map));
    }

    public void putArticle(String id, HashMap<String,String> map)
    {
        this.articleRepository.replaceArticle(Integer.parseInt(id),this.turnToArticle(map));
    }

    public void deleteArticle(String id)
    {
        this.articleRepository.deleteArticle(Integer.parseInt(id));
    }

    public List<ArticleDTO> getAllArticles() {
        List<Article> articleList=articleRepository.getAllArticles();
        List<ArticleDTO> articleDTOList=new ArrayList<ArticleDTO>();

        for(int i=0;i<articleList.size();i++) {
            articleDTOList.add(this.turnToArticleDTO(articleList.get(i)));
        }

        return articleDTOList;
    }

    public ModelAndView getPosts()
    {
        ModelAndView modelAndView=new ModelAndView();
        StringBuilder articlePosts=new StringBuilder("게시판");
        List<ArticleDTO> articleDTOList=this.getAllArticles();

        for(int i=0;i<articleDTOList.size();i++) {
            ArticleDTO articleDTO=articleDTOList.get(i);
            articlePosts.append("<br><br><h1>"+articleDTO.get("title")+"</h1><br>"+
                    articleDTO.get("author")+"<br>작성일시 : "+
                    articleDTO.get("postingdatetime")+"<br>수정일시 : "+
                    articleDTO.get("modifieddatetime")+"<br>게시판 : "+
                    articleDTO.get("board")+"<br>"+
                    articleDTO.get("content"));
        }
        modelAndView.setViewName("posts");
        modelAndView.addObject("articlePosts",articlePosts.toString());

        return modelAndView;
    }

    public User getUser(String id)
    {
        return this.articleRepository.getUser(Integer.parseInt(id));
    }

    public void postUser(String id,HashMap<String,String> map)
    {
        this.articleRepository.putUser(Integer.parseInt(id),
                new User(map.get("name"),map.get("email"),map.get("password")));
    }

    public void deleteUser(String id)
    {
        this.articleRepository.deleteUser(Integer.parseInt(id));
    }

    private Article turnToArticle(HashMap<String,String> map)
    {
        return new Article(Integer.parseInt(map.get("author_id"))
                ,Integer.parseInt(map.get("board_id")),
                map.get("title"),map.get("content"));
    }

    private ArticleDTO turnToArticleDTO(Article article)
    {
        String author=this.articleRepository.getUser(article.getAuthor_id()).getName();
        String board=this.articleRepository.getBoard(article.getBoard_id());

        return new ArticleDTO(author,board, article.getTitle(),article.getContent(),
                article.getPostingDateTime(),article.getModifiedDateTime());
    }
}
