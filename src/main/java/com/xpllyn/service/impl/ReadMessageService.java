package com.xpllyn.service.impl;

import com.xpllyn.mapper.ReadMessageMapper;
import com.xpllyn.pojo.ReadMessage;
import com.xpllyn.service.IReadMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * 读取消息回执服务接口
 * created by xiepl1997 at 2021-7-9
 */
@Service
@Slf4j
public class ReadMessageService implements IReadMessageService {

    @Autowired
    private ReadMessageMapper readMessageMapper = null;

    @Override
    public boolean insertReadMessage(ReadMessage rm) {
        boolean result = false;
        try {
            result = readMessageMapper.insertReadMessage(rm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean updateReadMessage(int rid, int sid, Timestamp time) {
        boolean result = false;
        try {
            result = readMessageMapper.updateReadMessage(rid, sid, time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ReadMessage getReadMessage(int rid, int sid) {
        ReadMessage readMessage = null;
        try {
            readMessage = readMessageMapper.getReadMessage(rid, sid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return readMessage;
    }
}
