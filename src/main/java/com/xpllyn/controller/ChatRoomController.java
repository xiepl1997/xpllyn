package com.xpllyn.controller;

import com.xpllyn.pojo.ChatMessage;
import com.xpllyn.pojo.GroupMessage;
import com.xpllyn.pojo.User;
import com.xpllyn.service.IMRedisService;
import com.xpllyn.service.impl.ChatService;
import com.xpllyn.service.impl.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@Slf4j
public class ChatRoomController {

    @Autowired
    private UserService userService;

    @Autowired
    private IMRedisService imRedisService;

    @Autowired
    private ChatService chatService;

    @RequestMapping("/chatroom")
    public ModelAndView gotoChatRoom(ModelAndView mv) {
        //获取用户姓名
        User user = (User)SecurityUtils.getSubject().getPrincipal();
        mv.addObject("user", user);
        mv.addObject("tab_index", 2);
        mv.setViewName("chat_room");

        log.info("【chatroom】 " + user.getUser_name() + "进入聊天室。");

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
        // 从redis获取世界频道历史消息和与各个好友之间的历史消息缓存
        List<GroupMessage> globalChatHistory = getGroupMessageHistory();
        Map<Integer, List<ChatMessage>> friendChatHistory = getChatMessageHistory(user, friends);
        // TODO: 2021/7/1 用获取到的聊天缓存记录渲染前端

        // 获取群聊消息的最后一条内容和时间，用于渲染聊天室左侧列表
        String lastGlobalMessage = null;
        String lastGlobalMessageTime = null;
        if (globalChatHistory != null && globalChatHistory.size() > 0) {
            String content = globalChatHistory.get(globalChatHistory.size() - 1).getContent();
            if (content.length() > 16) {
                content = content.substring(0, 16) + "...";
            }
            lastGlobalMessage = content;

            SimpleDateFormat format = new SimpleDateFormat("M/d");
            lastGlobalMessageTime = format.format(globalChatHistory.get(globalChatHistory.size() - 1).getCreate_time());
        }

        // 每个好友的最后一条消息和时间，用于渲染左侧列表
        Map<Integer, String> lastFriendChat = new HashMap<>();
        Map<Integer, String> lastFriendChatTime = new HashMap<>();
        if (friendChatHistory != null) {
            for (Map.Entry<Integer, List<ChatMessage>> entry : friendChatHistory.entrySet()) {
                String chat = null;
                String time = null;
                if (entry.getValue().size() != 0) {
                    ChatMessage tempLast = entry.getValue().get(entry.getValue().size() - 1);
                    String content = tempLast.getContent();
                    if (content.length() > 16) {
                        content = content.substring(0, 16) + "...";
                    }
                    chat = content;

                    SimpleDateFormat format = new SimpleDateFormat("M/d");
                    time = format.format(tempLast.getCreate_time());
                }
                lastFriendChat.put(entry.getKey(), chat);
                lastFriendChatTime.put(entry.getKey(), time);
            }
        }

        mv.addObject("friends", friends);
        mv.addObject("online_friend_ids", onlineFriendIds);
        mv.addObject("globalChatHistory", globalChatHistory);
        mv.addObject("friendChatHistory", friendChatHistory);
        mv.addObject("lastGlobalMessage", lastGlobalMessage);
        mv.addObject("lastFriendChat", lastFriendChat);
        mv.addObject("lastGlobalMessageTime", lastGlobalMessageTime);
        mv.addObject("lastFriendChatTime", lastFriendChatTime);

        return mv;
    }

    public List<GroupMessage> getGroupMessageHistory() {
        return imRedisService.getGroupMessageHistory();
    }

    /**
     * 获取当前用户和每一个好友的最近聊天记录（15条）
     * @param user
     * @param friends
     * @return
     */
    public Map<Integer, List<ChatMessage>> getChatMessageHistory(User user, List<User> friends) {
        Map<Integer, List<ChatMessage>> res = new HashMap<>();
        for (User friend : friends) {
            int firstId = Math.min(user.getId(), friend.getId());
            int secondId = Math.max(user.getId(), friend.getId());
            String key = firstId + "-" + secondId + "-history";
            List<ChatMessage> cms = imRedisService.getChatMessageHistory(key);
            if (cms != null) {
                res.put(friend.getId(), cms);
            } else {
                res.put(friend.getId(), new ArrayList<>());
            }
        }
        return res;
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

    /**
     * 获取群历史记录
     * @param request
     * @return
     */
    @RequestMapping("/chatroom/getGlobalHistory")
    @ResponseBody
    public List<GroupMessage> getLatestGroupMessage(HttpServletRequest request) {
        // TODO: 2021/7/3 获取群历史记录，情况分为第一次获取和非第一次获取
        // 每次从start开始，获取count条最近聊天记录
        // 1.start = 0，说明是第一次获取记录，则需要把从数据库获取的记录和新消息缓存队列一起返回给Ajax，
        // 让Ajax把聊天区域的html覆盖。
        // 2.start != 0，说明不是第一次获取记录，则只需要把当前分页获取到的记录返回给Ajax，追加在聊天
        // 区域的html之前。
        int start = Integer.parseInt(request.getParameter("start"));
        int count = Integer.parseInt(request.getParameter("count"));
        int groupId = Integer.parseInt(request.getParameter("groupId"));
        List<GroupMessage> res = chatService.getLatestGroupMessage(groupId, start, count);
        if (start == 0) {
            List<GroupMessage> groupChatCache = imRedisService.getGroupMessage();
            if (groupChatCache != null && groupChatCache.size() != 0) {
                res.addAll(groupChatCache);
            }
        }
        return res;
    }

    /**
     * 获取单聊历史记录
     * @param request
     * @return
     */
    @RequestMapping("/chatroom/getFriendChatHistory")
    @ResponseBody
    public List<ChatMessage> getLatestChatMessage(HttpServletRequest request) {
        // TODO: 2021/7/4 获单群历史记录，情况分为第一次获取和非第一次获取
        // 逻辑同getLatestGroupMessage()
        int uid = Integer.parseInt(request.getParameter("uid"));
        int fid = Integer.parseInt(request.getParameter("fid"));
        int start = Integer.parseInt(request.getParameter("start"));
        int count = Integer.parseInt(request.getParameter("count"));
        List<ChatMessage> res = chatService.getLatestChatMessageByIds(uid, fid, start, count);
        if (start == 0) {
            int firstId = Math.min(uid, fid);
            int secondId = Math.max(uid, fid);
            String key = firstId + "-" + secondId + "-chat";
            List<ChatMessage> chatCache = imRedisService.getChatMessage(key);
            if (chatCache != null && chatCache.size() != 0) {
                res.addAll(chatCache);
            }
        }
        return res;
    }
}
