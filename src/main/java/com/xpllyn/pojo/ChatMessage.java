package com.xpllyn.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

public class ChatMessage implements Serializable {
    private int id;
    private int from_user_id;
    private int to_user_id;
    private String content;
    private Timestamp create_time;

    public ChatMessage() {}

    public ChatMessage(int from_user_id, int to_user_id, String content, Timestamp create_time) {
        this.from_user_id = from_user_id;
        this.to_user_id = to_user_id;
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

    public int getFrom_user_id() {
        return from_user_id;
    }

    public void setFrom_user_id(int from_user_id) {
        this.from_user_id = from_user_id;
    }

    public int getTo_user_id() {
        return to_user_id;
    }

    public void setTo_user_id(int to_user_id) {
        this.to_user_id = to_user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
