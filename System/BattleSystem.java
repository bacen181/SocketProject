package com.sc.month2.StageProject.System;

import com.sc.month2.StageProject.CreateFactory.MonsterFactory;
import com.sc.month2.StageProject.Entity.pojo.player.PlayerInfo;
import com.sc.month2.StageProject.Entity.pojo.player.PlayerStatus;
import com.sc.month2.StageProject.Entity.pojo.monster.Monster;
import com.sc.month2.StageProject.Entity.pojo.player.SocketInfo;
import com.sc.month2.StageProject.Entity.pojo.treasure.Treasure;
import com.sc.month2.StageProject.Entity.pojo.info.BattleInfo;
import com.sc.month2.StageProject.Socket.PlayerSocket;
import com.sc.month2.StageProject.constant.CmdMsg;
import com.sc.month2.StageProject.constant.CommandParameters;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;


public class BattleSystem {
    static int round = 0;

    public void battleInfo(){}
    static Scanner input = new Scanner(System.in);

    public static PlayerInfo GameMainMenu(PlayerInfo playerInfo, SocketInfo socketInfo){

        try {
            while (true) {
                playerInfo.getPlayerStatus().setCurrentLevel(1);
                playerInfo.getPlayerStatus().setCurrentHP(playerInfo.getPlayerStatus().getMaxHP());
                PlayerSocket.sendMsgToServer(CommandParameters.SAVE_GAME,socketInfo,playerInfo);
                System.out.println("当前探索最深层数：" + playerInfo.getPlayerStatus().getLevel() + "层");
                System.out.println("当前所在层数：" + playerInfo.getPlayerStatus().getCurrentLevel() + "层");
                // 1. 显示菜单选项
                System.out.println("请选择操作：");
                System.out.println("1. 战斗(进入已探索的最深层)");
                System.out.println("2. 背包");
                System.out.println("3. 存档");
                System.out.println("4. 选择层数");
                System.out.println("5. 查看面板");
                System.out.println("6. 玩家排行榜");
                System.out.println("7. 返回上一级");
                System.out.print("请输入选项(1-7): ");

                // 2. 获取用户输入
                if (!input.hasNextInt()) {
                    System.out.println("错误：请输入数字！");
                    input.next(); // 清除无效输入
                    continue;
                }

                int choice = input.nextInt();
                input.nextLine(); // 清除换行符

                // 3. 处理用户选择
                switch (choice) {
                    /**
                     * 战斗
                     */
                    case 1: {
                        PlayerSocket.sendMsgToServer(CommandParameters.START_BATTLE,socketInfo,playerInfo);
                        System.out.println("【当前层数】:" + playerInfo.getPlayerStatus().getLevel());

                        Thread.sleep(800);
                        CmdMsg returnData2 = (CmdMsg) socketInfo.getOis().readObject();
                        List<Monster> monsterList = (List<Monster>) returnData2.getData();
                        if(returnData2.getCmd().equals(CommandParameters.SUCCESS_CODE)){
                            System.out.println("此时面前出现了" + monsterList.size() + "只怪物");
                            Thread.sleep(800);
                            playerInfo.getPlayerStatus().setCurrentLevel(playerInfo.getPlayerStatus().getLevel());
                            PlayerSocket.sendMsgToServer(CommandParameters.SAVE_GAME,socketInfo,playerInfo);
                            playerInfo = battleMenu(playerInfo,socketInfo, monsterList,new BattleInfo(monsterList,playerInfo));

                        }
                        break;
                    }
                    /**
                     * 背包
                     */
                    case 2: {

                        CmdMsg respMsg = PlayerSocket.sendMsgToServer(CommandParameters.OPEN_BACKPACK,socketInfo,playerInfo.getPlayerStatus().getBag());


                        if(respMsg.getCmd().equals(CommandParameters.SUCCESS_CODE)){
                            playerInfo = EquipmentSystem.printBagSocket(socketInfo,playerInfo);
                        }
                        break;
                    }
                    /**
                     * 存档
                     */
                    case 3: {
                        CmdMsg respMsg = PlayerSocket.sendMsgToServer(CommandParameters.SAVE_GAME,socketInfo,playerInfo);
                        if(respMsg.getCmd().equals(CommandParameters.SUCCESS_CODE)){
                            System.out.println("存档成功");
                        }
                        break;

                    }
                    /**
                     * 选择层数
                     */
                    case 4: {
                        CmdMsg returnData1 = PlayerSocket.sendMsgToServer(CommandParameters.SELECT_LEVEL,socketInfo,playerInfo);
                        if(returnData1.getCmd().equals(CommandParameters.SUCCESS_CODE)){
                            System.out.println("当前探索的最深层数为：" + returnData1.getData());
                            System.out.println("你可以选择该层数之前的所有层数");
                            System.out.print("请输入你的层数：(1~" + returnData1.getData() + ")");

                            if (!input.hasNextInt()) {
                                System.out.println("错误：请输入数字！");
                                input.next(); // 清除无效输入
                                continue;
                            }

                            int level = input.nextInt();
                            input.nextLine(); // 清除换行符


                            if(level > (Integer) returnData1.getData()){
                                System.err.println("超出你所探索的最大层数，请重试");
                                Thread.sleep(800);
                                continue;
                            }
                            if(level < 0){
                                System.err.println("层数不能为负数，请重试");
                                Thread.sleep(800);
                                continue;
                            }
                            if(level == 0){
                                System.out.println("恭喜你发现彩蛋【第0层】，其实是bug,作者懒得写,请从第0层开始吧");
                                Thread.sleep(800);
                            }
                            playerInfo.getPlayerStatus().setCurrentLevel(level);
                            CmdMsg respData = PlayerSocket.sendMsgToServer(CommandParameters.SAVE_GAME,socketInfo,playerInfo);
                            playerInfo = (PlayerInfo) respData.getData();

                            CmdMsg returnData2 = PlayerSocket.sendMsgToServer(CommandParameters.CREATE_MONSTER,socketInfo,level);
                            List<Monster> monsterList = (List<Monster>) returnData2.getData();
                            if(returnData2.getCmd().equals(CommandParameters.SUCCESS_CODE)){
                                System.out.println("【当前层数】：" + level);

                                Thread.sleep(800);
                                System.out.println("此时面前出现了" + monsterList.size() + "只怪物");
                                Thread.sleep(800);
                                playerInfo = battleMenu(playerInfo,socketInfo,monsterList,new BattleInfo(monsterList,playerInfo));
                            }
                        }else {
                            System.out.println("发生未知错误，请重试");
                            break;
                        }
                        break;
                    }
                    case 5:
                        System.out.println("============================\n" + "昵称: " + playerInfo.getNickName() + " " + playerInfo.getPlayerStatus());
                        break;
                    case 6:
                        AtomicInteger rank  = new AtomicInteger(1);
                        CmdMsg cmdMsg = PlayerSocket.sendMsgToServer(CommandParameters.GET_LEVEL_ALL, socketInfo, null);
                        Map<String, PlayerInfo> playerInfoMap = (Map<String, PlayerInfo>) cmdMsg.getData();
                        playerInfoMap.entrySet().stream()
                                .filter(entry -> entry.getValue() != null && entry.getValue().getPlayerStatus() != null)
                                .sorted((entry1, entry2) ->
                                        Integer.compare(
                                                entry2.getValue().getPlayerStatus().getLevel(),
                                                entry1.getValue().getPlayerStatus().getLevel()
                                        )
                                )
                                .forEach(entry -> {
                                    int currentRank = rank.getAndIncrement();
                                    String crown;
                                    switch (currentRank) {
                                        case 1: crown = "👑"; break; // 第一名
                                        case 2: crown = "🥈"; break; // 第二名
                                        case 3: crown = "🥉"; break; // 第三名
                                        default: crown = ""; break;
                                    }

                                    PlayerInfo player = entry.getValue();
                                    String nickname = player.getNickName();
                                    int level = player.getPlayerStatus().getLevel();

                                    System.out.println(crown + " " + currentRank + "  昵称: " + nickname + ", 所达最深层: " + level);
                                });
                        break;
                    case 7:
                        System.out.println("返回上一级");
                        return playerInfo;
                    default:
                        System.out.println("错误：无效选项，请重新输入！");
                        break;
                }
                playerInfo.getPlayerStatus().setCurrentLevel(playerInfo.getPlayerStatus().getLevel());
                PlayerSocket.sendMsgToServer(CommandParameters.SAVE_GAME,socketInfo,playerInfo);

            }
        } catch (Exception e) {
            System.out.println("发生错误: " + e.getMessage());
            input.nextLine(); // 清除错误输入
        }
        return playerInfo;
    }

