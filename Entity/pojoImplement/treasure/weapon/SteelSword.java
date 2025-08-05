package com.sc.month2.StageProject.Entity.pojoImplement.treasure.weapon;

import com.sc.month2.StageProject.Entity.pojo.treasure.Weapon;
import com.sc.month2.StageProject.constant.Rarity;

public class SteelSword extends Weapon {
    protected static final long serialVersionUID = 1L;
    public SteelSword() {
        super(2, "钢剑", 15, Rarity.NORMAL,false);
    }
}