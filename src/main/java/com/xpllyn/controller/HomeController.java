package com.xpllyn.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@EnableAutoConfiguration
public class HomeController {

    //首页
    @RequestMapping("/")
    public String home(){
        return "homepage";
    }
}
