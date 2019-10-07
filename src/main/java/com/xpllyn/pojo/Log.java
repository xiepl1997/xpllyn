package com.xpllyn.pojo;

/**
 * author: xiepl1997
 * date: 2019/10/06
 * function: the entity of logs
 */
public class Log {
    private String ip; //用户ip地址
    private String date; //日志产生时间
    private String type; //日志类型：访问主页visit，下载资源book_download，评论comment
    private String bookname; //书本名字，如果日志类型为电子书下载的话，则该字段不为空
    private String blogname; //主页博客名字，如果点击了主页的博客，该字段不为空
    private String status; //日志状态：操作成功success，操作失败fail

    public Log(String ip, String date, String type, String status) {
        this.ip = ip;
        this.type = type;
        this.date = date;
        this.bookname = null;
        this.blogname = null;
        this.status = status;
    }

    public Log(String ip, String date, String type, String name, String status){
        this.ip = ip;
        this.date = date;
        this.type = type;
        this.status = status;
        if(type.equals("book_download")){
            this.bookname = name;
            this.blogname = null;
        }
        else if(type.equals("readBlog")){
            this.bookname = null;
            this.blogname = name;
        }
        else if(type.equals("readMoreBlog")){
            this.bookname = null;
            this.blogname = name;
        }
        else{
            this.bookname = null;
            this.blogname = null;
        }
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getIp() {
        return ip;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
