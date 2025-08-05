package com.sc.month2.StageProject.CreateFactory;

import com.sc.month2.StageProject.Entity.pojo.monster.Monster;
import com.sc.month2.StageProject.Entity.pojo.player.PlayerInfo;
import com.sc.month2.StageProject.Entity.pojoImplement.monster.boss.DeathKnight;
import com.sc.month2.StageProject.Entity.pojoImplement.monster.boss.LichKing;
import com.sc.month2.StageProject.Entity.pojoImplement.monster.elite.Zombie;
import com.sc.month2.StageProject.Entity.pojoImplement.monster.nomal.Slime;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public final class MonsterFactory {
    // 使用并发安全的集合
    private static final List<Class<? extends Monster>> unlockedMonsters = new CopyOnWriteArrayList<>();
    private static final Map<Integer, Class<? extends Monster>> BOSS_MONSTERS = new ConcurrentHashMap<>();
    private static final Random random = new Random();

    // 初始化配置
    static {
        // 初始怪物
        unlockedMonsters.add(Slime.class);

        // BOSS配置
        BOSS_MONSTERS.put(10, Zombie.class);
        BOSS_MONSTERS.put(20, DeathKnight.class);
        BOSS_MONSTERS.put(30, LichKing.class);
    }

    private MonsterFactory() {} // 防止实例化

    public static List<Class<? extends Monster>> getUnlockedMonsters() {
        return Collections.unmodifiableList(unlockedMonsters);
    }

    public static void resetForPlayer(PlayerInfo playerInfo) {
        unlockedMonsters.clear();
        unlockedMonsters.add(Slime.class);
        unlockMonstersUpToFloor(playerInfo.getPlayerStatus().getLevel());
    }

    public static List<Monster> createMonsters(int floor) {
        List<Monster> monsters = new ArrayList<>();

        // 检查是否是BOSS层
        Class<? extends Monster> bossMonster = BOSS_MONSTERS.get(floor);
        if (bossMonster != null) {
            monsters.add(createMonsterInstance(bossMonster, floor));
            return monsters;
        }

        // 普通层生成怪物
        int count = calculateMonsterCount(floor);
        for (int i = 0; i < count; i++) {
            monsters.add(createRandomMonster(floor));
        }

        return monsters;
    }

    private static int calculateMonsterCount(int floor) {
        if (floor < 10) {
            return Math.max(1, floor % 4);
        }

        // 随楼层增加怪物数量(1-4个)
        int baseCount = 1 + floor / 10;
        return Math.min(4, baseCount + random.nextInt(2));
    }

    private static Monster createRandomMonster(int floor) {
        unlockMonstersUpToFloor(floor);

        if (unlockedMonsters.isEmpty()) {
            return new Slime(floor);
        }

        Class<? extends Monster> monsterClass = unlockedMonsters.get(
                random.nextInt(unlockedMonsters.size())
        );

        return createMonsterInstance(monsterClass, floor);
    }

    private static Monster createMonsterInstance(Class<? extends Monster> monsterClass, int floor) {
        try {
            return monsterClass == Slime.class
                    ? new Slime(floor)
                    : monsterClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return new Slime(floor); // 保底返回史莱姆
        }
    }

    private static void unlockMonstersUpToFloor(int floor) {
        BOSS_MONSTERS.entrySet().stream()
                .filter(entry -> entry.getKey() <= floor)
                .map(Map.Entry::getValue)
                .forEach(monsterClass -> {
                    if (!unlockedMonsters.contains(monsterClass)) {
                        unlockedMonsters.add(monsterClass);
                    }
                });
    }
}