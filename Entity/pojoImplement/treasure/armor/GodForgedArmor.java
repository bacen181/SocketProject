package com.sc.month2.StageProject.Entity.pojoImplement.treasure.armor;

import com.sc.month2.StageProject.Entity.pojo.treasure.Armor;
import com.sc.month2.StageProject.constant.Rarity;

public class GodForgedArmor extends Armor {
    protected static final long serialVersionUID = 1L;
    public GodForgedArmor() {
        super(5, "神铸铠甲", 80, 25, Rarity.LEGENDARY,false);
    }
}