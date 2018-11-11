package com.ackerley.library.modules.sys.entity;

import com.ackerley.library.common.entity.BaseEntity;

public class User extends BaseEntity {
    private String gender;      //性别
    private String age;      //年龄
    private String realName;    //真实姓名
    private String loginName;   //系统登陆名
    private String pwd;         //系统登陆密码，摘要后的密文
    private String telNumber;
    private String ctctAddr;    //联系地址
    private String email;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCtctAddr() {
        return ctctAddr;
    }

    public void setCtctAddr(String ctctAddr) {
        this.ctctAddr = ctctAddr;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public User(){}
}
