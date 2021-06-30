package com.xpllyn.controller;

import com.xpllyn.pojo.Book;
import com.xpllyn.pojo.Message;
import com.xpllyn.pojo.User;
import com.xpllyn.service.impl.MessageService;
import com.xpllyn.utils.BlogUtils;
import com.xpllyn.utils.BookUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class HomeController {

    @Autowired
    BookUtils bookUtil = null;

    @Autowired
    BlogUtils blogUtils = null;

    @Autowired
    MessageService messageService = null;

    //首页
    @RequestMapping("/")
    public ModelAndView home(HttpServletRequest request){

        log.info("a new visitor coming.");

        ModelAndView mv = new ModelAndView();

        //获取书本
//        List bookList = null;
        List bookList = null;
        try {
//            bookList = bookUtil.getFileName("C:\\Users\\xiepl\\Desktop");
            bookList = bookUtil.getFileName("/opt/book");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //获取最近文章标题和发表时间和url
        List blogList = null;
        blogList = blogUtils.getBlogInfo();

        //若blog数量小于10，则显示blog实际的篇数，否则显示10篇
        int blogCount = 0;
        if(blogList.size() < 10){
            blogCount = blogList.size();
        }
        else{
            blogCount = 10;
        }

        //获取最近10条评论
        List<Message> messageList = messageService.getAllMessages();
        int messageCount = messageList.size();
        if(messageCount > 10){
            messageCount = 10;
        }

        mv.addObject("blogCount",blogCount);
        mv.addObject("blogList",blogList);
        mv.addObject("bookList", bookList);
        mv.addObject("messageList",messageList);
        mv.addObject("messageCount" ,messageCount);
        mv.addObject("tab_index", 0);
        mv.setViewName("homepage");
        return mv;
    }


}
