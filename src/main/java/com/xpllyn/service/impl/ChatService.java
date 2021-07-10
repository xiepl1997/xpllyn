package com.xpllyn.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xpllyn.mapper.ChatMapper;
import com.xpllyn.pojo.ChatMessage;
import com.xpllyn.pojo.Group;
import com.xpllyn.pojo.GroupMessage;
import com.xpllyn.pojo.ReadMessage;
import com.xpllyn.service.IChatService;
import com.xpllyn.service.IMRedisService;
import com.xpllyn.utils.im.ChatType;
import com.xpllyn.utils.im.Constant;
import com.xpllyn.utils.im.ResponseJson;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class ChatService implements IChatService {

    @Autowired
    private GroupService groupService;

    @Autowired
    private IMRedisService imRedisService;

    @Autowired
    private ChatMapper chatMapper;

    @Autowired
    private ReadMessageService readMessageService;

    @Override
    public void singleSend(JSONObject param, ChannelHandlerContext ctx) {
        String fromUserId = (String) param.get("fromUserId");
        String toUserId = (String) param.get("toUserId");
        String content = (String) param.get("content");
        ChannelHandlerContext toUserCtx = Constant.onlineUser.get(toUserId);

        // 保存到Redis缓存中
        Timestamp time = new Timestamp(new Date().getTime());
        ChatMessage cm = new ChatMessage(Integer.parseInt(fromUserId), Integer.parseInt(toUserId), content, time);
        try {
            imRedisService.setChatMessage(cm);
        } catch (Exception e) {
            try {
                chatMapper.insertChatMessage(cm);
                log.error("【Redis】 发送单聊消息保存到缓存时出错。发送方id：" + fromUserId + "，接收方id：" + toUserId +
                        "。发送内容：" + content + "。已保存到MySQL中");
                e.printStackTrace();
            } catch (Exception e1) {
                log.error("【Redis】 发送单聊消息保存到缓存时出错。发送方id：" + fromUserId + "，接收方id：" + toUserId +
                        "发送内容：" + content + "。保存到MySQL中出错。消息持久化失败！");
            }
        }

        // 如果对方在线，则直接推送消息给对方，使得对方即时更新ui
        if (toUserCtx == null) {
            //String responseJson = new ResponseJson().error("用户" + toUserId + "没有登录！").toString();
            //sendMessage(ctx, responseJson);
            // 对方离线，则不需要推送
        } else {
            String responseJson = new ResponseJson().success()
                    .setData("fromUserId", fromUserId)
                    .setData("content", content)
                    .setData("time", new SimpleDateFormat("M/d").format(new Date()))
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

        // 保存到Redis中
        Timestamp time = new Timestamp(new Date().getTime());
        GroupMessage gm = new GroupMessage(Integer.parseInt(toGroupId), Integer.parseInt(fromUserId), content, time);
        try {
            imRedisService.setGroupMessage(gm);
        } catch (Exception e) {
            try {
                chatMapper.insertGroupMessage(gm);
                log.error("【Redis】 发送单聊消息保存到缓存时出错。发送方id：" + fromUserId + "，群id：" + toGroupId +
                        "。发送内容：" + content + "。已保存到MySQL中");
                e.printStackTrace();
            } catch (Exception e1) {
                log.error("【Redis】 发送单聊消息保存到缓存时出错。发送方id：" + fromUserId + "，群id：" + toGroupId +
                        "。发送内容：" + content + "。保存到MySQL中出错。消息持久化失败！");
            }
        }

        Group group = groupService.getByGroupId(toGroupId);
        if (group == null) {
            String responseJson = new ResponseJson().error("该群不存在！").toString();
            sendMessage(ctx, responseJson);

        } else {
            String responseJson = new ResponseJson().success()
                    .setData("fromUserId", fromUserId)
                    .setData("content", content)
                    .setData("toGroupId", toGroupId)
                    .setData("time", new SimpleDateFormat("M/d").format(new Date()))
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

        // 保存到Redis中
        Timestamp time = new Timestamp(new Date().getTime());
        GroupMessage gm = new GroupMessage(1, Integer.parseInt(fromUserId), content, time);
        try {
            imRedisService.setGroupMessage(gm);
        } catch (Exception e) {
            try {
                chatMapper.insertGroupMessage(gm);
                log.error("【Redis】 发送单聊消息保存到缓存时出错。发送方id：" + fromUserId + "，群id：1" +
                        "。发送内容：" + content + "。已保存到MySQL中");
                e.printStackTrace();
            } catch (Exception e1) {
                log.error("【Redis】 发送单聊消息保存到缓存时出错。发送方id：" + fromUserId + "，群id：1" +
                        "。发送内容：" + content + "。保存到MySQL中出错。消息持久化失败！");
            }
        }

        String responseJson = new ResponseJson().success()
                .setData("fromUserId", fromUserId)
                .setData("content", content)
                .setData("time", new SimpleDateFormat("M/d").format(new Date()))
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
    public void readReplySend(JSONObject param, ChannelHandlerContext ctx) {
        String fromUserId = (String) param.get("fromUserId");
        String toUserId = (String) param.get("toUserId");
        Timestamp time = Timestamp.valueOf ((String) param.get("date"));

        // 发送已读回执时，更新redis中的缓存，如果redis不可用，则直接更新到数据中
        int rid = Integer.parseInt(fromUserId);
        int sid = Integer.parseInt(toUserId);
        ReadMessage rm = new ReadMessage(rid, sid, time);
        try {
            imRedisService.setReadMessage(rm);
        } catch (Exception e) {
            try {
                ReadMessage readMessage = readMessageService.getReadMessage(rid, sid);
                if (readMessage == null) {
                    // 如果数据库中没有该记录，则插入新记录
                    readMessageService.insertReadMessage(rm);
                } else {
                    // 如果数据库中有该记录，则更新
                    readMessageService.updateReadMessage(rid, sid, time);
                }
                log.error("【Redis】 redis更新已读回执时出错，发送回执id：" + rid + "。接收回执方id：" + sid +
                        "。已保存到数据库。");
            } catch (Exception e1) {
                log.error("【Redis】 redis更新已读回执时出错，发送回执id：" + rid + "。接收回执方id：" + sid +
                        "。保存到数据库过程中失败。回执丢失！");
            }
        }

        // 对方在线的话，直接推送已读回执给对方，让对方即时更新ui界面
        ChannelHandlerContext toUserCtx = Constant.onlineUser.get(toUserId);
        if (toUserCtx == null) {
            //String responseJson = new ResponseJson().error("用户" + toUserId + "没有登录！").toString();
            //sendMessage(ctx, responseJson);
            // 对方离线，则不发送，
        } else {
            String responseJson = new ResponseJson().success()
                    .setData("fromUserId", fromUserId)
                    .setData("toUserId", toUserId)
                    .setData("time", time)
                    .setData("type", ChatType.READ_REPLY_SENDING)
                    .toString();
            sendMessage(toUserCtx, responseJson);
        }
    }

    /**
     * 同意好友请求后发送消息给新好友更新UI
     * @param param
     * @param ctx
     */
    @Override
    public void agreeResponse(JSONObject param, ChannelHandlerContext ctx) {
        String fromUserId = (String) param.get("fromUserId");
        String toUserId = (String) param.get("toUserId");
        String user_icon = (String) param.get("user_icon");
        String user_name = (String) param.get("user_name");
        String time = (String) param.get("time");

        String responseJson = new ResponseJson().success()
                .setData("id", fromUserId)
                .setData("toUserId", toUserId)
                .setData("user_icon", user_icon)
                .setData("user_name", user_name)
                .setData("preview", "我们已经是好友啦，一起聊天吧！")
                .setData("time", time)
                .setData("type", ChatType.AGREE_FRIEND_REQUEST)
                .toString();
        sendMessage(Constant.onlineUser.get(toUserId), responseJson);
    }

    /**
     * redis中存储的群消息持久化
     * @param groupMessages
     */
    @Override
    @Transactional
    public void insertGroupMessages(List<GroupMessage> groupMessages) {
        for (GroupMessage groupMessage : groupMessages) {
            chatMapper.insertGroupMessage(groupMessage);
        }
    }

    /**
     * redis中存储的个人消息持久化
     * @param chatMessages
     */
    @Override
    @Transactional
    public void insertChatMessages(List<ChatMessage> chatMessages) {
        for (ChatMessage chatMessage : chatMessages) {
            chatMapper.insertChatMessage(chatMessage);
        }
    }

    @Override
    public List<GroupMessage> getGroupMessages() {
        return chatMapper.getGroupMessage();
    }

    @Override
    public List<ChatMessage> getChatMessages() {
        return chatMapper.getChatMessage();
    }

    @Override
    public List<GroupMessage> getLatestGroupMessage(int groupId, int start, int count) {
        return chatMapper.getLatestGroupMessage(groupId, start, count);
    }

    @Override
    public List<ChatMessage> getLatestChatMessageByIds(int id1, int id2, int start, int count) {
        return chatMapper.getLatestChatMessageByIds(id1, id2, start, count);
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
