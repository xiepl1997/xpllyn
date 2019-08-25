package com.xpllyn.controller;

import com.xpllyn.pojo.Message;
import com.xpllyn.service.IMessageService;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * created by xiepl1997 at 2019-8-20
 * 获取用户ip地址，将用户评论保存
 */
@Controller
@EnableAutoConfiguration
public class MessageController {

    @Autowired
    IMessageService messageService;

    @PostMapping("/insertMessage")
    @ResponseBody
    public List<Message> insertMessage(HttpServletRequest request){
        //获取用户输入留言
        String str = request.getParameter("message");
        //得到Message
        Message message = messageService.assembleMessageObject(str,null, request);
        //将数据插入数据库中
        messageService.insertMessage(message);
        //重新获取所有的message
        List<Message> messageList = messageService.getAllMessages();
        //String messageList = JSONArray.fromObject(mList).toString();
        return messageList;
    }

    @GetMapping("/getAllMessage")
    @ResponseBody
    public List<Message> getAllMessage(){
        List<Message> messageList = messageService.getAllMessages();
        return messageList;

    }

}
