package com.xpllyn.utils;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 全局常量
 */
public class Constant {

    //webSocketServerHandshaker表，用channelId为键，存放握手实例。用来响应CloseWebSocketFrame的请求
    public static Map<String, WebSocketServerHandshaker> webSocketServerHandshakerMap =
            new ConcurrentHashMap<>();

    //onlineUser表，用userEmail为主键，存放在线的客户端连接上下文
    public static Map<String, ChannelHandlerContext> onlineUser =
            new ConcurrentHashMap<>();

    //用于存储每个在线用户的在线好友id
    public static Map<Integer, List<Integer>> onlineFriends =
            new ConcurrentHashMap<>();

    //id和ctx对照表
    public static Map<Integer, ChannelHandlerContext> idToCtx =
            new ConcurrentHashMap<>();

}
