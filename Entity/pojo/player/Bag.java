package com.sc.month2.StageProject.Entity.pojo.player;

import com.sc.month2.StageProject.Entity.pojo.treasure.Armor;
import com.sc.month2.StageProject.Entity.pojo.treasure.Potion;
import com.sc.month2.StageProject.Entity.pojo.treasure.Weapon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Bag implements Serializable {
    protected static final long serialVersionUID = 1L;
    private List<Weapon> weaponBag = new ArrayList<>(10);
    private List<Armor> armorBag = new ArrayList<>(10);
    private List<Potion> potionBag = new ArrayList<>(10);
    //默认为空包
    public Bag() {
    }

    public List<Weapon> getWeaponBag() {
        return weaponBag;
    }

    public void setWeaponBag(List<Weapon> weaponBag) {
        this.weaponBag = weaponBag;
    }

    public List<Armor> getArmorBag() {
        return armorBag;
    }

    public void setArmorBag(List<Armor> armorBag) {
        this.armorBag = armorBag;
    }

    public List<Potion> getPotionBag() {
        return potionBag;
    }

    public void setPotionBag(List<Potion> potionBag) {
        this.potionBag = potionBag;
    }



}
