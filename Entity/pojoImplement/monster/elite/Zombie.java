package com.sc.month2.StageProject.Entity.pojoImplement.monster.elite;

import com.sc.month2.StageProject.CreateFactory.TreasureFactory;
import com.sc.month2.StageProject.Entity.pojo.monster.Monster;
import com.sc.month2.StageProject.Entity.pojo.treasure.Treasure;
import com.sc.month2.StageProject.Entity.pojoImplement.treasure.potion.SmallLifePotion;

import java.util.ArrayList;
import java.util.List;

public class Zombie extends Monster {
    protected static final long serialVersionUID = 1L;
    public Zombie() {
        this.monsterName = "大鬼僵尸";
        this.HP = 150;
        this.currentHP = this.HP;
        this.attack = 18;
        this.floorLevel = 10;
        this.isBoss = true;
    }


    @Override
    public List<Treasure> dropItems() {
        List<Treasure> droppedTreasures = new ArrayList<>();

        // 固定掉落：每个怪物都会掉落一个小生命药水
        droppedTreasures.add(new SmallLifePotion());

        droppedTreasures.add(TreasureFactory.createRandomTreasure(floorLevel));

        return droppedTreasures;

    }

    //Todo
    @Override
    public Treasure dropItem() {

        return new SmallLifePotion();
    }

    @Override
    public Treasure dropItem(int floor) {
        // 小怪概率掉落逻辑
        Treasure dropItem = TreasureFactory.createRandomTreasure(floorLevel);
        return dropItem;
    }



    @Override
    public int calculateExpReward() {
        return 50;
    }

    @Override
    public Monster monsterStatus() {
        return null;
    }

    @Override
    public String toString() {
        return "怪物名：" + getMonsterName() + "\n" +
                "属性：当前血量：" + getHP() + "攻击" + getAttack() + "\n";
    }

}
