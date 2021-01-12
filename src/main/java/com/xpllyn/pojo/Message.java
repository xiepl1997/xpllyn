package com.xpllyn.pojo;

import java.sql.Timestamp;

/**
 * 留言pojo
 * created by xiepl1997 at 2019-8-21
 */
public class Message {
    private int id;
    private String ip;
    private String pre_id;
    private String name;
    private String province;
    private String city;
    private Timestamp create_time;
    private Timestamp update_time;
    private String content;

    public Message() {}

    public Message(String ip, String pre_id, String name, String province, String city, String content) {
        this.ip = ip;
        this.pre_id = pre_id;
        this.name = name;
        this.province = province;
        this.city = city;
        this.content = content;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPre_id(String pre_id) {
        this.pre_id = pre_id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIp() {
        return ip;
    }

    public int getId() {
        return id;
    }

    public String getPre_id() {
        return pre_id;
    }

    public String getName() {return name;}

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public Timestamp getCreate_time() {
        return create_time;
    }

    public String getContent() {
        return content;
    }

    public Timestamp getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Timestamp update_time) {
        this.update_time = update_time;
    }
}
