package com.xpllyn.service;

import com.xpllyn.mapper.ChatMapper;
import com.xpllyn.pojo.ChatMessage;
import com.xpllyn.pojo.GroupMessage;
import com.xpllyn.utils.im.IMRedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class IMRedisService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IMRedisUtils imRedisUtils;

    @Autowired
    private ChatMapper chatMapper;

    /**
     * 插入世界频道群聊新消息
     * @param gm
     * @return
     */
    public boolean setGroupMessage(GroupMessage gm) {
        return imRedisUtils.setGroupMessage(gm, redisTemplate);
    }

    /**
     * 插入数据库中取出的消息到世界频道群聊历史记录list
     * @param gm
     * @return
     */
    public boolean setGroupMessageHistory(GroupMessage gm) {
        return imRedisUtils.setGroupMessageHistory(gm, redisTemplate);
    }

    /**
     * 获取世界频道群聊信息
     * @return
     */
    public List<GroupMessage> getGroupMessage() {
        List<GroupMessage> res = null;
        try {
            res = imRedisUtils.getGroupMessage(redisTemplate);
        } catch (Exception e) {
            log.error("【Redis】 redis error！");
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 获取世界频道历史群聊历史消息
     * @return
     */
    public List<GroupMessage> getGroupMessageHistory() {
        List<GroupMessage> gms = null;
        try {
            gms = imRedisUtils.getGroupMessageHistory(redisTemplate);
        } catch (Exception e) {
            // 如果Redis出错，则上数据库中找最近15条记录。
            gms = chatMapper.getGlobalGroupMessage15();
            log.error("【Redis】 redis error！");
            e.printStackTrace();
            return gms;
        }
        if (gms == null) {
            // 如果缓存为null，则上数据库中找最近15条记录，放入缓存中。
            gms = chatMapper.getGlobalGroupMessage15();
            if (gms != null) {
                for (GroupMessage gm : gms) {
                    setGroupMessageHistory(gm);
                }
            }
        }
        return gms;
    }

    /**
     * 插入单聊消息
     * @param cm
     * @return
     */
    public boolean setChatMessage(ChatMessage cm) {
        return imRedisUtils.setChatMessage(cm, redisTemplate);
    }

    /**
     * 插入数据库中取出的消息到单聊历史记录list
     * @param cm
     * @return
     */
    public boolean setChatMessageHistory(ChatMessage cm) {
        return imRedisUtils.setChatMessageHistory(cm, redisTemplate);
    }

    /**
     *
     * @param key
     * @return
     */
    public List<ChatMessage> getChatMessage(String key) {
        List<ChatMessage> res = null;
        try {
            res = imRedisUtils.getChatMessage(key, redisTemplate);
        } catch (Exception e) {
            log.error("【Redis】 redis error！");
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 获取单聊历史消息
     * @param key
     * @return
     */
    public List<ChatMessage> getChatMessageHistory(String key) {
        List<ChatMessage> cms = null;
        try {
            cms = imRedisUtils.getChatMessage(key, redisTemplate);
        } catch (Exception e) {
            // 如果Redis出错，则上数据库中找最近15条记录。
            String[] str = key.split("-");
            int fId = Integer.parseInt(str[0]);
            int sId = Integer.parseInt(str[1]);
            int firstId = Math.min(fId, sId);
            int secondId = Math.max(fId, sId);
            cms = chatMapper.getChatMessage15ByIds(firstId, secondId);
            log.error("【Redis】 redis error！");
            e.printStackTrace();
            return cms;
        }
        if (cms == null) {
            // 如果缓存为null，则上数据库中找最近15条记录，放入缓存中。
            String[] str = key.split("-");
            int fId = Integer.parseInt(str[0]);
            int sId = Integer.parseInt(str[1]);
            int firstId = Math.min(fId, sId);
            int secondId = Math.max(fId, sId);
            cms = chatMapper.getChatMessage15ByIds(firstId, secondId);
            if (cms != null) {
                for (ChatMessage cm : cms) {
                    setChatMessageHistory(cm);
                }
            }
        }
        return cms;
    }

    /**
     * 移除缓存
     * @param key
     */
    public void remove(String key) {
        try {
            imRedisUtils.remove(key, redisTemplate);
        } catch (Exception e) {
            log.error("【Redis】 redis error！");
            e.printStackTrace();
        }
    }

    public RedisTemplate getRedisTemplate() {
        return this.redisTemplate;
    }


}
