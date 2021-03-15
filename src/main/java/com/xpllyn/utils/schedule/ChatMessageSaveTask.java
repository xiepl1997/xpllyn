package com.xpllyn.utils.schedule;

import com.xpllyn.pojo.ChatMessage;
import com.xpllyn.pojo.GroupMessage;
import com.xpllyn.service.impl.ChatService;
import com.xpllyn.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

//@Configuration
//@EnableScheduling
public class ChatMessageSaveTask {

//    @Autowired
//    private RedisUtils redisUtils;
//
//    @Autowired
//    private ChatService chatService;
//
//    /**
//     * 定时任务，每天凌晨3点进行redis缓存的持久化，将聊天记录保存到数据库
//     */
//    @Scheduled(cron = "0 0 3 * *")
//    public void chatMessageSave() {
//        // 获取缓存中的聊天记录
//        List<GroupMessage> gm = redisUtils.getGroupMessage();
//        List<ChatMessage> cm = redisUtils.getChatMessage();
//        // 聊天记录持久化
//        if (gm != null) {
//            chatService.insertGroupMessages(gm);
//        }
//        if (cm != null) {
//            chatService.insertChatMessages(cm);
//        }
//        // 清空缓存
//        redisUtils.remove("group");
//        redisUtils.remove("single");
//
//    }
}