    public static PlayerInfo battleMenu(PlayerInfo playerInfo, SocketInfo socketInfo, List<Monster> monsterList, BattleInfo battleInfo) {
        try {
            while (true) {

                System.out.println(battleInfo);
                Thread.sleep(500);
                System.out.println("=== 战斗选项 ===");
                System.out.println("1. 平A(别问为什么没有技能，问就是没做)");
                System.out.println("2. 查看背包（时停这一块）");
                System.out.println("3. 查看面板（角色属性）");
                System.out.println("4. 逃跑");
                Thread.sleep(200);
                System.out.print("你的选择是: ");

                // 1. 获取用户输入
                if (!input.hasNextInt()) {
                    System.out.println("错误：请输入数字！");
                    input.next(); // 清除无效输入
                    continue;
                }

                int choice = input.nextInt();
                input.nextLine(); // 清除换行符

                // 2. 处理用户选择
                switch (choice) {
                    case 1: { // 攻击选项
                        int size = monsterList.size() - battleInfo.getMonsterList().size();
                        if (size != 0){
                            System.out.print("请选择你要攻击的怪物编号(1~" + size + ")：");
                        }else {
                        System.out.print("请选择你要攻击的怪物编号(1~" + monsterList.size() + ")：");
}
                        if (!input.hasNextInt()) {
                            System.out.println("错误：请输入数字！");
                            input.next();
                            break;
                        }

                        int monsterNum = input.nextInt();
                        input.nextLine();

                        if (monsterNum < 1 || monsterNum > monsterList.size()) {
                            System.err.println("超出怪物编号范围！！！有效编号是1到" + monsterList.size());
                            break;
                        }

                        try {
                            // 发送攻击请求并处理响应
                            if (battleInfo == null) {
                                battleInfo = new BattleInfo(monsterList, playerInfo, monsterNum - 1);
                            }

                            battleInfo.setMonsterNum(monsterNum - 1);

                            CmdMsg cmdMsg = PlayerSocket.sendMsgToServer(CommandParameters.ATTACK, socketInfo, battleInfo);
                            /* 获取勇者攻击信息 */
                            Thread.sleep(400);
                            if (cmdMsg.getCmd().equals(CommandParameters.SUCCESS_CODE)) {
                                System.out.println(cmdMsg.getData());
                            }
                            /* 获取怪物攻击信息 */
                            Thread.sleep(400);
                            CmdMsg msgFromServer = PlayerSocket.getMsgFromServer(socketInfo);
                            if (msgFromServer.getCmd().equals(CommandParameters.SUCCESS_CODE)) {
                                System.out.println(msgFromServer.getData());
                            }


                            // 尝试获取战斗信息
                            battleInfo = (BattleInfo) PlayerSocket.getMsgFromServer(socketInfo).getData();

                            if (battleInfo.hasDefeatedMonsters()) {
                                System.out.println(battleInfo.getTreasureList());
                            }


                            if (!playerInfo.getPlayerStatus().getRank().equals(battleInfo.getPlayerInfo().getPlayerStatus().getRank())) {
                                System.out.println("玩家升级 Rank:" + playerInfo.getPlayerStatus().getRank() + "->" + "Rank:" + battleInfo.getPlayerInfo().getPlayerStatus().getRank());
                            }

                            // 更新战斗状态
                            playerInfo = battleInfo.getPlayerInfo();

                            // 处理战斗结果
                            if (playerDead(battleInfo.getPlayerInfo().getPlayerStatus())) {
                                System.out.println("玩家已死亡,游戏结束,请重新开始 !!!");
                                playerInfo.setPlayerStatus(new PlayerStatus());
                                MonsterFactory.resetForPlayer(playerInfo);
                                return playerInfo;
                            }

                            EquipmentSystem.handleMonsterDeathAndLoot(battleInfo, monsterNum, monsterList);

                            // 检查是否进入下一层
                            if (battleInfo.getMonsterList().isEmpty()) {
                                playerInfo = battleInfo.getPlayerInfo();
                                System.out.println("是否进入下一层？(0表示返回，1表示进入）");
                                int opNum = input.nextInt();
                                if (opNum == 1) {
                                    Thread.sleep(800);

                                    System.out.println("🎉 恭喜！进入第 " + playerInfo.getPlayerStatus().getCurrentLevel() + " 层！");
                                    CmdMsg newMonsterCmd = PlayerSocket.sendMsgToServer(
                                            CommandParameters.CREATE_MONSTER, socketInfo, battleInfo.getPlayerInfo()
                                    );
                                    monsterList = (List<Monster>) newMonsterCmd.getData();
                                    battleInfo.setMonsterList(monsterList);
                                    battleInfo.setTreasureList(null);
                                    monsterList = battleInfo.getMonsterList();
                                    Thread.sleep(800);
                                    System.out.println("【当前层数】：" + battleInfo.getPlayerInfo().getPlayerStatus().getCurrentLevel());
                                    Thread.sleep(800);
                                    System.out.println("此时面前出现了" + monsterList.size() + "只怪物");
                                    Thread.sleep(800);
                                } else if (opNum == 0) {

                                    System.out.println("回城了，返回至第一层");
                                    /* 玩家血量已恢复 */

                                    playerInfo.getPlayerStatus().setCurrentHP(playerInfo.getPlayerStatus().getMaxHP());
                                    cmdMsg = PlayerSocket.sendMsgToServer(CommandParameters.SAVE_GAME, socketInfo, playerInfo);
                                    if (cmdMsg.getCmd().equals(CommandParameters.SUCCESS_CODE)) {
                                        System.out.println("玩家血量已恢复");
                                        playerInfo = (PlayerInfo) cmdMsg.getData();
                                    } else {
                                        System.err.println("回城时发生网络波动，程序出错！！！");
                                        playerInfo = (PlayerInfo) cmdMsg.getData();
                                    }
                                    Thread.sleep(800);
                                    return playerInfo;
                                } else {
                                    System.out.println("输入错误，那也回城了，返回至第一层");
                                    /* 玩家血量已恢复 */
                                    playerInfo.getPlayerStatus().setCurrentHP(playerInfo.getPlayerStatus().getMaxHP());
                                    playerInfo.getPlayerStatus().setCurrentLevel(1);
                                    cmdMsg = PlayerSocket.sendMsgToServer(CommandParameters.SAVE_GAME, socketInfo, playerInfo);
                                    if (cmdMsg.getCmd().equals(CommandParameters.SUCCESS_CODE)) {
                                        System.out.println("玩家血量已恢复");
                                    } else {
                                        System.err.println("回城时发生网络波动，程序出错！！！");
                                    }

                                    Thread.sleep(800);
                                    return playerInfo;
                                }

                            }

                        } catch (Exception e) {
                            System.err.println("战斗通信错误: " + e.getMessage());
                            e.printStackTrace();
                            break;
                        }

                        break;
                    }
                    case 2: {// 打开背包
                        CmdMsg respMsg = PlayerSocket.sendMsgToServer(CommandParameters.OPEN_BACKPACK_IN_BATTLE, socketInfo, playerInfo.getPlayerStatus().getBag());
                        if (respMsg.getCmd().equals(CommandParameters.SUCCESS_CODE)) {
                            playerInfo = EquipmentSystem.printBagSocket(socketInfo, playerInfo);
                            battleInfo.setPlayerInfo(playerInfo);
                        }
                        break;
                    }
                    case 3: //查看面板
                        System.out.println("昵称: " + playerInfo.getNickName() + " " + playerInfo.getPlayerStatus());
                        break;
                    case 4:{ // 逃跑

                        System.out.println("你给路打油。。。返回第一层");
                        Thread.sleep(1000);
                        /* 玩家血量已恢复 */
                        playerInfo.getPlayerStatus().setCurrentHP(playerInfo.getPlayerStatus().getMaxHP());
                        playerInfo.getPlayerStatus().setCurrentLevel(1);
                        CmdMsg cmdMsg = PlayerSocket.sendMsgToServer(CommandParameters.SAVE_GAME, socketInfo, playerInfo);
                        if (cmdMsg.getCmd().equals(CommandParameters.SUCCESS_CODE)) {
                            System.out.println("玩家血量已恢复");
                        } else {
                            System.err.println("回城时发生网络波动，程序出错！！！");
                        }
                        return playerInfo;
                    }
                    default:
                        System.out.println("错误：无效选项，请重新输入！");
                        Thread.sleep(800);
                }
            }
        } catch (Exception e) {
            System.err.println("系统错误: " + e.getMessage());
            e.printStackTrace();
        }
        return playerInfo;
    }
    //单次选择攻击的模式
    public static BattleInfo playAttack(BattleInfo battleInfo) throws InterruptedException {


        int monsterNum = battleInfo.getMonsterNum();
        if (monsterNum < 0 || monsterNum >= battleInfo.getMonsterList().size()) {
            throw new IllegalArgumentException("无效的怪物编号: " + monsterNum);
        }
        Monster currentMonster = battleInfo.getMonsterList().get(monsterNum);

        battleInfo.setRound(battleInfo.getRound() + 1);
        //玩家状态
        PlayerStatus playerStatus = battleInfo.getPlayerInfo().getPlayerStatus();
        int playerAttack = playerStatus.getAttack(); // 玩家的攻击力
        // 4. 玩家攻击怪物（使用玩家的攻击力）
        currentMonster.setCurrentHP(currentMonster.getCurrentHP() - playerAttack);

        // 5. 检查怪物是否死亡
        if (monsterDead(battleInfo)) {
            List<Treasure> treasureList = currentMonster.dropItems();
            if(battleInfo.getTreasureList() == null){
                battleInfo.setTreasureList(new ArrayList<>());
                battleInfo.getTreasureList().addAll(EquipmentSystem.DroppedItemSort(battleInfo.getPlayerInfo(), treasureList));
            }
            int expReward = currentMonster.calculateExpReward();
            playerStatus.levelUp(playerStatus,expReward); // 玩家升级
            System.out.println("怪物被击败！玩家获得经验值: " + expReward);
        }

        return battleInfo;
    }
    //怪物攻击
    public static PlayerStatus monsterAttack(PlayerStatus player, Monster monster){
        double v = 0.2;
        int InjuryReduction = (int) (player.getArmor().getArmor() * v);
        player.setCurrentHP(player.getCurrentHP() + InjuryReduction - monster.getAttack());
        return player;
    }

