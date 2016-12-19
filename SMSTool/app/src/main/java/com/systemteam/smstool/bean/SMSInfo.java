package com.systemteam.smstool.bean;

/**
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2016/12/19 19:10
 */

public class SMSInfo {
    //{ "_id", "address", "person", "body", "date", "type" }
    private Long id;
    private String address;
    private String person;
    private String body;
    private long date;
    private int type;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
