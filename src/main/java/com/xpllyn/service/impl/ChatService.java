package com.xpllyn.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xpllyn.pojo.Group;
import com.xpllyn.pojo.User;
import com.xpllyn.service.IChatService;
import com.xpllyn.utils.ChatType;
import com.xpllyn.utils.Constant;
import com.xpllyn.utils.ResponseJson;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ChatService implements IChatService {

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;

    @Override
    public void singleSend(JSONObject param, ChannelHandlerContext ctx) {
        String fromUserId = (String) param.get("fromUserId");
        String toUserId = (String) param.get("toUserId");
        String content = (String) param.get("content");
        ChannelHandlerContext toUserCtx = Constant.onlineUser.get(toUserId);
        if (toUserCtx == null) {
            String responseJson = new ResponseJson().error("用户" + toUserId + "没有登录！").toString();
            sendMessage(ctx, responseJson);
        } else {
            String responseJson = new ResponseJson().success()
                    .setData("fromUserId", fromUserId)
                    .setData("content", content)
                    .setData("time", new SimpleDateFormat("M/dd").format(new Date()))
                    .setData("type", ChatType.SINGLE_SENDING)
                    .toString();
            sendMessage(toUserCtx, responseJson);
        }
    }

    @Override
    public void groupSend(JSONObject param, ChannelHandlerContext ctx) {
        String fromUserId = (String) param.get("fromUserId");
        String toGroupId = (String) param.get("toGroupId");
        String content = (String) param.get("content");

        Group group = groupService.getByGroupId(toGroupId);
        if (group == null) {
            String responseJson = new ResponseJson().error("该群不存在！").toString();
            sendMessage(ctx, responseJson);

        } else {
            String responseJson = new ResponseJson().success()
                    .setData("fromUserId", fromUserId)
                    .setData("content", content)
                    .setData("toGroupId", toGroupId)
                    .setData("time", new SimpleDateFormat("M/dd").format(new Date()))
                    .setData("type", ChatType.GROUP_SENDING)
                    .toString();

            // 获取该群中所有的用户id
            List<Integer> memberIds = groupService.getMemberByGroupId(toGroupId);
            ChannelHandlerContext tempCtx = null;
            for (Integer id : memberIds) {
                tempCtx = Constant.onlineUser.getOrDefault(id.toString(), null);
                if (tempCtx != null) {
                    sendMessage(tempCtx, responseJson);
                }
            }
        }
    }

    @Override
    public void groupSendAll(JSONObject param, ChannelHandlerContext ctx) {
        String fromUserId = (String) param.get("fromUserId");
        String content = (String) param.get("content");

        String responseJson = new ResponseJson().success()
                .setData("fromUserId", fromUserId)
                .setData("content", content)
                .setData("time", new SimpleDateFormat("M/dd").format(new Date()))
                .setData("type", ChatType.GROUP_SENDING_All)
                .toString();

        Iterator<Map.Entry<String, ChannelHandlerContext>> iterator =
                Constant.onlineUser.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ChannelHandlerContext> entry = iterator.next();
            if (!entry.getKey().equals(fromUserId)) {
                sendMessage(entry.getValue(), responseJson);
            }
        }
    }

    @Override
    public void register(JSONObject param, ChannelHandlerContext ctx) {
        String id = (String) param.get("id");
        Constant.onlineUser.put(id, ctx);
    }

    @Override
    public void remove(ChannelHandlerContext ctx) {
        Iterator<Map.Entry<String, ChannelHandlerContext>> iterator =
                Constant.onlineUser.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ChannelHandlerContext> entry = iterator.next();
            if (entry.getValue() == ctx) {
                Constant.webSocketServerHandshakerMap.remove(ctx.channel().id().asLongText());
                iterator.remove();
                break;
            }
        }
    }

    @Override
    public void typeError(ChannelHandlerContext ctx) {
        String responseJson = new ResponseJson()
                .error("该类型不存在")
                .toString();
        sendMessage(ctx, responseJson);
    }

    @Override
    public void offlineNotify(int fromUserId, ChannelHandlerContext ctx) {
        String responseJson = new ResponseJson()
                .setData("fromUserId", fromUserId)
                .setData("type", ChatType.OFFLINE_NOTIFY)
                .toString();
        sendMessage(ctx, responseJson);
    }

    private void sendMessage(ChannelHandlerContext ctx, String message) {
        ctx.channel().writeAndFlush(new TextWebSocketFrame(message));
    }
}
