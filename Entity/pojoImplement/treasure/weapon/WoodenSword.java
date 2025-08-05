package com.sc.month2.StageProject.Entity.pojoImplement.treasure.weapon;

import com.sc.month2.StageProject.Entity.pojo.treasure.Weapon;
import com.sc.month2.StageProject.constant.Rarity;

public class WoodenSword extends Weapon {
    protected static final long serialVersionUID = 1L;
    public WoodenSword() {
        super(1,"木剑",5, Rarity.BEGINNER,false);
    }
    public WoodenSword(Boolean isUse) {
        super(1,"木剑",5, Rarity.BEGINNER,isUse);
    }
}
