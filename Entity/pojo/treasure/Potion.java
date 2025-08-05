package com.sc.month2.StageProject.Entity.pojo.treasure;

import com.sc.month2.StageProject.constant.Rarity;

import java.io.Serializable;

public class Potion extends Treasure implements Serializable {
    private Integer replyHP;

    public Potion(Integer id, String name, Rarity rarity) {
        super(id, name, rarity);

    }

    public Integer getReplyHP() {
        return replyHP;
    }

    public void setReplyHP(Integer replyHP) {
        this.replyHP = replyHP;
    }
}
