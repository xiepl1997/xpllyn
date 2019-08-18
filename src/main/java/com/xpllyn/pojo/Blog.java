package com.xpllyn.pojo;

/**
 * Blog pojo
 * created by xiepl1997 at 2019-8-18
 */
public class Blog {
    private String title; //博客标题
    private String time; //博客发布时间
    private String url; //博客url

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getUrl() {
        return url;
    }
}
