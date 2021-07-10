package com.xpllyn.service;

import com.alibaba.fastjson.JSONObject;
import com.xpllyn.pojo.ChatMessage;
import com.xpllyn.pojo.GroupMessage;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

public interface IChatService {

    void singleSend(JSONObject param, ChannelHandlerContext ctx);

    void groupSend(JSONObject param, ChannelHandlerContext ctx);

    void groupSendAll(JSONObject param, ChannelHandlerContext ctx);

    void register(JSONObject param, ChannelHandlerContext ctx);

    /**
     * 发送已读回执
     * @param param
     * @param ctx
     */
    void readReplySend(JSONObject param, ChannelHandlerContext ctx);

    void remove(ChannelHandlerContext ctx);

    void typeError(ChannelHandlerContext ctx);

    void offlineNotify(int fromUserId, ChannelHandlerContext ctx);

    void agreeResponse(JSONObject param, ChannelHandlerContext ctx);

    void insertGroupMessages(List<GroupMessage> groupMessages);

    void insertChatMessages(List<ChatMessage> chatMessages);

    List<GroupMessage> getGroupMessages();

    List<ChatMessage> getChatMessages();

    /**
     * 获取群消息最近count条消息记录
     * @param groupId
     * @param start
     * @param count
     * @return
     */
    List<GroupMessage> getLatestGroupMessage(int groupId, int start, int count);

    /**
     * 获取单聊消息最近count条消息记录
     * @param id1
     * @param id2
     * @param start
     * @param count
     * @return
     */
    List<ChatMessage> getLatestChatMessageByIds(int id1, int id2, int start, int count);
}
