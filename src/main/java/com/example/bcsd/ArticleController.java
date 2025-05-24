package com.example.bcsd;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        ArticleDTO articleDTO;
            articleDTO=articleService.getArticle(id);
        if(articleDTO==null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(articleDTO,null,HttpStatus.OK);
    }

    @PostMapping("/articles")
    public ResponseEntity postArticle(@RequestBody HashMap map)
    {
        HttpStatus httpStatus;
        httpStatus = articleService.postArticle(map);
        return new ResponseEntity(httpStatus);
    }

    @PutMapping("/articles/{id}")
    public ResponseEntity putArticle(@PathVariable("id") String id,@RequestBody HashMap map)
    {
        HttpStatus httpStatus=articleService.putArticle(id,map);
        return new ResponseEntity(httpStatus);
    }

    @DeleteMapping("/articles/{id}")
    public ResponseEntity deleteArticle(@PathVariable("id") String id)
    {
        HttpStatus httpStatus=articleService.deleteArticle(id);
        return new ResponseEntity(httpStatus);
    }

    @GetMapping("/articles")
    public ResponseEntity getAllArticles()
    {
        return new ResponseEntity(articleService.getArticles(null),null,HttpStatus.OK);
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
        User user=articleService.getUser(id);
        if(user==null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user,null,HttpStatus.OK);
    }

    @PostMapping("/user/{id}")
    public ResponseEntity postUser(@PathVariable("id") String id, @RequestBody HashMap map)
    {
        HttpStatus httpStatus=this.articleService.postUser(id,map);
        return new ResponseEntity<>(httpStatus);
    }
    @PutMapping("/user/{id}")
    public ResponseEntity putUser(@PathVariable("id") String id, @RequestBody HashMap map)
    {
        HttpStatus httpStatus=this.articleService.putUser(id,map);
        return new ResponseEntity<>(httpStatus);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity deleteUser(@PathVariable("id") String id)
    {
        HttpStatus httpStatus=this.articleService.deleteUser(id);
        return new ResponseEntity<>(httpStatus);
    }

    @GetMapping("/board/{id}")
    public ResponseEntity getBoard(@PathVariable("id") String id)
    {
        String board=articleService.getBoard(id);
        if(board==null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(board,null,HttpStatus.OK);
    }

    @PostMapping("/Board")
    public ResponseEntity postBoard(@RequestBody HashMap map)
    {
        HttpStatus httpStatus=this.articleService.postBoard(map);
        return new ResponseEntity<>(httpStatus);
    }

    @DeleteMapping("/board/{id}")
    public ResponseEntity deleteBoard(@PathVariable("id") String id)
    {
        HttpStatus httpStatus=this.articleService.deleteBoard(id);
        return new ResponseEntity<>(httpStatus);
    }
}
