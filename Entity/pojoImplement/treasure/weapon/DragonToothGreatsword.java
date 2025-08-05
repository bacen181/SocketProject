package com.sc.month2.StageProject.Entity.pojoImplement.treasure.weapon;

import com.sc.month2.StageProject.Entity.pojo.treasure.Weapon;
import com.sc.month2.StageProject.constant.Rarity;

public class DragonToothGreatsword extends Weapon {
    protected static final long serialVersionUID = 1L;
    public DragonToothGreatsword() {
        super(4, "龙牙巨剑", 40, Rarity.EPIC,false);
    }
}