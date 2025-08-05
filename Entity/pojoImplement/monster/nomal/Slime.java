package com.sc.month2.StageProject.Entity.pojoImplement.monster.nomal;

import com.sc.month2.StageProject.CreateFactory.TreasureFactory;
import com.sc.month2.StageProject.Entity.pojo.monster.Monster;
import com.sc.month2.StageProject.Entity.pojo.treasure.Treasure;
import com.sc.month2.StageProject.Entity.pojoImplement.treasure.potion.SmallLifePotion;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Slime extends Monster {
    protected static final long serialVersionUID = 1L;
    static Random random = new Random();

    public Slime(Integer floor){
        this.monsterName = "史莱姆";
        this.HP = 35;
        this.currentHP = this.HP;
        this.attack = 7;
        this.floorLevel = floor;
        this.isBoss = false;
    }


    @Override
    public List<Treasure> dropItems() {

        List<Treasure> droppedTreasures = new ArrayList<>();

        // 60%概率掉落一个小生命药水
        if(random.nextInt(101) > 60){
            droppedTreasures.add(new SmallLifePotion());
        }
        if(random.nextInt(101) > 30){
            droppedTreasures.add(TreasureFactory.createRandomTreasure(floorLevel));
        }


        return droppedTreasures;
    }

    //Todo
    @Override
    public Treasure dropItem() {
        if(!(random.nextInt(101) > 30)){
           return null;
        }
        return new SmallLifePotion();
    }

    @Override
    public Treasure dropItem(int floor) {
        Treasure dropItem = null;
        // 小怪概率掉落逻辑
        if(random.nextInt(101) > 30){
            dropItem = TreasureFactory.createRandomTreasure(floorLevel);
        }

        return dropItem;
    }


    @Override
    public int calculateExpReward() {
        return 5;
    }

    @Override
    public Monster monsterStatus() {
        return null;
    }

    @Override
    public String toString() {
        return "怪物名：" + getMonsterName() + " ," +
                "属性：当前血量：" + getCurrentHP() + ",攻击力：" + getAttack();
    }

}
