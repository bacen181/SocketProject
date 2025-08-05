package com.sc.month2.StageProject.Entity.pojoImplement.treasure.armor;

import com.sc.month2.StageProject.Entity.pojo.treasure.Armor;
import com.sc.month2.StageProject.constant.Rarity;

public class DragonScaleArmor extends Armor {
    protected static final long serialVersionUID = 1L;
    public DragonScaleArmor() {
        super(4, "龙鳞甲", 60, 18, Rarity.EPIC,false);
    }
}
