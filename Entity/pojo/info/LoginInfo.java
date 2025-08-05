package com.sc.month2.StageProject.Entity.pojo.info;

import java.io.Serializable;

public class LoginInfo implements Serializable {
    protected static final long serialVersionUID = 1L;
    protected String nickName;
    protected String password;

    public LoginInfo(String nickName, String password) {
        this.nickName = nickName;
        this.password = password;
    }

    public LoginInfo() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
