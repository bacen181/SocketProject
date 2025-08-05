package com.sc.month2.StageProject.Entity.pojo.info;

import com.sc.month2.StageProject.Entity.pojo.player.PlayerInfo;

import java.io.Serializable;

public class EquipmentInfo implements Serializable {
    protected static final long serialVersionUID = 1L;
    private PlayerInfo playerInfo;

    /**
     * 药水编号
     */
    private Integer num;

    public EquipmentInfo(PlayerInfo playerInfo,Integer num) {
        this.playerInfo = playerInfo;

        this.num = num;
    }

    public PlayerInfo getPlayerInfo() {
        return playerInfo;
    }

    public void setPlayerInfo(PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
    }


    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
