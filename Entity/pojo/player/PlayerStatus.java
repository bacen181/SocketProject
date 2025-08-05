package com.sc.month2.StageProject.Entity.pojo.player;

import com.sc.month2.StageProject.Entity.pojo.treasure.Armor;
import com.sc.month2.StageProject.Entity.pojo.treasure.Weapon;
import com.sc.month2.StageProject.Entity.pojoImplement.treasure.armor.Vest;
import com.sc.month2.StageProject.Entity.pojoImplement.treasure.weapon.Fist;

import java.io.Serializable;

public class PlayerStatus implements Serializable {
    protected static final long serialVersionUID = 1L;
    protected volatile Integer maxHP;            //最大血量
    protected volatile Integer baseHP;           //基础血量
    protected volatile Integer currentHP;        //当前血量
    protected volatile Integer attack;           //实际攻击
    protected volatile Integer baseAttack;       //基础攻击
    protected volatile Armor armor;              //当前防具护甲
    protected volatile Weapon weapon;            //当前武器
    protected volatile Integer level;            //最高探索层数
    protected volatile Integer currentLevel;
    protected volatile Integer xpToNextLevel;    //到下一级所需经验最大值
    protected volatile Integer currentXP;        //当前经验
    protected volatile Bag bag;                  //背包
    protected volatile Integer Rank;

    public Bag getBag() {
        return bag;
    }

    public void setBag(Bag bag) {
        this.bag = bag;
    }

    public Integer getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(Integer currentLevel) {
        this.currentLevel = currentLevel;
    }

    public PlayerStatus() {
        this.Rank = 1;
        this.armor = new Vest(true);
        this.weapon = new Fist(true);
        this.level = 1;
        this.currentLevel = level;
        this.xpToNextLevel = 20;
        this.currentXP = 0;
        this.bag = new Bag();
        this.baseHP = 100;
        this.baseAttack = 5;
        this.attack = baseAttack + weapon.getAttack();
        this.maxHP = baseHP + armor.getHealthBonus();
        this.currentHP = maxHP;


    }

    @Override
    public String toString() {
        return  "等级：" + Rank + "\n" +
                "生命值: " + currentHP + "/" + maxHP +
                "\n当前攻击力: " + attack +",基础攻击力: " + baseAttack +
                (weapon != null ? "\n武器: " + weapon.getName() + "(+" + weapon.getAttack() + "攻击)" : "\n武器: 无") +
                (armor != null ? "\n防具: " + armor.getName() + "(+" + armor.getArmor() + "防御)" : "\n防具: 无") +
                "\n总攻击力: " + (baseAttack + (weapon != null ? weapon.getAttack() : 0)) +
                "\n总防御力: " + (armor != null ? armor.getArmor() : 0) + "\n============================";
    }

    public void levelUp(PlayerStatus playerStatus, int earnedXP) {
        // 参数校验
        if (playerStatus == null) {
            throw new IllegalArgumentException("PlayerStatus 不能为空");
        }
        if (earnedXP <= 0) {
            throw new IllegalArgumentException("获得经验值必须大于0");
        }
        if (playerStatus.currentXP < 0 || playerStatus.xpToNextLevel <= 0) {
            throw new IllegalStateException("玩家当前经验值异常");
        }

        // 累计经验值
        int totalXP = playerStatus.currentXP + earnedXP;

        // 循环处理可能的连续升级
        while (totalXP >= playerStatus.xpToNextLevel) {
            // 计算升级后剩余经验
            int remainingXP = totalXP - playerStatus.xpToNextLevel;

            // 升级增强角色属性

            playerStatus.baseHP += 10;
            playerStatus.maxHP = baseHP + playerStatus.getArmor().getHealthBonus();
            playerStatus.currentHP = playerStatus.maxHP; // 满血恢复
            playerStatus.baseAttack += 2;
            playerStatus.attack = baseAttack + playerStatus.getWeapon().getAttack();
            playerStatus.Rank += 1;

            // 提高下一级所需经验
            playerStatus.xpToNextLevel += 30;

            // 准备下一轮判断（如果有剩余经验可能继续升级）
            totalXP = remainingXP;
        }
        System.out.println("勇者升级了：" +  playerStatus);

        // 更新当前经验值
        playerStatus.currentXP = totalXP;
    }




    public Integer getBaseHP() {
        return baseHP;
    }

    public void setBaseHP(Integer baseHP) {
        this.baseHP = baseHP;
    }


    public Integer getBaseAttack() {
        return baseAttack;
    }

    public void setBaseAttack(Integer baseAttack) {
        this.baseAttack = baseAttack;
    }

    public Integer getAttack() {
        return attack;
    }

    public void setAttack(Integer attack, Weapon weapon) {
        this.attack = attack + weapon.getAttack();
    }

    public Armor getArmor() {
        return armor;
    }

    public void setArmor(Armor armor) {
        this.armor = armor;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public Integer getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(Integer maxHP) {
        this.maxHP = maxHP;
    }

    public void setAttack(Integer attack) {
        this.attack = attack;
    }

    public Integer getRank() {
        return Rank;
    }

    public void setRank(Integer rank) {
        Rank = rank;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getXpToNextLevel() {
        return xpToNextLevel;
    }

    public void setXpToNextLevel(Integer xpToNextLevel) {
        this.xpToNextLevel = xpToNextLevel;
    }



    public Integer getCurrentHP() {
        return currentHP;
    }

    public void setCurrentHP(Integer currentHP) {
        this.currentHP = currentHP;
    }

    public Integer getCurrentXP() {
        return currentXP;
    }

    public void setCurrentXP(Integer currentXP) {
        this.currentXP = currentXP;
    }
}
