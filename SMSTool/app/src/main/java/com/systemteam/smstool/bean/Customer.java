package com.systemteam.smstool.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import java.util.Date;

/**
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2016/12/5 19:52
 */
@Entity
public class Customer implements Serializable {
    @Transient
    private static final long serialVersionUID = -8940196742313994740L;
    @Id
    public Long id;
    public String name;
    public String nickName;
    @NotNull
    public String phoneNum;
    public int age;
    public boolean sex;//性别
    public String homeAddress;
    public String companyAddress;
    public String remarks;// 备注
    public Date time;//创建时间
    public Date registerTime;//注册时间
    @Transient
    public String send;
    @Transient
    public String recive;

    @Generated(hash = 441724340)
    public Customer(Long id, String name, String nickName, @NotNull String phoneNum,
            int age, boolean sex, String homeAddress, String companyAddress,
            String remarks, Date time, Date registerTime) {
        this.id = id;
        this.name = name;
        this.nickName = nickName;
        this.phoneNum = phoneNum;
        this.age = age;
        this.sex = sex;
        this.homeAddress = homeAddress;
        this.companyAddress = companyAddress;
        this.remarks = remarks;
        this.time = time;
        this.registerTime = registerTime;
    }
    @Generated(hash = 60841032)
    public Customer() {
    }

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getNickName() {
        return this.nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getPhoneNum() {
        return this.phoneNum;
    }
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
    public int getAge() {
        return this.age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public boolean getSex() {
        return this.sex;
    }
    public void setSex(boolean sex) {
        this.sex = sex;
    }
    public String getHomeAddress() {
        return this.homeAddress;
    }
    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }
    public String getCompanyAddress() {
        return this.companyAddress;
    }
    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }
    public String getRemarks() {
        return this.remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRecive() {
        return recive;
    }

    public void setRecive(String recive) {
        this.recive = recive;
    }

    public String getSend() {
        return send;
    }

    public void setSend(String send) {
        this.send = send;
    }

    public boolean isSex() {
        return sex;
    }
    public Date getTime() {
        return this.time;
    }
    public void setTime(Date time) {
        this.time = time;
    }
    public Date getRegisterTime() {
        return this.registerTime;
    }
    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }
}
