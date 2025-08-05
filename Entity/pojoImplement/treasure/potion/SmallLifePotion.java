package com.sc.month2.StageProject.Entity.pojoImplement.treasure.potion;

import com.sc.month2.StageProject.Entity.pojo.treasure.Potion;
import com.sc.month2.StageProject.constant.Rarity;

public class SmallLifePotion extends Potion {
    protected static final long serialVersionUID = 1L;
    private Integer replyHP;
    public SmallLifePotion() {
        super(1, "小型生命药水", Rarity.BEGINNER);
        this.replyHP = 40;
    }

    public Integer getReplyHP() {
        return replyHP;
    }

    public void setReplyHP(Integer replyHP) {
        this.replyHP = replyHP;
    }
}
