package com.xpllyn.controller;

import com.xpllyn.pojo.ChatMessage;
import com.xpllyn.pojo.GroupMessage;
import com.xpllyn.pojo.User;
import com.xpllyn.service.impl.ChatService;
import com.xpllyn.service.impl.UserService;
import com.xpllyn.utils.RedisUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ChatRoomController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private ChatService chatService;

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

//        List<GroupMessage> gms = null;
//        List<ChatMessage> cms = null;
//        // 获取群聊消息
//        if (redisUtils.exists("group")) {
//            gms = redisUtils.getGroupMessage();
//        } else {
//            gms = chatService.getGroupMessages();
//            for (GroupMessage gm : gms) {
//                redisUtils.setGroupMessage(gm);
//            }
//        }

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

    /**
     * 发送一条添加好友请求
     * @param request
     * @return
     */
    @RequestMapping("/chatroom/addFriendRequest")
    @ResponseBody
    public Map<String, String> addFriendRequest(HttpServletRequest request) {
        int fromId = Integer.parseInt(request.getParameter("fromId"));
        int toId = Integer.parseInt(request.getParameter("toId"));
        List<Integer> friendIds = userService.findFriendIds(fromId);
        Map<String, String> map = new HashMap<>();
        String s = "";
        // 如果已经是好友
        if (friendIds.contains(toId)) {
            s = "isyourfriend";
            map.put("status", s);
            return map;
        }
        // 如果发送过添加好友消息
        if (userService.getAddFriendRequest(fromId, toId) != 0) {
            s = "repeat";
        } else {
            userService.sendAddFriendRequest(fromId, toId);
            s = "success";
        }
        map.put("status", s);
        return map;
    }

    /**
     * 获取发送的添加好友消息
     * @param request
     * @return
     */
    @RequestMapping("/chatroom/getAddRequest")
    @ResponseBody
    public List<User> getAddRequest(HttpServletRequest request) {
        String id = request.getParameter("id");
        return userService.getSendAddRequestUsers(Integer.parseInt(id));
    }

    /**
     * 同意添加好友请求
     * @param request
     * @return
     */
    @RequestMapping("/chatroom/agreeAddRequest")
    @ResponseBody
    public List<User> agreeAddRequest(HttpServletRequest request) {
        int toId = Integer.parseInt(request.getParameter("toId"));
        int fromId = Integer.parseInt(request.getParameter("fromId"));
        userService.agreeAddRequest(fromId, toId);
        // 保持新好友和自己，用于更新自己的UI和好友的UI
        List<User> users = new ArrayList<>();
        users.add(userService.findByUserId(String.valueOf(fromId)));
        users.add(userService.findByUserId(String.valueOf(toId)));
        return users;
    }

    /**
     * 拒绝同意添加好友请求
     * @param request
     * @return
     */
    @RequestMapping("/chatroom/disagreeAddRequest")
    @ResponseBody
    public boolean disagreeAddRequest(HttpServletRequest request) {
        int toId = Integer.parseInt(request.getParameter("toId"));
        int fromId = Integer.parseInt(request.getParameter("fromId"));
        return userService.disagreeAddRequest(fromId, toId);
    }
}
