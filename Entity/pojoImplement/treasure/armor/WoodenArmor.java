package com.sc.month2.StageProject.Entity.pojoImplement.treasure.armor;

import com.sc.month2.StageProject.Entity.pojo.treasure.Armor;
import com.sc.month2.StageProject.constant.Rarity;

public class WoodenArmor extends Armor {
    protected static final long serialVersionUID = 1L;
    public WoodenArmor(Boolean isUse) {
        super(1,"木甲",20,5, Rarity.BEGINNER,isUse);
    }
    public WoodenArmor() {
        super(1,"木甲",20,5, Rarity.BEGINNER,false);
    }
}
