package com.xpllyn.service;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;

public interface IChatService {

    void singleSend(JSONObject param, ChannelHandlerContext ctx);

    void groupSend(JSONObject param, ChannelHandlerContext ctx);

    void groupSendAll(JSONObject param, ChannelHandlerContext ctx);

    void register(JSONObject param, ChannelHandlerContext ctx);

    void remove(ChannelHandlerContext ctx);

    void typeError(ChannelHandlerContext ctx);

    void offlineNotify(int fromUserId, ChannelHandlerContext ctx);
}
