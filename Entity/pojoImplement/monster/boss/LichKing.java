package com.sc.month2.StageProject.Entity.pojoImplement.monster.boss;

import com.sc.month2.StageProject.CreateFactory.TreasureFactory;
import com.sc.month2.StageProject.Entity.pojo.monster.Monster;
import com.sc.month2.StageProject.Entity.pojo.treasure.Treasure;
import com.sc.month2.StageProject.Entity.pojoImplement.treasure.potion.SmallLifePotion;

import java.util.ArrayList;
import java.util.List;

public class LichKing extends Monster {
    protected static final long serialVersionUID = 1L;
    public LichKing() {
        this.monsterName = "巫妖王";
        this.HP = 700;
        this.currentHP = this.HP;
        this.attack = 60;
        this.floorLevel = 30;
        this.isBoss = true;
    }

    @Override
    public List<Treasure> dropItems() {
        List<Treasure> droppedTreasures = new ArrayList<>();

        // 固定掉落：每个怪物都会掉落一个小生命药水
        droppedTreasures.add(new SmallLifePotion());

        droppedTreasures.add(TreasureFactory.createLegendaryEquipment());
        return droppedTreasures;
    }

    @Override
    public Treasure dropItem() {
        // 最终Boss掉落传说装备
        return TreasureFactory.createLegendaryEquipment();
    }

    @Override
    public Treasure dropItem(int floor) {
        return TreasureFactory.createLegendaryEquipment();
    }

    @Override
    public int calculateExpReward() {
        return 500;
    }

    @Override
    public Monster monsterStatus() {
        return null;
    }


}