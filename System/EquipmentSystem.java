package com.sc.month2.StageProject.System;

import com.sc.month2.StageProject.Entity.pojo.monster.Monster;
import com.sc.month2.StageProject.Entity.pojo.player.Bag;
import com.sc.month2.StageProject.Entity.pojo.player.PlayerInfo;
import com.sc.month2.StageProject.Entity.pojo.player.PlayerStatus;
import com.sc.month2.StageProject.Entity.pojo.player.SocketInfo;
import com.sc.month2.StageProject.Entity.pojo.treasure.*;
import com.sc.month2.StageProject.Entity.pojo.info.BattleInfo;
import com.sc.month2.StageProject.Entity.pojo.info.EquipmentInfo;
import com.sc.month2.StageProject.Socket.GameServerSocket;
import com.sc.month2.StageProject.Socket.PlayerSocket;
import com.sc.month2.StageProject.constant.CmdMsg;
import com.sc.month2.StageProject.constant.CommandParameters;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EquipmentSystem {
    private static final Scanner input = new Scanner(System.in);

    public static String printBagSocket() {
        String menu = "=== 背包 ===\n" +
                "1. 武器\n" +
                "2. 防具\n" +
                "3. 药水\n" +
                "4. 返回\n" ;

        return menu;
    }


    public static PlayerInfo printBagSocket(SocketInfo socketInfo, PlayerInfo playerInfo) {

        try {
        // 获取用户输入

            while (true) {
                // 打印菜单选项
                System.out.println(printBagSocket());
                Thread.sleep(500);
                System.out.println( "请选择要查看的物品类型: ");
                if (input.hasNextInt()) {
                    int choice = input.nextInt();
                    input.nextLine(); // 消耗换行符
                    CmdMsg cmdMsg = null;
                    switch (choice) {
                        case 1:
                            cmdMsg = PlayerSocket.sendMsgToServer(CommandParameters.OPEN_WEAPON, socketInfo, playerInfo);
                            if(cmdMsg.getCmd().equals(CommandParameters.SUCCESS_CODE)){
                                playerInfo = (PlayerInfo) cmdMsg.getData();
                                playerInfo = printUse(playerInfo.getPlayerStatus().getBag().getWeaponBag(),socketInfo,playerInfo);
                            }
                            break;
                        case 2:
                            cmdMsg = PlayerSocket.sendMsgToServer(CommandParameters.OPEN_ARMOR,socketInfo,playerInfo);
                            if(cmdMsg.getCmd().equals(CommandParameters.SUCCESS_CODE)){
                                playerInfo = (PlayerInfo) cmdMsg.getData();
                                playerInfo = printUse(playerInfo.getPlayerStatus().getBag().getArmorBag(),socketInfo,playerInfo);
                            }
                            break;
                        case 3:
                            cmdMsg = PlayerSocket.sendMsgToServer(CommandParameters.OPEN_POTION,socketInfo,playerInfo);
                            if(cmdMsg.getCmd().equals(CommandParameters.SUCCESS_CODE)){
                                playerInfo = (PlayerInfo) cmdMsg.getData();
                                playerInfo = printUse(playerInfo.getPlayerStatus().getBag().getPotionBag(),socketInfo,playerInfo);
                            }
                            break;
                        case 4:
                            System.out.println("返回上一级");
                            Thread.sleep(800);
                            return playerInfo;
                        default:
                            System.out.print("无效输入，请重新选择 (1-4): ");
                            Thread.sleep(800);
                            break;
                    }

                } else {
                    System.out.print("请输入数字 (1-4): ");
                    input.next(); // 消耗无效输入
                }
            }
        } catch (Exception e) {
            System.out.println("printBagSocket下未知错误");
        }
        return playerInfo;
    }




    //背包栏
    public static PlayerInfo printUse( List<? extends Treasure> itemList,SocketInfo socketInfo,PlayerInfo playerInfo) throws InterruptedException {
            if (itemList.isEmpty()) {
                System.out.println("当前栏位没有道具！");
                return playerInfo;
            }

            // 根据列表类型确定标题
            String title = "======物品栏======";
            if (!itemList.isEmpty()) {
                Treasure firstItem = itemList.get(0);
                if (firstItem instanceof Weapon) {
                    title = "======武器栏======";
                } else if (firstItem instanceof Armor) {
                    title = "======防具栏======";
                } else if (firstItem instanceof Potion) {
                    title = "======药水栏======";
                }
            }

            // 打印标题和物品列表
            System.out.println(title);
            for (int i = 0; i < itemList.size(); i++) {
                Thread.sleep(100);
                System.out.println(i + " --> " + itemList.get(i));
            }


            // 进入操作选择
            playerInfo = printUseList(itemList,socketInfo,playerInfo);
            return playerInfo;
    }



    //使用药水
    public static EquipmentInfo UsePotion(EquipmentInfo equipmentInfo){
        PlayerStatus playerStatus = equipmentInfo.getPlayerInfo().getPlayerStatus();
        Integer num = equipmentInfo.getNum();
        List<Potion> potionBag = playerStatus.getBag().getPotionBag();
        Potion potion = potionBag.get(num);
        int HP = potion.getReplyHP() + playerStatus.getCurrentHP();
        if(HP > playerStatus.getMaxHP()){
            playerStatus.setCurrentHP(playerStatus.getMaxHP());
        }else{
            playerStatus.setCurrentHP(HP);
        }
        playerStatus.getBag().getPotionBag().remove(potion);
        return equipmentInfo;
    }



    /**
     * 处理物品使用列表
     * @param treasuresList 物品列表
     * @param socketInfo socket信息
     * @param playerInfo 玩家信息
     */
    public static PlayerInfo printUseList(List<? extends Treasure> treasuresList,
                                    SocketInfo socketInfo, PlayerInfo playerInfo) throws InterruptedException {
        Thread.sleep(500);
        System.out.print("输入你要选定的物品（输入-1返回）：");

        while (true) {
            try {
                if (!input.hasNextInt()) {
                    System.out.println("请输入数字！");
                    input.next(); // 消耗无效输入
                    continue;
                }

                int choice = input.nextInt();
                input.nextLine(); // 消耗换行符

                if (choice == -1) {
                    System.out.println("返回上一级");
                    Thread.sleep(800);
                    return playerInfo;
                }

                if (choice < 0 || choice >= treasuresList.size()) {
                    System.out.print("输入无效，请重新输入！");
                    Thread.sleep(800);
                    continue;
                }

                playerInfo = handleSelectedItem(treasuresList.get(choice), choice, socketInfo, playerInfo);
                return playerInfo;
            } catch (Exception e) {
                System.out.println("发生错误：" + e.getMessage());
                input.nextLine(); // 清除错误输入
            }
        }
    }

    /**
     * 处理选中的物品
     */
    private static PlayerInfo handleSelectedItem(Treasure treasure, int index,
                                           SocketInfo socketInfo, PlayerInfo playerInfo) throws InterruptedException {
        printItemDetails(treasure);

        if (treasure instanceof Potion) {
            playerInfo = handlePotion((Potion) treasure, index, socketInfo, playerInfo);
        } else if (treasure instanceof Weapon) {
            playerInfo = handleWeapon((Weapon) treasure, index, socketInfo, playerInfo);
        } else if (treasure instanceof Armor) {
            playerInfo = handleArmor((Armor) treasure, index, socketInfo, playerInfo);
        }

        return playerInfo;
    }

    /**
     * 打印物品详情
     */
    private static void printItemDetails(Treasure treasure) {
        System.out.println("===============");
        System.out.println(treasure);
        System.out.println("===============");
    }

    /**
     * 处理药水操作
     */
    private static PlayerInfo handlePotion(Potion potion, int index,
                                     SocketInfo socketInfo, PlayerInfo playerInfo) throws InterruptedException {
        Thread.sleep(500);
        System.out.println("1.使用\n2.丢弃\n3.返回");

        int opNum = input.nextInt();

        try {
            CmdMsg cmdMsg = null;
            EquipmentInfo equipmentInfo = new EquipmentInfo(playerInfo, index);
            switch (opNum) {
                case 1:
                    cmdMsg = PlayerSocket.sendMsgToServer(CommandParameters.USE_POTION, socketInfo, equipmentInfo);
                    if (cmdMsg.getCmd().equals(CommandParameters.SUCCESS_CODE)) {
                        System.out.println("已使用");
                    }
                    break;
                case 2:
                    cmdMsg = PlayerSocket.sendMsgToServer(CommandParameters.DISCARD_POTION, socketInfo, equipmentInfo);
                    if (cmdMsg.getCmd().equals(CommandParameters.SUCCESS_CODE)) {
                        System.out.println("已丢弃");
                    }
                    break;
                case 3:
                    System.out.println("返回上一级");
                    break;
                default:
                    System.out.println("无效操作");
            }

            equipmentInfo  = (EquipmentInfo) cmdMsg.getData();
            playerInfo.setPlayerStatus(equipmentInfo.getPlayerInfo().getPlayerStatus());
            System.out.println(playerInfo.getPlayerStatus());

        } catch (Exception e) {
            System.out.println("操作失败：" + e.getMessage());
        }
        return playerInfo;
    }

    /**
     * 处理武器操作
     */
    private static PlayerInfo handleWeapon(Weapon weapon, int index,
                                     SocketInfo socketInfo, PlayerInfo playerInfo) throws InterruptedException {
        Thread.sleep(500);
        System.out.println("1.装备\n2.丢弃\n3.返回");
        int opNum = input.nextInt();

        try {
            CmdMsg cmdMsg = null;
            EquipmentInfo equipmentInfo = new EquipmentInfo(playerInfo, index);
            switch (opNum) {
                case 1:
                    cmdMsg = PlayerSocket.sendMsgToServer(CommandParameters.EQUIP_WEAPON, socketInfo, equipmentInfo);
                    if (cmdMsg.getCmd().equals(CommandParameters.SUCCESS_CODE)) {
                        System.out.println("武器已装备");
                        Thread.sleep(800);
                    }
                    break;
                case 2:
                    cmdMsg = PlayerSocket.sendMsgToServer(CommandParameters.DISCARD_WEAPON, socketInfo, equipmentInfo);
                    if (cmdMsg.getCmd().equals(CommandParameters.SUCCESS_CODE)) {
                    System.out.println("已丢弃");
                        Thread.sleep(800);
                    }
                    break;
                case 3:
                    System.out.println("返回上一级");
                    Thread.sleep(800);
                    break;
                default:
                    System.out.println("无效操作");
                    Thread.sleep(800);
            }
            equipmentInfo  = (EquipmentInfo) cmdMsg.getData();
            playerInfo.setPlayerStatus(equipmentInfo.getPlayerInfo().getPlayerStatus());
            System.out.println(playerInfo.getPlayerStatus());
        } catch (Exception e) {
            System.out.println("操作失败：" + e.getMessage());
            Thread.sleep(800);
        }
        return playerInfo;
    }

    /**
     * 处理防具操作
     */
    private static PlayerInfo handleArmor(Armor armor, int index,
                                    SocketInfo socketInfo, PlayerInfo playerInfo) throws InterruptedException {
        Thread.sleep(500);
        System.out.println("1.装备\n2.丢弃\n3.返回");
        int opNum = input.nextInt();

        try {
            CmdMsg cmdMsg = null;
            EquipmentInfo equipmentInfo = new EquipmentInfo(playerInfo, index);
            switch (opNum) {
                case 1:
                    cmdMsg = PlayerSocket.sendMsgToServer(CommandParameters.EQUIP_ARMOR, socketInfo, equipmentInfo);
                    if (cmdMsg.getCmd().equals(CommandParameters.SUCCESS_CODE)) {
                        System.out.println("防具已装备");
                        Thread.sleep(800);
                    }
                    break;
                case 2:
                    cmdMsg = PlayerSocket.sendMsgToServer(CommandParameters.DISCARD_ARMOR, socketInfo, equipmentInfo);
                    if (cmdMsg.getCmd().equals(CommandParameters.SUCCESS_CODE)) {
                        System.out.println("已丢弃");
                        Thread.sleep(800);
                    }
                    break;
                case 3:
                    System.out.println("返回上一级");
                    Thread.sleep(800);
                    break;
                default:
                    System.out.println("无效操作");
                    Thread.sleep(800);
            }

            equipmentInfo  = (EquipmentInfo) cmdMsg.getData();
            playerInfo.setPlayerStatus(equipmentInfo.getPlayerInfo().getPlayerStatus());
            System.out.println(playerInfo.getPlayerStatus());
        } catch (Exception e) {
            System.out.println("操作失败：" + e.getMessage());
        }
        return playerInfo;
    }


    /**
     * 整理玩家掉落的物品，将其分类放入背包的对应列表中
     *
     * @param playerInfo  玩家信息，包含背包（Bag）
     * @param treasureList 掉落的物品列表（包含武器、防具、药水等）
     */
    public static List<Treasure> DroppedItemSort(PlayerInfo playerInfo, List<Treasure> treasureList) throws InterruptedException {
        // 1. 检查输入参数是否有效
        if (playerInfo == null || treasureList == null) {
            System.err.println("错误：玩家信息或掉落物品列表为空");
            Thread.sleep(800);
            return null;
        }

        // 2. 获取玩家的背包
        Bag playerBag = playerInfo.getPlayerStatus().getBag();
        if (playerBag == null) {
            System.err.println("错误：玩家的背包未初始化");
            Thread.sleep(800);
            return null;
        }

        // 3. 遍历掉落的物品列表
        for (Treasure treasure : treasureList) {
            if (treasure instanceof Weapon) {
                // 如果是武器，放入武器背包
                addToBag(playerBag.getWeaponBag(), treasure, "武器背包");
            } else if (treasure instanceof Armor) {
                // 如果是防具，放入防具背包
                addToBag(playerBag.getArmorBag(), treasure, "防具背包");
            } else if (treasure instanceof Potion) {
                // 如果是药水，放入药水背包
                addToBag(playerBag.getPotionBag(), treasure, "药水背包");
            } else {
                // 未知类型的物品，打印警告日志
                System.err.println("警告：未知类型的掉落物品 - " + treasure.getClass().getName());
                Thread.sleep(800);
            }
        }

        // 4. 打印整理结果（可选）
        System.out.println("掉落物品整理完成！");
        Thread.sleep(800);
        return treasureList;
    }

    /**
     * 将物品添加到背包的指定列表中，并检查列表容量
     *
     * @param bagList  目标列表（如 weaponBag、armorBag、potionBag）
     * @param treasure 要添加的物品
     * @param listName 列表名称（用于日志输出）
     */
    private static void addToBag(List<?> bagList, Treasure treasure, String listName) {
        if (bagList.size() >= 10) {
            System.out.println("提示：" + listName + "已满，无法添加更多物品：" + treasure);
            return;
        }

        try {
            if (treasure instanceof Weapon) {
                ((List<Weapon>) bagList).add((Weapon) treasure);
            } else if (treasure instanceof Armor) {
                ((List<Armor>) bagList).add((Armor) treasure);
            } else if (treasure instanceof Potion) {
                ((List<Potion>) bagList).add((Potion) treasure);
            } else {
                System.err.println("错误：无法将物品添加到列表：" + treasure.getClass().getName());
                Thread.sleep(800);
            }
        } catch (ClassCastException | InterruptedException e) {
            System.err.println("类型转换错误：" + e.getMessage());
        }

        System.out.println("物品已添加到 " + listName + "：\n" + treasure);
    }

    /**
     * 处理怪物死亡后的宝物掉落和背包检查
     * @param battleInfo 战斗信息对象
     * @param monsterNum 怪物编号
     * @param monsterList 怪物列表
     */
    public static void handleMonsterDeathAndLoot(BattleInfo battleInfo, int monsterNum, List<Monster> monsterList) throws InterruptedException {
        if (BattleSystem.monsterDead(battleInfo)) {
            // 获取掉落的宝物列表
            List<Treasure> treasureList = battleInfo.getTreasureList();
            if(null == treasureList){
                return;
            }
            System.out.println("此层掉落宝物：");
            for (Treasure treasure : treasureList){
                System.out.println("======宝物信息=======");
                System.out.println(treasure);
                System.out.println("===================");
            }

            // 获取各背包当前大小
            PlayerStatus playerStatus = battleInfo.getPlayerInfo().getPlayerStatus();
            Bag bag = playerStatus.getBag();
            int weaponBagSize = bag.getWeaponBag().size();
            int armorBagSize = bag.getArmorBag().size();
            int potionBagSize = bag.getPotionBag().size();

            // 检查每种类型的宝物是否可以拾取
            List<Treasure> cannotPickUp = new ArrayList<>();

            for (Treasure treasure : treasureList) {
                if (treasure instanceof Weapon && weaponBagSize >= 10) {
                    cannotPickUp.add(treasure);
                } else if (treasure instanceof Armor && armorBagSize >= 10) {
                    cannotPickUp.add(treasure);
                } else if (treasure instanceof Potion && potionBagSize >= 10) {
                    cannotPickUp.add(treasure);
                }
            }

            // 输出无法拾取的宝物
            if (!cannotPickUp.isEmpty()) {
                System.out.println("由于背包已满，以下宝物无法拾取：");
                for (Treasure treasure : cannotPickUp) {
                    String type = "";
                    if (treasure instanceof Weapon) {
                        type = "武器";
                    } else if (treasure instanceof Armor) {
                        type = "防具";
                    } else if (treasure instanceof Potion) {
                        type = "药水";
                    }
                    System.out.println("- " + treasure + " (" + type + ")");
                }
            }

            // 输出各背包状态
            if (weaponBagSize >= 10) {
                System.out.println("警告：武器背包已满（当前数量：" + weaponBagSize + "）");
                Thread.sleep(800);
            }
            if (armorBagSize >= 10) {
                System.out.println("警告：防具背包已满（当前数量：" + armorBagSize + "）");
                Thread.sleep(800);

            }
            if (potionBagSize >= 10) {
                System.out.println("警告：药水背包已满（当前数量：" + potionBagSize + "）");
                Thread.sleep(800);
            }
        }
    }
}
