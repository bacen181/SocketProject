package com.sc.month2.StageProject.Entity.pojo.treasure;

import com.sc.month2.StageProject.constant.Rarity;

import java.io.Serializable;

public class Armor extends Treasure implements Serializable {
    protected static final long serialVersionUID = 1L;
    private Integer healthBonus;
    private Integer armor;  //防具护甲
    private Boolean isUse;

    public Armor(Integer id, String name, Integer healthBonus, Integer armor, Rarity rarity,Boolean isUse) {
        super(id, name,rarity);
        this.healthBonus = healthBonus;
        this.armor = armor;
        this.isUse = isUse;
    }

    @Override
    public String toString() {
        return String.format("✨ %s "+ getBagUseStatus() +" ✨ \nID: %03d\n稀有度: %s\n%s",
                getName(),
                getId(),
                getRarityDisplayName(),
                getRarityVisual());
    }

    public String getBagUseStatus() {
        if (isUse) {
            return "(已装备)";
        }
        return " ";
    }

    public Boolean getUse() {
        return isUse;
    }

    public void setUse(Boolean use) {
        isUse = use;
    }

    public Integer getArmor() {
        return armor;
    }

    public void setArmor(Integer armor) {
        this.armor = armor;
    }

    public Integer getHealthBonus() {
        return healthBonus;
    }

    public void setHealthBonus(Integer healthBonus) {
        this.healthBonus = healthBonus;
    }
}
