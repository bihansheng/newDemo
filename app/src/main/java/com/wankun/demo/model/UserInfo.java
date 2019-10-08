/**
 * Copyright (C), 2018-2019, wankun
 */
package com.wankun.demo.model;

import com.wankun.demo.model.httpResponse.BaseResponse;

import java.util.Date;
import java.util.UUID;

/**
 * 〈用户信息对象〉
 *
 * 使用枚举通常会比使用静态常量要消耗两倍以上的内存，在Android开发当中我们应当尽可能地不使用枚举
 * @author wankun
 * @create 2019/5/9
 * @since 1.0.0
 */
public class UserInfo extends BaseResponse {

    public static final String USER_SEX_UNKNOW = "UNKNOW";
    public static final String USER_SEX_MAN = "MAN";
    public static final String USER_SEX_WOMAN = "WOMAN";
    public static final String USER_TYP_NOMAL = "NOMAL";
    public static final String USER_TYP_MANGER = "MANGER";


    private String code;
    private String name;
    private String password;
    private int sex ;
    private Date birth;
    private String introduce;
    private String icon;

    private String email;
    private Date regTime;

    private String state;

    private  String type ;

    public UserInfo(String name, Date birth, String password) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        this.code = uuid;
        this.name = name;
        this.birth = birth;
        this.password = password;
    }

    public UserInfo( String name, String password,String email) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        this.code = uuid;
        this.name = name;
        this.password = password;
        this.email = email;
        Date date = new Date();
        this.regTime = date;
    }

    /**
     * 额外设置code
     */
    public void setNewCode(){
        this.code = UUID.randomUUID().toString().replace("-", "");
    }



    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", sex=" + sex +
                ", birth=" + birth +
                ", introduce='" + introduce + '\'' +
                ", icon='" + icon + '\'' +
                ", email='" + email + '\'' +
                ", regTime=" + regTime +
                ", state='" + state + '\'' +
                ", type=" + type +
                '}';
    }
}