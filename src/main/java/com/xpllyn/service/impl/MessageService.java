package com.xpllyn.service.impl;

import com.xpllyn.mapper.MessageMapper;
import com.xpllyn.pojo.Message;
import com.xpllyn.service.IMessageService;
import com.xpllyn.utils.MessageInfoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 留言服务实现类
 * created by xiepl1997 at 2019-8-21
 */
@Service
@Slf4j
public class MessageService implements IMessageService {

    @Autowired
    private MessageMapper messageMapper = null;

    @Autowired
    private MessageInfoUtils messageInfoUtils = null;

    @Override
    public boolean insertMessage(Message message) {
        boolean flag = false;
        try {
            flag = messageMapper.insert(message);
            log.info("【留言】 收到新留言:" + message.getContent());
        }
        catch (Exception e){
            log.error("【留言】 留言保存失败，留言内容：" + message.getContent());
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public Message assembleMessageObject(String str, String pre_id, HttpServletRequest request){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp time = new Timestamp(System.currentTimeMillis());

        //HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String ip = messageInfoUtils.getUserIp(request);

        String[] province_city = messageInfoUtils.getAddressByIp(ip);

        String name = "";
        if (province_city[0] == "") {
            name = "未知地区";
        }
        //如果省和市名字一样（如北京市北京市）
        else if(province_city[0].equals(province_city[1])){
            name = province_city[0];
        } else {
            name = province_city[0] + province_city[1];
        }
        name += "的朋友";

        Message message = new Message(ip ,pre_id ,name ,province_city[0] ,province_city[1] ,str);

        return message;
    }

    @Override
    public List<Message> getAllMessages() {
        List<Message> messageList = messageMapper.selectAll();
        return messageList;
    }

}
