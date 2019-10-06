package com.xpllyn.controller;

import com.xpllyn.pojo.Log;
import com.xpllyn.service.LogService;
import com.xpllyn.utils.MessageInfoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public Log insertLog(HttpServletRequest request){
        //String bookname = request.getParameter("bookname");
        Log log = logService.assembleLogObject(request);
        logService.insertLog(log);
        return log;
    }
}
