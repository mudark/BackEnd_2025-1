package com.example.bcsd;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class ArticleRepository {

    private JdbcTemplate jdbcTemplate;

    public ArticleRepository() {
        DataSource dataSource = new DBConfiguration().getDataSource();
        this.jdbcTemplate=new JdbcTemplate(dataSource);
    }

    public Integer insertArticle(Article article) {
        Integer id= article.getID();
        Boolean isPosted=false;
        while(isPosted==false) {
            try {
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                String str = "INSERT INTO article " +
                        "(id,author_id,board_id,title,content,created_date,modified_date)" +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";
                this.jdbcTemplate.update(
                        str,
                        id,
                        article.getAuthor_id(),
                        article.getBoard_id(),
                        article.getTitle(),
                        article.getContent(),
                        timestamp,
                        timestamp);
                isPosted=true;
            } catch (DataIntegrityViolationException e) {
                id++;
            }
        }
        return id;
    }

    public void updateArticle(Article article) throws SQLIntegrityConstraintViolationException {
        String sql="UPDATE article SET "+
                "author_id=?, "+
                "board_id=?, "+
                "title=?, "+
                "content=?, "+
                "modified_date=? "+
                "WHERE id=?";
        this.jdbcTemplate.update(
                sql,
                article.getAuthor_id(),
                article.getBoard_id(),
                article.getTitle(),
                article.getContent(),
                new Timestamp(System.currentTimeMillis()),
                article.getID());
    }

    public void insertUser(User user) {
        String sql="INSERT INTO Member(id,name,email,password) VALUES (?,?,?,?)";
        this.jdbcTemplate.update(
                sql,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword()
        );
    }

    public void updateUser(User user) {
        String sql="UPDATE member SET "+
                "name=?, "+
                "email=?, "+
                "password=? "+
                "WHERE id=?";
        this.jdbcTemplate.update(
                sql,
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getId()
        );
    }

    public Article getArticle(Integer id) {
        List<Article> articleList=this.jdbcTemplate.query(
                "SELECT * FROM article WHERE id=?",
                new RowMapper<Article>() {
                    @Override
                    public Article mapRow(ResultSet rs, int rowNum) throws SQLException {
                         return getArticleFrom(rs);
                    }
                }
        ,id);
        if(articleList.size()==0){
            return null;
        }
        return  articleList.get(0);
    }

    public User getUser(Integer id) {
        List<User> userList=this.jdbcTemplate.query(
                "SELECT * FROM member WHERE id=?",
                new RowMapper<User>() {
                    @Override
                    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                        User user=getUserFrom(rs);
                        return user;
                    }
                }
        ,id);
        if(userList.size()==0){
            return null;
        }
        return userList.get(0);
    }
    public List<User> getUser(String field, String str) {
        List<User> userList=this.jdbcTemplate.query(
                "SELECT * FROM member WHERE "+field+"=?",
                new RowMapper<User>() {
                    @Override
                    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                        User user=getUserFrom(rs);
                        return user;
                    }
                }
                ,str);
        return userList;
    }

    public List<Article> getAllArticles()
    {
        List<Article> articleList=this.jdbcTemplate.query(
                "SELECT * FROM article",
                new RowMapper<Article>() {
                    @Override
                    public Article mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Article article=getArticleFrom(rs);
                        return article;
                    }
                }
                );
        return articleList;
    }

    public List<Article> getArticles(String field, Integer field_id)
    {
        List<Article> articleList=this.jdbcTemplate.query(
                "SELECT * FROM article WHERE "+field+"=?",
                new RowMapper<Article>() {
                    @Override
                    public Article mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return  getArticleFrom(rs);
                    }
                }
        ,field_id);
        return articleList;
    }

    public String getBoard(Integer id) {
        List<String> nameList=this.jdbcTemplate.query(
                "SELECT name FROM board WHERE id=?",
                new RowMapper<String>() {
                    @Override
                    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new String(rs.getString("name"));
                    }
                }
        ,id);
        if(nameList.size()==0){
            return null;
        }
        return nameList.get(0);
    }

    public void postBoard(Integer id, String name) {
        String sql="INSERT INTO board(id,name) VALUES (?,?)";
        this.jdbcTemplate.update(
                sql,
                id,
                name
        );
    }

    public void deleteBoard(Integer id) {
        String sql="DELETE FROM board where id=?";
        this.jdbcTemplate.update(sql,id);
    }

    public void deleteArticle(Integer id) {
        String sql="DELETE FROM article where id=?";
        this.jdbcTemplate.update(sql,id);
    }

    public void deleteUser(Integer id) {
        String str="DELETE FROM member where id=?";
        this.jdbcTemplate.update(str,id);
    }

    private Article getArticleFrom(ResultSet rs) throws SQLException {
        return new Article(
                rs.getInt("id"),
                rs.getInt("author_id"),
                rs.getInt("board_id"),
                rs.getString("title"),
                rs.getString("content"),
                rs.getTimestamp("created_date"),
                rs.getTimestamp("modified_date")
        );
    }
    private User getUserFrom(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password"));
    }
}
