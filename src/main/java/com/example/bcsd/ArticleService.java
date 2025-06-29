package com.example.bcsd;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Autowired
    public ArticleService(
            ArticleRepository articleRepository,
            BoardRepository boardRepository,
            UserRepository userRepository) {
        this.articleRepository=articleRepository;
        this.boardRepository=boardRepository;
        this.userRepository=userRepository;
    }
    @Transactional(readOnly = true)
    public ArticleResponseDTO getArticle(String id_str)
    {
        Integer id=Integer.parseInt(id_str);
        return this.turnToArticleDTO(this.findActicle(id));
    }

    @Transactional
    public HttpStatus postArticle(ArticleReqeustDTO articleReqeustDTO) {

        Article article;
        article = turnToArticle(
                null,
                new Timestamp(System.currentTimeMillis()),
                articleReqeustDTO);
        this.save(article);
        return HttpStatus.OK;
    }

    @Transactional
    public HttpStatus putArticle(String id_str, ArticleReqeustDTO articleReqeustDTO)
    {
        Integer id=Integer.parseInt(id_str);
        Timestamp createdTime=this.findActicle(id).getCreatedDate();
        this.deleteArticle(id_str);
        Article article;
        article = turnToArticle(
                id,
                createdTime,
                articleReqeustDTO);
        this.save(article);
        return HttpStatus.OK;
    }

    @Transactional
    public HttpStatus deleteArticle(String id_str)
    {
        Integer id=Integer.parseInt(id_str);
        Article article=this.findActicle(id);
        Board board=article.getBoard();
        board.getArticleList().remove(article);
        this.boardRepository.save(board);
        return HttpStatus.OK;
    }

    @Transactional(readOnly = true)
    public List<ArticleResponseDTO> getArticles(String board_id_str) {
        List<Article> articleList;
        Integer board_id;
        try {
            board_id=Integer.parseInt(board_id_str);
            articleList = boardRepository
                    .findById(board_id)
                    .get()
                    .getArticleList();
        } catch(NumberFormatException e) {
            articleList = articleRepository.findAll();
        }
        List<ArticleResponseDTO> articleDTOList=new ArrayList<ArticleResponseDTO>();

        for(int i=0;i<articleList.size();i++) {
            articleDTOList.add(this.turnToArticleDTO(articleList.get(i)));
        }

        return articleDTOList;
    }

    @Transactional(readOnly = true)
    public ModelAndView getPosts(String board_id_str)
    {
        ModelAndView modelAndView=new ModelAndView();
        String boardName="전체";
        Integer board_id=Integer.parseInt(board_id_str);
        if(board_id!=null){
            boardName=this.findBoard(board_id).getName();
        }
        StringBuilder articlePosts=new StringBuilder(boardName+" 게시판");
        List<ArticleResponseDTO> articleResponseDTOList=this.getArticles(board_id_str);

        for(int i=0;i<articleResponseDTOList.size();i++) {
            ArticleResponseDTO articleDTO=articleResponseDTOList.get(i);
            articlePosts.append("<br><br><h1>"+articleDTO.getTitle()+"</h1><br>"+
                    articleDTO.getAuthor()+"<br>작성일시 : "+
                    articleDTO.getCreatedDate()+"<br>수정일시 : "+
                    articleDTO.getModifiedDate()+"<br>게시판 : "+
                    articleDTO.getBoard()+"<br>"+
                    articleDTO.getContent());
        }
        modelAndView.setViewName("posts");
        modelAndView.addObject("articlePosts",articlePosts.toString());

        return modelAndView;
    }

    @Transactional(readOnly = true)
    public User getUser(String id_str)
    {
        Integer id=Integer.parseInt(id_str);
        return this.findUser(id);
    }

    @Transactional
    public HttpStatus postUser(String id_str,UserRequestDTO userRequestDTO)
    {
        Integer id=Integer.parseInt(id_str);
        if(this.userRepository.existsById(id)){
            throw new CustomException(
                    HttpStatus.BAD_REQUEST,
                    "이미 존재하는 회원입니다."
            );
        }
        User user=this.turnToUser(id,userRequestDTO);
        this.userRepository.save(user);
        return HttpStatus.OK;
    }

    @Transactional
    public HttpStatus putUser(String id_str,UserRequestDTO userRequestDTO) {
        Integer id=Integer.parseInt(id_str);
        this.findUser(id);
        User user=this.turnToUser(id,userRequestDTO);
        this.userRepository.save(user);
        return HttpStatus.OK;
    }

    @Transactional
    public HttpStatus deleteUser(String id_str)
    {
        Integer id=Integer.parseInt(id_str);
        this.userRepository.deleteById(id);
        return HttpStatus.OK;
    }

    @Transactional
    public HttpStatus deleteBoard(String id_str) {
        Integer id=Integer.parseInt(id_str);
        this.boardRepository.deleteById(id);
        return HttpStatus.OK;
    }

    @Transactional
    public BoardResponseDTO getBoard(String id_str) {
        Integer id=Integer.parseInt(id_str);
        String name=this.findBoard(id).getName();
        return new BoardResponseDTO(name);
    }

    @Transactional
    public HttpStatus postBoard(String id,BoardRequestDTO boardDTO) {
        this.boardRepository.save(
                new Board(Integer.parseInt(id), boardDTO.getName()));
        return HttpStatus.OK;
    }

    @Transactional
    public Integer login(LoginRequestDTO loginRequestDTO) {
        String message = "아이디 또는 비밀번호가 잘못되었습니다.";
        Integer id = Integer.parseInt(loginRequestDTO.getId());
        User user = this.userRepository
                .findById(id)
                .orElseThrow(() -> new CustomException(
                        HttpStatus.NOT_FOUND,
                        message
                ));
        if (user.getPassword().equals(loginRequestDTO.getPassword()) == false) {
            throw new CustomException(HttpStatus.NOT_FOUND, message);
        }
        return user.getId();
    }

    private Article turnToArticle(
            Integer id, Timestamp createdDate ,ArticleReqeustDTO articleReqeustDTO)
    {
        Integer author_id=Integer.parseInt(articleReqeustDTO.getAuthor_id());
        User author=this.findUser(author_id);
        Integer board_id=Integer.parseInt(articleReqeustDTO.getBoard_id());
        Board board=this.findBoard(board_id);

        return new Article(
                id,
                author,
                board,
                articleReqeustDTO.getTitle(),
                articleReqeustDTO.getContent(),
                createdDate,
                new Timestamp(System.currentTimeMillis()));
    }

    private ArticleResponseDTO turnToArticleDTO(Article article)
    {
        return new ArticleResponseDTO(
                article.getAuthor().getName(),
                article.getBoard().getName(),
                article.getTitle(),
                article.getContent(),
                article.getCreatedDate().toString(),
                article.getModifiedDate().toString());
    }
    private User turnToUser(Integer id,UserRequestDTO userRequestDTO)
    {
        if(this.userRepository.existsByEmail(userRequestDTO.getEmail())){
            throw(new CustomException(HttpStatus.CONFLICT,"중복되는 이메일입니다."));
        }
        return new User(
                id,
                userRequestDTO.getName(),
                userRequestDTO.getEmail(),
                userRequestDTO.getPassword()
        );
    }

    private Article findActicle(Integer id) {
         return this.articleRepository
                .findById(id)
                .orElseThrow(()->new CustomException(
                        HttpStatus.NOT_FOUND,
                        "존재하지 않는 게시글입니다.")
                );
    }
    private User findUser(Integer id){
        return this.userRepository
                .findById(id)
                .orElseThrow(()->new CustomException(
                        HttpStatus.NOT_FOUND,
                        "존재하지 않는 회원입니다."
                ));
    }

    private Board findBoard(Integer id){
        return this.boardRepository
                .findById(id)
                .orElseThrow(()->new CustomException(
                        HttpStatus.NOT_FOUND,
                        "존재하지 않는 게시판입니다."
                ));
    }

    private void save(Article article) {
        Board board=article.getBoard();
        board.add(article);
        this.boardRepository.save(board);
    }
}
