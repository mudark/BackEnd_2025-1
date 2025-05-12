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
}
