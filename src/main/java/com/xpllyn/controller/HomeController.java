package com.xpllyn.controller;

import com.xpllyn.utils.BookUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.FileNotFoundException;
import java.util.List;

@Controller
@EnableAutoConfiguration
public class HomeController {

    @Autowired
    BookUtils bookUtil = null;

    //首页
    @RequestMapping("/")
    public ModelAndView home(){
        ModelAndView mv = new ModelAndView();

        //获取书本
        List booklist = null;
        try {
            booklist = bookUtil.getFileName("classpath:static/book");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        mv.addObject("booklist", booklist);
        mv.setViewName("homepage");
        return mv;
    }
}
