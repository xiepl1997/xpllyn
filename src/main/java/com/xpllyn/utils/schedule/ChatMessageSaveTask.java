package com.xpllyn.utils.schedule;

import com.xpllyn.pojo.ChatMessage;
import com.xpllyn.pojo.GroupMessage;
import com.xpllyn.pojo.ReadMessage;
import com.xpllyn.service.IMRedisService;
import com.xpllyn.service.impl.ChatService;
import com.xpllyn.service.impl.ReadMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Configuration
@EnableScheduling
@Slf4j
public class ChatMessageSaveTask {

    @Autowired
    private IMRedisService imRedisService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private ReadMessageService readMessageService;

    /**
     * 定时任务，每天凌晨3点进行redis缓存的持久化，将聊天记录、已读回执记录保存到数据库
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void chatMessageSave() {
        log.info("【定时任务】 开始！");

        // 获取缓存中的群聊天缓存
        List<GroupMessage> gm = imRedisService.getGroupMessage();
        // 聊天记录持久化
        if (gm != null) {
            try {
                chatService.insertGroupMessages(gm);
                // 清空缓存
                imRedisService.remove("group");
                log.info("【定时任务】 " + gm.size() + "条世界频道新聊天记录持久化到MySQL。");
                log.info("【定时任务】 " + gm.size() + "条世界频道新聊天记录缓存已清空。");
            } catch (Exception e) {
                log.error("【定时任务】 Redis出错！");
            }
        } else {
            log.info("【定时任务】 无世界频道新聊天记录持久化到MySQL。");
        }

        // 获取单聊聊天缓存
        String pattern = "*-chat";
        Set<String> keys = imRedisService.getRedisTemplate().keys(pattern);
        int cnt = 0;
        if (keys != null) {
            for (String key : keys) {
                List<ChatMessage> cm = imRedisService.getChatMessage(key);
                // 聊天记录持久化
                if (cm != null) {
                    try {
                        chatService.insertChatMessages(cm);
                        // 清空缓存
                        imRedisService.remove(key);
                        cnt += cm.size();
                    } catch (Exception e) {
                        log.error("【定时任务】 Redis出错！");
                    }
                }
            }
        }
        if (cnt != 0) {
            log.info("【定时任务】 " + cnt + "条单聊新聊天记录从Redis新消息缓存持久化到MySQL。");
            log.info("【Redis】 " + cnt + "新消息缓存已清除。");
        } else {
            log.info("【定时任务】 无单聊新聊天记录持久化到MySQL。");
        }

        // 获取redis中的已读回执
        String pattern1 = "*-readTime";
        Set<String> keys1 = imRedisService.getRedisTemplate().keys(pattern1);
        int cnt1 = 0;
        int cnt2 = 0;
        if (keys != null) {
            for (String key : keys1) {
                ReadMessage readMessage = imRedisService.getReadMessage(key);
                if (readMessage != null) {
                    int rid = readMessage.getRead_user_id();
                    int sid = readMessage.getSend_user_id();
                    Timestamp time = readMessage.getTime();
                    try {
                        ReadMessage rm = readMessageService.getReadMessage(rid, sid);
                        if (rm != null) {
                            if (rm.getTime() != time) {
                                readMessageService.updateReadMessage(rid, sid, time);
                                cnt1++;
                            } else {
                                cnt2++;
                            }
                        } else {
                            readMessageService.insertReadMessage(readMessage);
                            cnt1++;
                        }
                        // 清空缓存
                        imRedisService.remove(key);
                    } catch (Exception e) {
                        log.error("【定时任务】 Redis出错！");
                    }
                }
            }
        }
        log.info("【定时任务】 " + cnt1 + "条已读回执已插入或更新到MySQL。");
        if (cnt2 != 0) {
            log.info("【定时任务】 " + cnt2 + "条已读回执无需持久化到MySQL。");
        }
        log.info("【定时任务】 结束！");
    }
}
