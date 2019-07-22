package com.xpllyn.pojo;

/**
 * User pojo created at 2019/7/22 by Peiliang
 */
public class User {
    private String id;
    private String password;
    private String last_login;
    private String username;
    private String sex;
    private String phonenumber;
    private String email;
    private String address;
    private String ip;
    private String position;

    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setLast_login(String last_login) {
        this.last_login = last_login;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getLast_login() {
        return last_login;
    }

    public String getUsername() {
        return username;
    }

    public String getSex() {
        return sex;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getIp() {
        return ip;
    }

    public String getPosition() {
        return position;
    }
}
