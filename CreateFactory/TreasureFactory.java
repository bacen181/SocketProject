package com.sc.month2.StageProject.CreateFactory;

import com.sc.month2.StageProject.Entity.pojo.treasure.Treasure;
import com.sc.month2.StageProject.Entity.pojoImplement.treasure.armor.*;
import com.sc.month2.StageProject.Entity.pojoImplement.treasure.weapon.*;
import com.sc.month2.StageProject.constant.Rarity;

import java.util.concurrent.ThreadLocalRandom;

public class TreasureFactory {

    // 线程安全的随机数生成器
    private static final ThreadLocalRandom random = ThreadLocalRandom.current();

    /**
     * 根据楼层计算稀有度权重
     */
    public static double getRarityFactor(int floorLevel) {
        return 1.0 + Math.log1p(floorLevel) * 0.05; // 对数增长，减缓权重提升速度
    }

    /**
     * 生成随机宝物（武器或防具）
     */
    public static Treasure createRandomTreasure(int floorLevel) {
        boolean isWeapon = random.nextBoolean();
        double rarityFactor = getRarityFactor(floorLevel);
        double rarityRoll = random.nextDouble() * rarityFactor;
        Rarity rarity = determineRarity(floorLevel, rarityRoll);
        return createTreasureByRarity(isWeapon, rarity);
    }

    private static Rarity determineRarity(int floorLevel, double rarityRoll) {
        if (floorLevel < 20) {
            if (rarityRoll < 0.7) return Rarity.BEGINNER;
            else if (rarityRoll < 0.95) return Rarity.NORMAL;
            else if (rarityRoll < 0.99) return Rarity.ELITE;
            else return Rarity.EPIC;
        } else if (floorLevel < 30) {
            if (rarityRoll < 0.5) return Rarity.BEGINNER;
            else if (rarityRoll < 0.85) return Rarity.NORMAL;
            else if (rarityRoll < 0.96) return Rarity.ELITE;
            else if (rarityRoll < 0.99) return Rarity.EPIC;
            else return Rarity.LEGENDARY;
        } else {
            if (rarityRoll < 0.3) return Rarity.BEGINNER;
            else if (rarityRoll < 0.7) return Rarity.NORMAL;
            else if (rarityRoll < 0.9) return Rarity.ELITE;
            else if (rarityRoll < 0.98) return Rarity.EPIC;
            else return Rarity.LEGENDARY;
        }
    }

    /**
     * 根据稀有度和类型生成具体的宝物
     */
    private static Treasure createTreasureByRarity(boolean isWeapon, Rarity rarity) {
        if (isWeapon) {
            switch (rarity) {
                case NORMAL:
                    return new SteelSword();
                case ELITE:
                    return new MithrilSword();
                case EPIC:
                    return new DragonToothGreatsword();
                case LEGENDARY:
                    return new GodForgedBlade();
                default:
                    return new WoodenSword();
            }
        } else {
            switch (rarity) {
                case NORMAL:
                    return new ChainMail();
                case ELITE:
                    return new KnightPlateArmor();
                case EPIC:
                    return new DragonScaleArmor();
                case LEGENDARY:
                    return new GodForgedArmor();
                default:
                    return new WoodenArmor();
            }
        }
    }

    /**
     * 直接生成传奇等级的随机宝物
     */
    public static Treasure createLegendaryEquipment() {
        boolean isWeapon = random.nextBoolean();
        return createTreasureByRarity(isWeapon, Rarity.LEGENDARY);
    }
}

