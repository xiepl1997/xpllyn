package com.xpllyn.service;

import com.xpllyn.pojo.Message;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 留言服务接口
 * created by xiepl1997 at 2019-8-21
 */
public interface IMessageService {

    /**
     * @param message
     * @return 插入成功返回true，否则返回false
     */
    boolean insertMessage(Message message);

    /**
     * function：组装Message对象
     * @param str 将用户输入的留言传入
     * @param pre_id 父留言id，若没有父留言则传入null
     * @param request http请求
     * @return 返回Message对象
     */
    Message assembleMessageObject(String str, String pre_id, HttpServletRequest request);

    /**
     * function:将所有的留言查询出来
     * @return
     */
    List<Message> getAllMessages();

}
