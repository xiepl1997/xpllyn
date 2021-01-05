package com.xpllyn.pojo;

import java.sql.Timestamp;

/**
 * User pojo created at 2019/7/22 by Peiliang Xie
 */
public class User {
    private String user_email;
    private String user_pw;
    private Timestamp user_last_login;
    private String user_name;
    private String user_sex;
    private String user_phone;
    private String user_address;
    private Timestamp create_time;
    private Timestamp update_time;

    public User(String user_email, String user_pw) {
        this.user_email = user_email;
        this.user_pw = user_pw;
    }

    public User(String user_email, String user_pw, Timestamp user_last_login, String user_name, String user_sex,
                String user_phone, String user_address, Timestamp create_time, Timestamp update_time) {
        this.user_email = user_email;
        this.user_pw = user_pw;
        this.user_last_login = user_last_login;
        this.user_name = user_name;
        this.user_sex = user_sex;
        this.user_phone = user_phone;
        this.user_address = user_address;
        this.create_time = create_time;
        this.update_time = update_time;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_pw() {
        return user_pw;
    }

    public void setUser_pw(String user_pw) {
        this.user_pw = user_pw;
    }

    public Timestamp getUser_last_login() {
        return user_last_login;
    }

    public void setUser_last_login(Timestamp user_last_login) {
        this.user_last_login = user_last_login;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_sex() {
        return user_sex;
    }

    public void setUser_sex(String user_sex) {
        this.user_sex = user_sex;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public Timestamp getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }

    public Timestamp getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Timestamp update_time) {
        this.update_time = update_time;
    }
}
