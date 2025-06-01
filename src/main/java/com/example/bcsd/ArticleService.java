package com.example.bcsd;

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
    private ArticleRepository articleRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository=articleRepository;
    }
    @Transactional(readOnly = true)
    public ArticleResponseDTO getArticle(String id)
    {
        Article article=this.articleRepository.getArticle(Integer.parseInt(id),HttpStatus.NOT_FOUND);
        return this.turnToArticleDTO(article);
    }

    @Transactional
    public HttpStatus postArticle(ArticleReqeustDTO articleReqeustDTO) {

        Article article;
        article = turnToArticle(
                null, new Timestamp(System.currentTimeMillis()), articleReqeustDTO);
        this.articleRepository.getUser(article.getAuthor_id(),HttpStatus.BAD_REQUEST);
        this.articleRepository.getBoard(article.getBoard_id(),HttpStatus.BAD_REQUEST);
        this.articleRepository.insertArticle(article);

        return HttpStatus.OK;
    }

    @Transactional
    public HttpStatus putArticle(String id_str, ArticleReqeustDTO articleReqeustDTO)
    {
        Integer id=Integer.parseInt(id_str);
        Timestamp createdDatetime=this.articleRepository
                .getArticle(id,HttpStatus.BAD_REQUEST)
                .getCreatedDate();
        Article article=this.turnToArticle(id,createdDatetime, articleReqeustDTO);
        this.articleRepository.getUser(article.getAuthor_id(),HttpStatus.BAD_REQUEST);
        this.articleRepository.getBoard(article.getBoard_id(),HttpStatus.BAD_REQUEST);
        this.articleRepository.updateArticle(article);
        return HttpStatus.OK;
    }

    @Transactional
    public HttpStatus deleteArticle(String id)
    {
        this.articleRepository.deleteArticle(Integer.parseInt(id));
        return HttpStatus.OK;
    }

    @Transactional(readOnly = true)
    public List<ArticleResponseDTO> getArticles(String board_id) {
        List<Article> articleList;
        try {
            articleList = articleRepository.getArticles("board_id",Integer.parseInt(board_id));
        } catch(NumberFormatException e) {
            articleList = articleRepository.getAllArticles();
        }
        List<ArticleResponseDTO> articleDTOList=new ArrayList<ArticleResponseDTO>();

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
            boardName=this.articleRepository
                    .getBoard(Integer.parseInt(board_id),HttpStatus.NOT_FOUND)
                    .getName();
        }
        StringBuilder articlePosts=new StringBuilder(boardName+" 게시판");
        List<ArticleResponseDTO> articleResponseDTOList=this.getArticles(board_id);

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
        return this.articleRepository.getUser(id,HttpStatus.NOT_FOUND);
    }

    @Transactional
    public HttpStatus postUser(String id,UserRequestDTO userRequestDTO)
    {
        User user=this.turnToUser(id,userRequestDTO);
        if(this.articleRepository.getUser("email",user.getEmail())!=null){
            throw(new CustomException(HttpStatus.CONFLICT,"중복되는 이메일입니다."));
        }
        this.articleRepository.insertUser(user);
        return HttpStatus.OK;
    }

    @Transactional
    public HttpStatus putUser(String id,UserRequestDTO userRequestDTO) {
        User user=this.turnToUser(id,userRequestDTO);
        if(this.articleRepository.getUser("email",user.getEmail())!=null){
            throw(new CustomException(HttpStatus.CONFLICT,"중복되는 이메일입니다."));
        }
        this.articleRepository.updateUser(user);
        return HttpStatus.OK;
    }

    @Transactional
    public HttpStatus deleteUser(String id_str)
    {
        Integer id=Integer.parseInt(id_str);
        if(this.articleRepository.getArticles("author_id",id).size()!=0){
            throw(new CustomException(
                    HttpStatus.BAD_REQUEST,
                    "해당 유저의 게시글이 남아서 해당 유저는 삭제할 수 없습니다."));
        }
        this.articleRepository.deleteUser(id);
        return HttpStatus.OK;
    }

    @Transactional
    public HttpStatus deleteBoard(String id_str) {
        Integer id=Integer.parseInt(id_str);
        if(this.articleRepository.getArticles("board_id",id).size()!=0){
            throw(new CustomException(
                    HttpStatus.BAD_REQUEST,
                    "해당 게시판에 게시글이 남아서 해당 게시판은 삭제할 수 없습니다."));
        }
        this.articleRepository.deleteBoard(id);
        return HttpStatus.OK;
    }

    @Transactional
    public BoardResponseDTO getBoard(String id_str) {
        Integer id=Integer.parseInt(id_str);
        String name=this.articleRepository
                .getBoard(id,HttpStatus.NOT_FOUND)
                .getName();
        return new BoardResponseDTO(name);
    }

    @Transactional
    public HttpStatus postBoard(String id,BoardRequestDTO boardDTO) {
        this.articleRepository
                .postBoard(new Board(Integer.parseInt(id), boardDTO.getName()));
        return HttpStatus.OK;
    }

    private Article turnToArticle(
            Integer id, Timestamp createdDate ,ArticleReqeustDTO articleReqeustDTO)
    {
        return new Article(
                id,
                Integer.parseInt(articleReqeustDTO.getAuthor_id()),
                Integer.parseInt(articleReqeustDTO.getBoard_id()),
                articleReqeustDTO.getTitle(),
                articleReqeustDTO.getContent(),
                createdDate,
                new Timestamp(System.currentTimeMillis()));
    }

    private ArticleResponseDTO turnToArticleDTO(Article article)
    {
        String author=this.articleRepository
                .getUser(article.getAuthor_id(),HttpStatus.BAD_REQUEST)
                .getName();
        String board=this.articleRepository
                .getBoard(article.getBoard_id(),HttpStatus.BAD_REQUEST)
                .getName();

        return new ArticleResponseDTO(author,board, article.getTitle(),article.getContent(),
                article.getCreatedDate().toString(),article.getModifiedDate().toString());
    }
    private User turnToUser(String id,UserRequestDTO userRequestDTO)
    {
        return new User(
                Integer.parseInt(id),
                userRequestDTO.getName(),
                userRequestDTO.getEmail(),
                userRequestDTO.getPassword()
        );
    }
}
