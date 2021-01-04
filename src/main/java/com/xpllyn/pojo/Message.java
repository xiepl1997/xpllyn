package com.xpllyn.pojo;

import java.sql.Timestamp;

/**
 * 留言pojo
 * created by xiepl1997 at 2019-8-21
 */
public class Message {
    private String ip;
    private String id;
    private String pre_id;
    private String name;
    private String province;
    private String city;
    private Timestamp time;
    private String content;

    public Message(String ip, String id, String pre_id, String name, String province, String city, Timestamp time, String content) {
        this.ip = ip;
        this.id = id;
        this.pre_id = pre_id;
        this.name = name;
        this.province = province;
        this.city = city;
        this.time = time;
        this.content = content;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setId(String id) {
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

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIp() {
        return ip;
    }

    public String getId() {
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

    public Timestamp getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }
}
