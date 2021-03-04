package com.xpllyn.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 世界频道聊天记录
 */
public class GroupMessage implements Serializable {
    private int id;
    private int group_id;
    private int user_id;
    private String content;
    private Timestamp create_time;

    public GroupMessage() {}

    public GroupMessage(int group_id, int user_id, String content, Timestamp create_time) {
        this.group_id = group_id;
        this.user_id = user_id;
        this.content = content;
        this.create_time = create_time;
    }

    public Timestamp getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
