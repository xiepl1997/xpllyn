package com.xpllyn.service;

import com.xpllyn.pojo.ReadMessage;

import java.sql.Timestamp;

/**
 * 读取消息回执服务接口
 * created by xiepl1997 at 2021-7-9
 */
public interface IReadMessageService {

    /**
     * 插入一条读取回执
     * @param rm
     * @return
     */
    boolean insertReadMessage(ReadMessage rm);

    /**
     * 更新一条回执
     * @param rid
     * @param sid
     * @param time
     * @return
     */
    boolean updateReadMessage(int rid, int sid, Timestamp time);

    /**
     * 获取回执
     * @param rid
     * @param sid
     * @return
     */
    ReadMessage getReadMessage(int rid, int sid);
}
