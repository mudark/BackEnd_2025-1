package com.example.bcsd;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

@RestController
public class ArticleController {

    ArticleService articleService;

    public ArticleController()
    {
        this.articleService=new ArticleService();
    }

    @GetMapping("/articles/{id}")
    public ResponseEntity getArticle(@PathVariable("id") String id)
    {
        return new ResponseEntity(articleService.getArticle(id),null,HttpStatus.OK);
    }

    @PostMapping("/articles")
    public ResponseEntity postArticle(@RequestBody HashMap map)
    {
        articleService.postArticle(map);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/articles/{id}")
    public ResponseEntity putArticle(@PathVariable("id") String id,@RequestBody HashMap map)
    {
        articleService.putArticle(id,map);

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/articles/{id}")
    public ResponseEntity deleteArticle(@PathVariable("id") String id)
    {
        articleService.deleteArticle(id);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/articles")
    public ResponseEntity getAllArticles()
    {
        return new ResponseEntity(articleService.getAllArticles(null),null,HttpStatus.OK);
    }

    @GetMapping("/posts")
    public ModelAndView getAllPosts(@RequestParam(
            name="boardId",required=false)
                                        String boardId)
    {
        return this.articleService.getPosts(boardId);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity getUser(@PathVariable("id") String id)
    {
        return new ResponseEntity<>(articleService.getUser(id),null,HttpStatus.OK);
    }

    @PostMapping("/user/{id}")
    public ResponseEntity postUser(@PathVariable("id") String id, @RequestBody HashMap map)
    {
        this.articleService.postUser(id,map);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity postUser(@PathVariable("id") String id)
    {
        this.articleService.deleteUser(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
