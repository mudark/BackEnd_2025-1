package com.example.bcsd;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class ArticleController {

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService)
    {
        this.articleService=articleService;
    }

    @GetMapping("/articles/{id}")
    public ResponseEntity getArticle(@PathVariable("id") String id)
    {
        ArticleResponseDTO articleDTO;
        articleDTO=articleService.getArticle(id);
        return new ResponseEntity(articleDTO,null,HttpStatus.OK);
    }

    @PostMapping("/articles")
    public ResponseEntity postArticle(
            @Valid @RequestBody ArticleReqeustDTO articleReqeustDTO,
            HttpSession httpSession)
    {
        HttpStatus httpStatus;
        httpStatus = articleService.postArticle(articleReqeustDTO);
        return new ResponseEntity(httpStatus);
    }

    @PutMapping("/articles/{id}")
    public ResponseEntity putArticle(
            @PathVariable("id") String id,
            @Valid @RequestBody ArticleReqeustDTO articleReqeustDTO)
    {
        HttpStatus httpStatus=articleService
                .putArticle(id, articleReqeustDTO);
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
        return new ResponseEntity(
                articleService.getArticles(null),
                null,
                HttpStatus.OK);
    }

    @GetMapping("/posts")
    public ModelAndView getAllPosts(@RequestParam(name="boardId",required=false) String boardId)
    {
        return this.articleService.getPosts(boardId);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity getUser(@PathVariable("id") String id)
    {
        User user=articleService.getUser(id);
        return new ResponseEntity<>(user,null,HttpStatus.OK);
    }

    @PostMapping("/user/{id}")
    public ResponseEntity postUser(@PathVariable("id") String id,
                                   @Valid @RequestBody UserRequestDTO userRequestDTO)
    {
        HttpStatus httpStatus=this.articleService.postUser(id,userRequestDTO);
        return new ResponseEntity<>(httpStatus);
    }
    @PutMapping("/user/{id}")
    public ResponseEntity putUser(@PathVariable("id") String id,
                                  @Valid @RequestBody UserRequestDTO userRequestDTO)
    {
        HttpStatus httpStatus=this.articleService.putUser(id,userRequestDTO);
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
        BoardResponseDTO boardResponseDTO=articleService.getBoard(id);
        return new ResponseEntity<>(boardResponseDTO,null,HttpStatus.OK);
    }

    @PostMapping("/board/{id}")
    public ResponseEntity postBoard(
            @PathVariable("id") String id,
            @Valid @RequestBody BoardRequestDTO boardRequestDTO)
    {
        HttpStatus httpStatus=this.articleService.postBoard(id,boardRequestDTO);
        return new ResponseEntity<>(httpStatus);
    }

    @DeleteMapping("/board/{id}")
    public ResponseEntity deleteBoard(@PathVariable("id") String id)
    {
        HttpStatus httpStatus=this.articleService.deleteBoard(id);
        return new ResponseEntity<>(httpStatus);
    }

    @PostMapping("/login/{id}")
    public ResponseEntity signUp(@PathVariable("id") String id_str,
                                 @Valid @RequestBody UserRequestDTO userRequestDTO,
                                 HttpSession httpSession)
    {
        this.canLogin(httpSession);
        this.articleService.postUser(id_str,userRequestDTO);
        httpSession.setAttribute("login",Integer.parseInt(id_str));
        return ResponseEntity.ok("가입이 완료되었습니다.");
    }

    @GetMapping("/login")
    public ResponseEntity login(@Valid @RequestBody LoginRequestDTO loginRequestDTO,
                                HttpSession httpSession)
    {
        this.canLogin(httpSession);
        Integer id=this.articleService.login(loginRequestDTO);
        httpSession.setAttribute("login",id);
        return ResponseEntity.ok("로그인이 완료되었습니다.");
    }

    @GetMapping("/logout")
    public ResponseEntity logout(HttpSession httpSession)
    {
        if(!this.existSession(httpSession)){
            throw new CustomException(
                    HttpStatus.BAD_REQUEST,
                    "로그인을 안 했습니다.");
        }
        httpSession.invalidate();
        return ResponseEntity.ok("로그아웃이 완료되었습니다.");
    }

    private Boolean existSession(HttpSession httpSession)
    {
        Integer id=(Integer) httpSession.getAttribute("login");
        if(id==null) {
            return false;
        }
        return true;
    }

    private void canLogin(HttpSession httpSession)
    {
        if(this.existSession(httpSession))
        {
            throw new CustomException(
                    HttpStatus.BAD_REQUEST,
                    "이미 로그인 했습니다.");
        }
    }
}
