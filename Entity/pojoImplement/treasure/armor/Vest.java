package com.sc.month2.StageProject.Entity.pojoImplement.treasure.armor;

import com.sc.month2.StageProject.Entity.pojo.treasure.Armor;
import com.sc.month2.StageProject.constant.Rarity;

public class Vest extends Armor {
    protected static final long serialVersionUID = 1L;

    public Vest() {
        super(0, "背心", 0, 0, Rarity.BEGINNER,false);
    }
    public Vest(Boolean isUse) {
        super(0, "背心", 0, 0, Rarity.BEGINNER,isUse);
    }
}
