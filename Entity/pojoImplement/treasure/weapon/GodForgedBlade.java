package com.sc.month2.StageProject.Entity.pojoImplement.treasure.weapon;

import com.sc.month2.StageProject.Entity.pojo.treasure.Weapon;
import com.sc.month2.StageProject.constant.Rarity;

public class GodForgedBlade extends Weapon {
    protected static final long serialVersionUID = 1L;
    public GodForgedBlade() {
        super(5, "神铸之刃", 60, Rarity.LEGENDARY,false);
    }
}