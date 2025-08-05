package com.sc.month2.StageProject.Entity.pojoImplement.treasure.weapon;

import com.sc.month2.StageProject.Entity.pojo.treasure.Weapon;
import com.sc.month2.StageProject.constant.Rarity;

public class MithrilSword extends Weapon {
    protected static final long serialVersionUID = 1L;
    public MithrilSword() {
        super(3, "秘银剑", 25, Rarity.ELITE,false);
    }
}