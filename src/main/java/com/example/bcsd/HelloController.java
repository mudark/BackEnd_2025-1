package com.example.bcsd;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Controller
public class HelloController {

    @ResponseBody
    @GetMapping("/hello")
    public String hello() {
        return "Hello World!!!!!";
    }

    @GetMapping("/hello2")
    public String hello2() {
        return "hello";
    }

    @GetMapping("/introduce")
    public String introduce(@RequestParam(
            name="name",required=false,defaultValue="김민석")
        String name, Model model)
    {
        model.addAttribute("name",name);
        return "introduce";
        // 참고 : https://it-techtree.tistory.com/entry/serving-web-content-with-springmvc
    }
    @ResponseBody
    @GetMapping("/json")
    public HashMap<String,Object> json()
    {
        HashMap<String,Object> map= new HashMap<String,Object>();
        map.put("age",26);
        map.put("name","허준기");
        return map;
        // 참조 : https://hajoung56.tistory.com/77
    }
    HashMap<Integer,Object> articleMap;
    public HelloController(){
        articleMap=new HashMap<Integer,Object>();
    }
    @ResponseBody
    @RequestMapping(value = "/article/{id}", method = RequestMethod.GET)
    public ResponseEntity getArticle(@PathVariable("id") String id) {
        if(articleMap.containsKey(Integer.parseInt(id))==false)
            return new ResponseEntity (HttpStatus.NOT_FOUND);

        Object object=articleMap.get(Integer.parseInt(id));
        HashMap<String,Object> map=new HashMap<String,Object>();
        map.put("description",object);

        return new ResponseEntity (map,HttpStatus.OK);
    }
    @ResponseBody
    @RequestMapping(value = "/article", method = RequestMethod.POST)
    public ResponseEntity postArticle(@RequestBody HashMap<String, Object> map) {
        int id=1;
        while(articleMap.containsKey(id)==true)
            id++;

        articleMap.put(id,map.get("description"));

        return new ResponseEntity (HttpStatus.OK);
    }
    @ResponseBody
    @RequestMapping(value = "/article/{id}", method = RequestMethod.PUT)
    public ResponseEntity putArticle(@PathVariable("id") String id,
                                     @RequestBody HashMap<String, Object> map) {
        if(articleMap.containsKey(Integer.parseInt(id))==false)
            return new ResponseEntity (HttpStatus.NOT_FOUND);

        articleMap.replace(Integer.parseInt(id),map.get("description"));

        return new ResponseEntity (HttpStatus.OK);
    }
    @ResponseBody
    @RequestMapping(value = "/article/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteArticle(@PathVariable("id") String id) {
        if(articleMap.containsKey(Integer.parseInt(id))==false)
            return new ResponseEntity (HttpStatus.NOT_FOUND);

        articleMap.remove(Integer.parseInt(id));

        return new ResponseEntity (HttpStatus.OK);
    }
}
