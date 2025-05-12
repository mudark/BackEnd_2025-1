package com.example.bcsd;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ArticleRepository {

    private Map<Integer,Article> articleMap;
    private Map<Integer,User> userMap;
    private Map<Integer,String> boardMap;

    public ArticleRepository() {
        articleMap=new HashMap<>();
        userMap=new HashMap<>();
        boardMap=new HashMap<>();

        boardMap.put(1,"기타");
        boardMap.put(2,"경제");
        boardMap.put(3,"기술");

        userMap.put(1,new User("관리자","admin@admin.com","admin"));
    }

    public void putArticle(Article article) {
        Integer i=1;
        while(articleMap.containsKey(i)==true)
            i++;
        articleMap.put(i,article);
    }

    public void replaceArticle(Integer id, Article article) {
        article.putPostingDateTime(articleMap.get(id).getPostingDateTime());

        articleMap.replace(id,article);
    }

    public void putUser(Integer id, User user) {
        userMap.put(id, user);
    }

    public Article getArticle(Integer id) {
        return articleMap.get(id);
    }

    public User getUser(Integer id) {
        return userMap.get(id);
    }

    public List<Article> getAllArticles()
    {
        List<Article> articleList=new ArrayList<Article>();

        articleList.addAll(articleMap.values());

        return articleList;
    }

    public String getBoard(Integer id)
    {
        return boardMap.get(id);
    }

    public void deleteArticle(Integer id)
    {
        articleMap.remove(id);
    }

    public void deleteUser(Integer id)
    {
        userMap.remove(id);
    }
}
