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

    void remove(ChannelHandlerContext ctx);

    void typeError(ChannelHandlerContext ctx);

    void offlineNotify(int fromUserId, ChannelHandlerContext ctx);

    void agreeResponse(JSONObject param, ChannelHandlerContext ctx);

    void insertGroupMessages(List<GroupMessage> groupMessages);

    void insertChatMessages(List<ChatMessage> chatMessages);

    List<GroupMessage> getGroupMessages();

    List<ChatMessage> getChatMessages();
}
