package com.xpllyn.utils.im;

import com.xpllyn.pojo.ChatMessage;
import com.xpllyn.pojo.GroupMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class IMRedisUtils {

    /**
     * 插入世界频道群聊新消息
     * 两个缓存队列都会进行插入。但只有group这个list中的数据才会持久化到mysql
     * @param gm
     * @return
     */
    public boolean setGroupMessage(GroupMessage gm, RedisTemplate redisTemplate) {
        boolean result = false;
        try {
            redisTemplate.opsForList().rightPush("group", gm);
            redisTemplate.expire("group", 25, TimeUnit.HOURS);
            redisTemplate.opsForList().rightPush("group-history", gm);
            // 历史消息list只保存15条记录
            redisTemplate.opsForList().trim("group-history", -15, -1);
            result = true;
        } catch (Exception e) {
            log.error("【redis error】saving the GroupMessage" + gm.toString());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 插入数据库中取出的消息到世界频道群聊历史记录list
     * @param gm
     * @param redisTemplate
     * @return
     */
    public boolean setGroupMessageHistory(GroupMessage gm, RedisTemplate redisTemplate) {
        boolean result = false;
        try {
            redisTemplate.opsForList().rightPush("group-history", gm);
            result = true;
        } catch (Exception e) {
            log.error("【redis error】 saving the GroupMessage" + gm.toString());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 读取当前群聊消息（未持久化的记录）
     * @return
     */
    public List<GroupMessage> getGroupMessage(RedisTemplate redisTemplate) {
        if (!exists("group", redisTemplate)) {
            return null;
        }
        List<GroupMessage> list = redisTemplate.opsForList().range("group", 0, -1);
        return list;
    }

    /**
     * 获取历史群聊记录
     * @param redisTemplate
     * @return
     */
    public List<GroupMessage> getGroupMessageHistory(RedisTemplate redisTemplate) {
        if (!exists("group-history", redisTemplate)) {
            return null;
        }
        List<GroupMessage> list = redisTemplate.opsForList().range("group-history", 0, -1);
        return list;
    }

    /**
     * 插入单聊新消息到redis中。会插入到历史消息list和当前消息list中。历史消息list
     * 只会保存十条并且永不过期，当前消息list通过定时任务保存到MySQL之后去除。
     * 当前消息使用list来实现，其中key为双方的id组合，例如“1-2-chat”，小id在前，大id在后。
     * 历史消息list的key则为“1-2-history”.
     * 使用该list是为了减少数据库的操作，IM系统讲究即时性，聊天记录通过redis记
     * 录下来，然后再通过定时任务保存到数据库中。
     * 群聊的处理逻辑也是这样的。
     * @param cm
     * @return
     */
    public boolean setChatMessage(ChatMessage cm, RedisTemplate redisTemplate) {
        boolean result = false;
        int firstId = Math.min(cm.getFrom_user_id(), cm.getTo_user_id());
        int secondId = Math.max(cm.getFrom_user_id(), cm.getTo_user_id());
        String key = firstId + "-" + secondId + "-chat";
        String key_history = firstId + "-" + secondId + "-history";
        try {
            redisTemplate.opsForList().rightPush(key, cm);
            redisTemplate.expire(key, 25, TimeUnit.HOURS);
            // 历史消息记录不设置过期时间。
            redisTemplate.opsForList().rightPush(key_history, cm);
            // 历史消息list只保存15条记录
            redisTemplate.opsForList().trim(key_history, -15, -1);
            result = true;
        } catch (Exception e) {
            log.error("【redis error】 saving the CharMessage" + cm.toString());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 插入数据库中取出的消息到单聊聊历史记录list
     * @param cm
     * @param redisTemplate
     * @return
     */
    public boolean setChatMessageHistory(ChatMessage cm, RedisTemplate redisTemplate) {
        boolean result = false;
        int firstId = Math.min(cm.getFrom_user_id(), cm.getTo_user_id());
        int secondId = Math.max(cm.getFrom_user_id(), cm.getTo_user_id());
        String key = firstId + "-" + secondId + "-history";
        try {
            redisTemplate.opsForList().rightPush(key, cm);
            result = true;
        } catch (Exception e) {
            log.error("【redis error】 saving the GroupMessage" + cm.toString());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取单聊消息
     * @return
     */
    public List<ChatMessage> getChatMessage(String key, RedisTemplate redisTemplate) {
        if (!exists(key, redisTemplate)) {
            return null;
        }
        List<ChatMessage> list = redisTemplate.opsForList().range(key, 0, -1);
        return list;
    }

    /**
     * 判断是否有相应的key
     * @param key
     * @return
     */
    public boolean exists(String key, RedisTemplate redisTemplate) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 删除对应的value
     * @param key
     */
    public void remove(String key, RedisTemplate redisTemplate) {
        if (redisTemplate.hasKey(key)) {
            redisTemplate.delete(key);
        }
    }
}
