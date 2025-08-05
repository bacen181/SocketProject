package com.sc.month2.StageProject.Entity.pojo.treasure;

import com.sc.month2.StageProject.constant.Rarity;

import java.io.Serializable;

public class Weapon extends Treasure implements Serializable {
    protected static final long serialVersionUID = 1L;
    private Integer attack;
    private Boolean isUse;

    public Weapon(Integer id, String name, Integer attack, Rarity rarity,Boolean isUse) {
        super(id,name,rarity);
        this.attack = attack;
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

    public Integer getAttack() {
        return attack;
    }

    public void setAttack(Integer attack) {
        this.attack = attack;
    }

    public Boolean getUse() {
        return isUse;
    }

    public void setUse(Boolean use) {
        isUse = use;
    }

    public String getBagUseStatus() {
        if (isUse) {
            return "(已装备)";
        }
        return " ";
    }
}
