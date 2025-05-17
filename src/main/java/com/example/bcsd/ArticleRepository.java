package com.example.bcsd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class ArticleRepository {

    private JdbcTemplate jdbcTemplate;

    public ArticleRepository() {
        DataSource dataSource = new DBConfiguration().getDataSource();
        this.jdbcTemplate=new JdbcTemplate(dataSource);
    }

    public void insertArticle(Article article) {
        Timestamp timestamp=new Timestamp(System.currentTimeMillis());
        String str="INSERT INTO article "+
                "(id,author_id,board_id,title,content,created_date,modified_date)"+
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        this.jdbcTemplate.update(
                str,
                article.getID(),
                article.getAuthor_id(),
                article.getBoard_id(),
                article.getTitle(),
                article.getContent(),
                timestamp,
                timestamp);
    }

    public void updateArticle(Article article) {
        String str="UPDATE article SET "+
                "author_id=?, "+
                "board_id=?, "+
                "title=?, "+
                "content=?, "+
                "modified_time=? "+
                "WHERE id=?";
        this.jdbcTemplate.update(
                str,
                article.getAuthor_id(),
                article.getBoard_id(),
                article.getTitle(),
                article.getContent(),
                new Timestamp(System.currentTimeMillis()),
                article.getID());
    }

    public void insertUser(User user) {
        String sql="INSERT INTO Member"+
                "VALUES (?,?,?,?)";
        this.jdbcTemplate.update(
                sql,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword()
        );
    }

    public Article getArticle(Integer id) {
        List<Article> articleList=this.jdbcTemplate.query(
                "SELECT * FROM article WHERE id=?",
                new RowMapper<Article>() {
                    @Override
                    public Article mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Article article=new Article(
                                rs.getInt("id"),
                                rs.getInt("board_id"),
                                rs.getInt("author_id"),
                                rs.getString("title"),
                                rs.getString("content"),
                                rs.getTimestamp("created_date"),
                                rs.getTimestamp("modified_date")
                        );
                        return article;
                    }
                }
        ,id);
        return  articleList.get(0);
    }

    public User getUser(Integer id) {
        List<User> userList=this.jdbcTemplate.query(
                "SELECT * FROM member WHERE id=?",
                new RowMapper<User>() {
                    @Override
                    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                        User user=new User(
                                rs.getInt("id"),
                                rs.getString("name"),
                                rs.getString("email"),
                                rs.getString("password")
                        );
                        return user;
                    }
                }
        ,id);
        return userList.get(0);
    }

    public List<Article> getAllArticles()
    {
        List<Article> articleList=this.jdbcTemplate.query(
                "SELECT * FROM article",
                new RowMapper<Article>() {
                    @Override
                    public Article mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Article article=new Article(
                                rs.getInt("id"),
                                rs.getInt("board_id"),
                                rs.getInt("author_id"),
                                rs.getString("title"),
                                rs.getString("content"),
                                rs.getTimestamp("created_date"),
                                rs.getTimestamp("modified_date")
                        );
                        return article;
                    }
                }
        );
        return articleList;
    }

    public String getBoard(Integer id) {
        List<String> NameList=this.jdbcTemplate.query(
                "SELECT name FROM board WHERE id=?",
                new RowMapper<String>() {
                    @Override
                    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new String(rs.getString("name"));
                    }
                }
        ,id);
        return NameList.get(0);
    }

    public void deleteArticle(Integer id) {
        String sql="DELETE FROM article where id=?";
        this.jdbcTemplate.update(sql,id);
    }

    public void deleteUser(Integer id) {
        String str="DELETE FROM member where id=?";
        this.jdbcTemplate.update(str,id);
    }
}
