package com.systemteam.smstool.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.Date;

/**
 * 类描述：记录
 * 创建人：Administrator
 * 创建时间：2016/12/5 20:01
 */
@Entity
public class SMSRecord {
    @Id
    public Long id;
    public Long customerId;
    public String customerName;
    @NotNull
    public String phoneNum;
    public String content;
    @NotNull
    public Date time;
    @Generated(hash = 1318248864)
    public SMSRecord(Long id, Long customerId, String customerName,
            @NotNull String phoneNum, String content, @NotNull Date time) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.phoneNum = phoneNum;
        this.content = content;
        this.time = time;
    }
    @Generated(hash = 1195491800)
    public SMSRecord() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getCustomerId() {
        return this.customerId;
    }
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    public String getCustomerName() {
        return this.customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public String getPhoneNum() {
        return this.phoneNum;
    }
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Date getTime() {
        return this.time;
    }
    public void setTime(Date time) {
        this.time = time;
    }
}
