package com.xpllyn.controller;

import com.xpllyn.pojo.User;
import com.xpllyn.service.impl.UserService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ChatRoomController {

    @Autowired
    private UserService userService;

    @RequestMapping("/chatroom")
    public ModelAndView gotoChatRoom(ModelAndView mv) {
        //获取用户姓名
        User user = (User)SecurityUtils.getSubject().getPrincipal();
        mv.addObject("user", user);
        mv.addObject("tab_index", 2);
        mv.setViewName("chat_room");

        // 获取好友
        List<User> friends = userService.findFriends(user.getId());
        // 获取在线好友id
        List<Integer> onlineFriendIds = userService.findOnlineFriendIds(friends);
        
        mv.addObject("friends", friends);
        mv.addObject("online_friend_ids", onlineFriendIds);

        return mv;
    }

    @RequestMapping("/chatroom/SearchUser")
    @ResponseBody
    public List<User> searchUser(HttpServletRequest request) {
        String idOrEmail = request.getParameter("idOrEmail");
        List<User> res = userService.findByIdOrEmail(idOrEmail);
        return res;
    }
}
