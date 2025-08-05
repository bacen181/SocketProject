package com.sc.month2.StageProject.Entity.pojo.player;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class PlayerInfo implements Serializable{
    protected static final long serialVersionUID = 1L;
    protected String nickName; //客户端名称也叫用户名
    protected String ip;
    protected String password;
    protected transient ObjectOutputStream oos;
    protected transient ObjectInputStream ois;
    protected PlayerStatus playerStatus;

    public PlayerStatus getPlayerStatus() {
        return playerStatus;
    }

    public void setPlayerStatus(PlayerStatus playerStatus) {
        this.playerStatus = playerStatus;
    }

    public PlayerInfo(String nickName, String pwd, ObjectOutputStream oos, String ip, ObjectInputStream ois, PlayerStatus playerStatus) {
        this.oos = oos;
        this.password = pwd;
        this.nickName = nickName;
        this.ip = ip;
        this.ois = ois;
        this.playerStatus = playerStatus;
    }

    public PlayerInfo(ObjectOutputStream oos, String ip, ObjectInputStream ois){
        this.oos = oos;
        this.ip = ip;
        this.ois = ois;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public void setOos(ObjectOutputStream oos) {
        this.oos = oos;
    }

    public ObjectInputStream getOis() {
        return ois;
    }

    public void setOis(ObjectInputStream ois) {
        this.ois = ois;
    }

    @Override
    public String toString() {
        return "玩家{" +
                "昵称='" + nickName + '\'' +
                ", ip='" + ip + '\'' +
                '}';
    }
}
