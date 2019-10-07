package com.xpllyn.controller;

import com.xpllyn.pojo.Log;
import com.xpllyn.service.LogService;
import com.xpllyn.utils.MessageInfoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 日志服务控制器
 * author: xiepl1997
 * date: 2019/10/06
 */
@Controller
@EnableAutoConfiguration
public class LogController {
    @Autowired
    LogService logService;

    @Autowired
    MessageInfoUtils messageInfoUtils;

    /**
     * 在用户点击了电子书下载列表后，插入一条用户日志
     * @param request
     * @return
     */
    @RequestMapping("/ebook_download")
    @ResponseBody
    public Log insertLog_ed(HttpServletRequest request){
        //String bookname = request.getParameter("bookname");
        Log log = logService.assembleLogObject(request);
        logService.insertLog(log);
        return log;
    }

    /**
     * 用户点击了首页博客后，插入一条用户日志
     * @param request
     * @return
     */
    @RequestMapping("/read_blog")
    @ResponseBody
    public Log insertLog_rb(HttpServletRequest request){
        Log log = logService.assembleLogObject(request);
        logService.insertLog(log);
        return log;
    }

    /**
     * 用户点击了查看更多博客后，插入一条用户日志
     * @param request
     * @return
     */
    @RequestMapping("/read_blog_more")
    @ResponseBody
    public Log insertLog_rmb(HttpServletRequest request){
        Log log = logService.assembleLogObject(request);
        logService.insertLog(log);
        return log;
    }
}
