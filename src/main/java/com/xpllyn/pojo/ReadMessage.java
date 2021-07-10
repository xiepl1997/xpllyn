package com.xpllyn.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 已读回执。
 * 记录消息接收人对消息发送人的消息的读取时间。
 * created by xiepl1997 at 2021-7-9
 */
public class ReadMessage implements Serializable {
    private int id;
    // 接收人id
    private int read_user_id;
    // 发送人id
    private int send_user_id;
    // 接收人读取发送人消息的最后时间
    private Timestamp read_time;

    public ReadMessage() {
    }

    public ReadMessage(int rid, int sid, Timestamp time) {
        this.read_user_id = rid;
        this.send_user_id = sid;
        this.read_time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRead_user_id() {
        return read_user_id;
    }

    public void setRead_user_id(int read_user_id) {
        this.read_user_id = read_user_id;
    }

    public int getSend_user_id() {
        return send_user_id;
    }

    public void setSend_user_id(int send_user_id) {
        this.send_user_id = send_user_id;
    }

    public Timestamp getTime() {
        return read_time;
    }

    public void setTime(Timestamp time) {
        this.read_time = time;
    }
}
