package com.sc.month2.StageProject.Entity.pojoImplement.treasure.armor;

import com.sc.month2.StageProject.Entity.pojo.treasure.Armor;
import com.sc.month2.StageProject.constant.Rarity;

public class KnightPlateArmor extends Armor {
    protected static final long serialVersionUID = 1L;
    public KnightPlateArmor() {
        super(3, "骑士板甲", 40, 12, Rarity.ELITE,false);
    }
}
