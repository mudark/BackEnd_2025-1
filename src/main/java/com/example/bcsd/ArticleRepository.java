package com.example.bcsd;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article,Integer> {


    /*
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public ArticleRepository() {
        //this.entityManager=entityManagerFactory.createEntityManager();
    }

    public Integer insertArticle(Article article) {
        this.entityManager.persist(article);
        return 0;
    }

    public void updateArticle(Article article){
        this.entityManager.merge(article);
    }

    public void insertUser(User user) {
        this.entityManager.persist(user);
    }

    public void updateUser(User user) {
        this.entityManager.merge(user);
    }

    public Article getArticle(Integer id,HttpStatus httpStatus) {
        Article article=this.entityManager.find(Article.class, id);
        if(article==null){
            throw new CustomException(httpStatus,"존재하지 않는 게시글입니다.");
        }
        return  article;
    }

    public User getUser(Integer id, HttpStatus httpStatus) {
        User user=this.entityManager.find(User.class, id);
        if(user==null){
            throw new CustomException(httpStatus,"존재하지 않는 유저입니다.");
        }
        return user;
    }
    public User getUser(String field, String data) {

        String sql="SELECT u FROM User u WHERE "+field+"= :data";
        List<User> userList=this.entityManager
                .createQuery(sql,User.class)
                .setParameter("data",data)
                .getResultList();
        if(userList.size()==0){
            return null;
        }
        return userList.get(0);
    }

    public List<Article> getAllArticles()
    {
        String sql="SELECT a FROM Article a";
        List<Article> articleList=this.entityManager
                .createQuery(sql,Article.class)
                .getResultList();
        return articleList;
    }

    public List<Article> getArticles(String field, Integer field_id)
    {
        String sql="SELECT a FROM Article a WHERE "+field+"= :field_id";
        List<Article> articleList=this.entityManager
                .createQuery(sql,Article.class)
                .setParameter("field_id",field_id)
                .getResultList();
        return articleList;
    }

    public Board getBoard(Integer id, HttpStatus httpStatus) {
        Board board=this.entityManager.find(Board.class,id);
        if(board==null){
            throw new CustomException(httpStatus,"존재하지 않는 게시판입니다.");
        }
        return board;
    }

    public void postBoard(Board board) {
        this.entityManager.persist(board);
    }

    public void deleteBoard(Integer id) {
        Board board=this.entityManager.find(Board.class,id);
        this.entityManager.remove(board);
    }

    public void deleteArticle(Integer id) {
        Article article=this.entityManager.find(Article.class, id);
        this.entityManager.remove(article);
    }

    public void deleteUser(Integer id) {
        User user=this.entityManager.find(User.class, id);
        this.entityManager.remove(user);
    }
    */
}
