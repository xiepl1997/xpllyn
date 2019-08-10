package com.xpllyn.pojo;


/**
 * visitor pojo created at 2019/8/9 by Peiliang Xie
 */
public class Visitor {
    private String visitor_ip;
    private String visitor_name;
    private String visitor_address;
    private String visitor_visited_time;

    public String getVisitor_ip() {
        return visitor_ip;
    }

    public String getVisitor_name() {
        return visitor_name;
    }

    public String getVisitor_address() {
        return visitor_address;
    }

    public String getVisitor_visited_time() {
        return visitor_visited_time;
    }

    public void setVisitor_ip(String visitor_ip) {
        this.visitor_ip = visitor_ip;
    }

    public void setVisitor_name(String visitor_name) {
        this.visitor_name = visitor_name;
    }

    public void setVisitor_address(String visitor_address) {
        this.visitor_address = visitor_address;
    }

    public void setVisitor_visited_time(String visitor_visited_time) {
        this.visitor_visited_time = visitor_visited_time;
    }
}
