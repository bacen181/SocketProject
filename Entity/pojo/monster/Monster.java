package com.sc.month2.StageProject.Entity.pojo.monster;

import com.sc.month2.StageProject.Entity.pojo.treasure.Treasure;

import java.io.Serializable;
import java.util.List;

public abstract class Monster implements Serializable {
    protected static final long serialVersionUID = 1L;
    protected String monsterName; //怪物名
    protected Integer HP; //怪物血量
    protected Integer currentHP; //怪物当前血量
    protected Integer attack; //怪物攻击力
    protected Integer treasuresDropId;    //怪物掉宝id
    protected Integer floorLevel;  //生成层数
    protected Boolean isBoss;

    public abstract List<Treasure> dropItems();

    public abstract Treasure dropItem();

    public abstract Treasure dropItem(int flood);

    public abstract int calculateExpReward();

    public abstract Monster monsterStatus();

    public Integer getCurrentHP() {
        return currentHP;
    }

    public void setCurrentHP(Integer currentHP) {
        this.currentHP = currentHP;
    }

    public Boolean getBoss() {
        return isBoss;
    }

    public void setBoss(Boolean boss) {
        isBoss = boss;
    }

    public String getMonsterName() {
        return monsterName;
    }

    public void setMonsterName(String monsterName) {
        this.monsterName = monsterName;
    }

    public Integer getHP() {
        return HP;
    }

    public void setHP(Integer HP) {
        this.HP = HP;
    }

    public Integer getAttack() {
        return attack;
    }

    public void setAttack(Integer attack) {
        this.attack = attack;
    }

    public Integer getTreasuresDropId() {
        return treasuresDropId;
    }

    public void setTreasuresDropId(Integer treasuresDropId) {
        this.treasuresDropId = treasuresDropId;
    }

    public Integer getFloorLevel() {
        return floorLevel;
    }

    public void setFloorLevel(Integer floorLevel) {
        this.floorLevel = floorLevel;
    }

    public Boolean getBoSS() {
        return isBoss;
    }

    public void setBoSS(Boolean boSS) {
        isBoss = boSS;
    }

    public Integer attackPlayer(Monster monster){
        return monster.getAttack();
    }

    @Override
    public String toString() {
        return "Monster{" +
                "monsterName='" + monsterName + '\'' +
                ", HP=" + HP +
                ", attack=" + attack +
                ", treasuresDropId=" + treasuresDropId +
                ", floorLevel=" + floorLevel +
                ", isBoss=" + isBoss +
                '}';
    }

}
