package com.sc.month2.StageProject.Entity.pojo.info;

import com.sc.month2.StageProject.Entity.pojo.monster.Monster;
import com.sc.month2.StageProject.Entity.pojo.player.PlayerInfo;
import com.sc.month2.StageProject.Entity.pojo.treasure.Treasure;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class BattleInfo implements Serializable {
    protected static final long serialVersionUID = 1L;
    private List<Monster> monsterList;
    private PlayerInfo playerInfo;
    private Integer monsterNum;
    private List<Treasure> treasureList;
    private int round;

    public BattleInfo() {
    }

    public List<Monster> getMonsterList() {
        return monsterList;
    }

    public void setMonsterList(List<Monster> monsterList) {
        this.monsterList = monsterList;
    }

    public PlayerInfo getPlayerInfo() {
        return playerInfo;
    }

    public void setPlayerInfo(PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
    }

    public Integer getMonsterNum() {
        return monsterNum;
    }

    public void setMonsterNum(Integer monsterNum) {
        this.monsterNum = monsterNum;
    }

    public BattleInfo(PlayerInfo playerInfo, List<Monster> monsterList, List<Treasure> treasureList) {
        this.playerInfo = playerInfo;
        this.monsterList = monsterList;
        this.treasureList = treasureList;
    }

    public List<Treasure> getTreasureList() {
        return treasureList;
    }

    public void setTreasureList(List<Treasure> treasureList) {
        this.treasureList = treasureList;
    }

    public BattleInfo(List<Monster> monsterList, PlayerInfo playerInfo, Integer monsterNum) {
        this.monsterList = monsterList;
        this.playerInfo = playerInfo;
        this.monsterNum = monsterNum;
    }

    public BattleInfo(List<Monster> monsterList, PlayerInfo playerInfo) {
        this.monsterList = monsterList;
        this.playerInfo = playerInfo;
    }
    public BattleInfo(List<Monster> monsterList, PlayerInfo playerInfo, Integer monsterNum, Integer round) {
        this.monsterList = monsterList;
        this.playerInfo = playerInfo;
        this.monsterNum = monsterNum;
        this.round = round;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    @Override
    public String toString() {
        return String.format(
                "[回合%d] %s Rank:%d HP:%d/%d | 敌人:%d个 (%s)",
                round,
                playerInfo.getNickName(),
                playerInfo.getPlayerStatus().getRank(),
                playerInfo.getPlayerStatus().getCurrentHP(),
                playerInfo.getPlayerStatus().getMaxHP(),
                monsterList.size(),
                monsterList.isEmpty()
                        ? "无敌人"
                        : monsterList.stream()
                        .map(m -> String.format("%s (HP:%d, 攻击:%d)", m.getMonsterName(), m.getCurrentHP(), m.getAttack()))
                        .collect(Collectors.joining(", "))
        );
    }

    private Set<Integer> defeatedMonsterIndices = new HashSet<>();

    public void markMonsterDefeated(int index) {
        defeatedMonsterIndices.add(index);
    }

    public Set<Integer> getDefeatedMonsterIndices() {
        return defeatedMonsterIndices;
    }

    public void setDefeatedMonsterIndices(Set<Integer> defeatedMonsterIndices) {
        this.defeatedMonsterIndices = defeatedMonsterIndices;
    }

    // 检查是否有被消灭的怪物
    public boolean hasDefeatedMonsters() {
        return !defeatedMonsterIndices.isEmpty();
    }

    // 安全移除被消灭的怪物
    public void cleanupDefeatedMonsters() {
        Iterator<Monster> iterator = monsterList.iterator();
        int currentIndex = 0;

        while (iterator.hasNext()) {
            iterator.next();
            if (defeatedMonsterIndices.contains(currentIndex)) {
                iterator.remove();
            }
            currentIndex++;
        }
        defeatedMonsterIndices.clear();
    }
}