    //玩家死亡
    public static Boolean playerDead(PlayerStatus player){
        if(player.getCurrentHP() <= 0){
            return true;
        }
        return false;
    }

    //怪物死亡
    public static Boolean monsterDead(BattleInfo battleInfo){
        if(battleInfo == null){
            return false;
        }
        if(!battleInfo.getMonsterList().isEmpty() && battleInfo.getMonsterList().get(battleInfo.getMonsterNum()).getCurrentHP() <= 0) {
           return true;
        }
        if(battleInfo.getMonsterList().isEmpty()){
            return true;
        }
        return false;
    }

    /**
     * 更新层数
     * @param battleInfo
     * @return
     */
    public static BattleInfo levelUpdate(BattleInfo battleInfo){
        PlayerStatus playerStatus = battleInfo.getPlayerInfo().getPlayerStatus();
        int newLevel = playerStatus.getCurrentLevel()+1;
        System.out.println(newLevel);
        System.out.println(playerStatus.getCurrentLevel());
        if(newLevel > playerStatus.getLevel()){
            playerStatus.setLevel(newLevel);
        }
        playerStatus.setCurrentLevel(newLevel);
        System.out.println(playerStatus.getCurrentLevel());
        return battleInfo;
    }

    //玩家逃跑
    public static Boolean playerEscape(){
        return true;
    }


}
