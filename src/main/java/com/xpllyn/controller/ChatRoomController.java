package com.xpllyn.controller;

import com.xpllyn.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ChatRoomController {

    @RequestMapping("/chatroom")
    public ModelAndView gotoChatRoom() {
        ModelAndView mv = new ModelAndView();
        //获取用户姓名
        User user = (User)SecurityUtils.getSubject().getPrincipal();
        mv.addObject("user", user);
        mv.addObject("tab_index", 2);
        mv.setViewName("chat_room");
        return mv;
    }
}
