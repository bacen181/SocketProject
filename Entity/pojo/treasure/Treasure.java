package com.sc.month2.StageProject.Entity.pojo.treasure;

import com.sc.month2.StageProject.constant.Rarity;

import java.io.Serializable;

public class Treasure implements Serializable {
    protected static final long serialVersionUID = 1L;
    private Integer id;
    private String name;
    private Rarity rarity;

    @Override
    public String toString() {
        return String.format("✨ %s ✨\nID: %03d\n稀有度: %s\n%s",
                name,
                id,
                getRarityDisplayName(),
                getRarityVisual());
    }

    public String getRarityDisplayName() {
        switch (rarity) {
            case BEGINNER: return "新手";
            case NORMAL:   return "普通";
            case ELITE:    return "精良";
            case EPIC:     return "史诗";
            case LEGENDARY:return "传说";
            default:       return "未知";
        }
    }

    public String getRarityVisual() {
        switch (rarity) {
            case BEGINNER: return "☆";
            case NORMAL:   return "★";
            case ELITE:    return "★★";
            case EPIC:     return "★★★";
            case LEGENDARY:return "★★★★★";
            default:       return "";
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Treasure(Integer id, String name,Rarity rarity) {
        this.id = id;
        this.name = name;
        this.rarity = rarity;


    }


}

