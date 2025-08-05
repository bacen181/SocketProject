package com.sc.month2.StageProject.Entity.pojoImplement.monster.boss;

import com.sc.month2.StageProject.CreateFactory.TreasureFactory;
import com.sc.month2.StageProject.Entity.pojo.monster.Monster;
import com.sc.month2.StageProject.Entity.pojo.treasure.Treasure;
import com.sc.month2.StageProject.Entity.pojoImplement.treasure.potion.SmallLifePotion;

import java.util.ArrayList;
import java.util.List;

public class DeathKnight extends Monster {
    protected static final long serialVersionUID = 1L;
    public DeathKnight() {
        this.monsterName = "死亡骑士";
        this.HP = 300;
        this.currentHP = this.HP;
        this.attack = 35;
        this.floorLevel = 20;
        this.isBoss = true;
    }

    @Override
    public List<Treasure> dropItems() {
        List<Treasure> droppedTreasures = new ArrayList<>();

        // 固定掉落：每个怪物都会掉落一个小生命药水

        droppedTreasures.add(new SmallLifePotion());
        droppedTreasures.add(new SmallLifePotion());
        droppedTreasures.add(new SmallLifePotion());

        // Boss掉落逻辑
        droppedTreasures.add(TreasureFactory.createRandomTreasure(floorLevel));
        return droppedTreasures;
    }

    @Override
    public Treasure dropItem() {
        return TreasureFactory.createRandomTreasure(floorLevel);
    }

    @Override
    public Treasure dropItem(int floor) {
        return TreasureFactory.createRandomTreasure(floorLevel);
    }


    @Override
    public int calculateExpReward() {
        return 100;
    }

    @Override
    public Monster monsterStatus() {
        return null;
    }
}