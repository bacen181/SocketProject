package com.sc.month2.StageProject.Entity.pojoImplement.treasure.armor;

import com.sc.month2.StageProject.Entity.pojo.treasure.Armor;
import com.sc.month2.StageProject.constant.Rarity;

public class ChainMail extends Armor {
    protected static final long serialVersionUID = 1L;

    public ChainMail() {
        super(2, "锁子甲", 25, 8, Rarity.NORMAL,false);

    }
}
