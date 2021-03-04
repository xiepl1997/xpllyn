package com.xpllyn.utils;

import com.xpllyn.pojo.ChatMessage;
import com.xpllyn.pojo.GroupMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RedisUtils {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 插入群聊消息
     * @param gm
     * @return
     */
    public boolean setGroupMessage(GroupMessage gm) {
        boolean result = false;
        try {
            redisTemplate.opsForList().rightPush("group", gm);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 读取群聊消息
     * @return
     */
    public List<GroupMessage> getGroupMessage() {
        if (!exists("group")) {
            return null;
        }
        ListOperations<String, GroupMessage> ops = (ListOperations<String, GroupMessage>) redisTemplate.opsForList();
        List<GroupMessage> list = ops.range("group", 0, -1);
        return list;
    }

    /**
     * 插入单聊消息
     * @param cm
     * @return
     */
    public boolean setChatMessage(ChatMessage cm) {
        boolean result = false;
        try {
            redisTemplate.opsForList().rightPush("single", cm);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取单聊消息
     * @return
     */
    public List<ChatMessage> getChatMessage() {
        if (!exists("single")) {
            return null;
        }
        ListOperations<String, ChatMessage> ops = (ListOperations<String, ChatMessage>) redisTemplate.opsForList();
        List<ChatMessage> list = ops.range("single", 0, -1);
        return list;
    }

    /**
     * 判断是否有相应的key
     * @param key
     * @return
     */
    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 删除对应的value
     * @param key
     */
    public void remove(String key) {
        if (redisTemplate.hasKey(key)) {
            redisTemplate.delete(key);
        }
    }
}
