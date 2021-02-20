package com.xpllyn.im.handler;

import com.alibaba.fastjson.JSONObject;
import com.xpllyn.pojo.User;
import com.xpllyn.service.impl.ChatService;
import com.xpllyn.service.impl.UserService;
import com.xpllyn.utils.Constant;
import com.xpllyn.utils.ResponseJson;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.*;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ChannelHandler.Sharable
public class WebSocketServerHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    @Autowired
    private ChatService chatService;

    /**
     * 读取完连接的消息后，对消息进行处理
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
        handlerWebSocketFrame(ctx, msg);
    }

    /**
     * 处理websocketframe
     * @param ctx
     * @param frame
     */
    private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        if (frame instanceof CloseWebSocketFrame) {
            WebSocketServerHandshaker handshaker =
                    Constant.webSocketServerHandshakerMap.get(ctx.channel().id().asLongText());
            if (handshaker == null) {
                sendErrorMessage(ctx, "不存在的客户端连接！");
            } else {
                handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            }
            return;
        }
        //ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        if (!(frame instanceof TextWebSocketFrame)) {
            sendErrorMessage(ctx, "仅支持文本格式，不支持二进制消息！");
            return;
        }

        // 客户端传来的消息
        String request = ((TextWebSocketFrame)frame).text();
        JSONObject param = null;
        try {
            param = JSONObject.parseObject(request);
        } catch (Exception e) {
            sendErrorMessage(ctx, "JSON字符串转换出错！");
            e.printStackTrace();
        }
        if (param == null) {
            sendErrorMessage(ctx, "参数为空！");
            return;
        }

        String type = (String) param.get("type");
        switch (type) {
            case "SINGLE_SENDING":
                chatService.singleSend(param, ctx);
                break;
            case "GROUP_SENDING":
                chatService.groupSend(param, ctx);
                break;
            case "GROUP_SENDING_ALL":
                chatService.groupSendAll(param, ctx);
                break;
            case "REGISTER":
                chatService.register(param, ctx);
                break;
            case "AGREE_FRIEND_REQUEST":
                chatService.agreeResponse(param, ctx);
            default:
                chatService.typeError(ctx);
                break;
        }
    }

    /**
     * 客户端断开连接
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        // 通知在线好友我已下线
//        User user = (User) SecurityUtils.getSubject().getPrincipal();
//        List<User> friends = userService.findFriends(user.getId());
//        List<Integer> onlineFriendIds = userService.findOnlineFriendIds(friends);
//        for (int i : onlineFriendIds) {
//            chatService.offlineNotify(user.getId(), Constant.onlineUser.get(String.valueOf(i)));
//        }
        // 去除客户端ctx
        chatService.remove(ctx);
    }

    /**
     * 异常处理，关闭channel
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private void sendErrorMessage(ChannelHandlerContext ctx, String errorMsg) {
        String responseJson = new ResponseJson().error(errorMsg).toString();
        ctx.channel().writeAndFlush(new TextWebSocketFrame(responseJson));
    }
}
