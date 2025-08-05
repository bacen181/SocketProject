package com.sc.month2.StageProject.Entity.pojoImplement.treasure.weapon;

import com.sc.month2.StageProject.Entity.pojo.treasure.Weapon;
import com.sc.month2.StageProject.constant.Rarity;

public class Fist extends Weapon {
    protected static final long serialVersionUID = 1L;
    public Fist() {
        super(0,"拳头",0, Rarity.BEGINNER,false);
    } public Fist( Boolean isUse) {
        super(0,"拳头",0, Rarity.BEGINNER,isUse);
    }
}
