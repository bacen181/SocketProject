package com.sc.month2.StageProject.Socket;




import com.sc.month2.StageProject.CreateFactory.MonsterFactory;

import com.sc.month2.StageProject.Entity.pojo.player.PlayerInfo;
import com.sc.month2.StageProject.Entity.pojo.player.PlayerStatus;
import com.sc.month2.StageProject.Entity.pojo.player.SocketInfo;
import com.sc.month2.StageProject.Entity.pojo.treasure.Armor;
import com.sc.month2.StageProject.Entity.pojo.monster.Monster;
import com.sc.month2.StageProject.Entity.pojo.treasure.Potion;
import com.sc.month2.StageProject.Entity.pojo.treasure.Weapon;
import com.sc.month2.StageProject.Entity.pojo.info.BattleInfo;
import com.sc.month2.StageProject.Entity.pojo.info.EquipmentInfo;
import com.sc.month2.StageProject.Entity.pojo.info.LoginInfo;
import com.sc.month2.StageProject.System.BattleSystem;
import com.sc.month2.StageProject.System.EquipmentSystem;
import com.sc.month2.StageProject.System.LoginSystem;
import com.sc.month2.StageProject.constant.CmdMsg;
import com.sc.month2.StageProject.constant.CommandParameters;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class GameServerSocket implements Serializable{

    private static final int SERVER_PORT = 8888;
    private static final int THREAD_COUNT = 10;

    private static Map<String, PlayerInfo> playerInfoMap;
    private static Set<SocketInfo> socketSet = new HashSet<>();


    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        try {
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            while (true) {
                Socket socket = serverSocket.accept();

                executorService.execute(() -> handleThreadTask(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void handleThreadTask(Socket socket) {
        try {
            File file = new File("src/main/resources/playerInfo/playInfo.obj");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileInputStream is = new FileInputStream(file);
            if (is.available() > 0) {
                System.out.println("********正在获取文件中数据*******");
                ObjectInputStream ois = new ObjectInputStream(is);
                playerInfoMap = (Map<String, PlayerInfo>) ois.readObject();
            } else {
                playerInfoMap = new HashMap<>();
            }
            playerInfoMap.forEach((k,v)-> System.out.println(k + " " + v));
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            SocketInfo socketInfo = new SocketInfo(oos, ois);
            socketSet.add(socketInfo);
            //用于保存信息
            ObjectOutputStream mainOos = null;
            while (true) {
                CmdMsg cmdMsg = (CmdMsg) socketInfo.getOis().readObject();
                System.out.println("收到客户端命令: " + cmdMsg.getCmd());
                int cmd = cmdMsg.getCmd().getId();

                switch (cmd) {

                    /* 登录(已完成) */
                    case 100:{
                        LoginInfo loginInfo = (LoginInfo) cmdMsg.getData();
                        PlayerInfo login = LoginSystem.login(playerInfoMap, socketInfo, loginInfo);
                        if(login != null){
                            System.out.println("登陆成功");
                            sendSuccessResponse(socketInfo, login);
                        }

                        break;
                    }

                    /* 注册(已完成) */
                    case 101: {
                        PlayerInfo playerInfo = (PlayerInfo) cmdMsg.getData();
                        if(playerInfoMap.containsKey(playerInfo.getNickName())){
                            sendFailResponse(socketInfo, "该用户已存在，注册失败 ！！！");
                            break;
                        }
                        playerInfoMap.put(playerInfo.getNickName(), playerInfo);
                        sendSuccessResponse(socketInfo, playerInfo);
                        System.out.println("注册成功");
                        break;
                    }

                    /* 退出游戏 让客户端自己做*/
                    case 102:
                        System.out.println("退出");
                        break;

                    /*新的开始(已完成)  用于定义新角色状态 并 使客户端进入下一层菜单*/
                    case 201: {
                        PlayerInfo playerInfo = (PlayerInfo) cmdMsg.getData();
                        playerInfo.setPlayerStatus(new PlayerStatus());
                        MonsterFactory.resetForPlayer(playerInfo);
                        sendSuccessResponse(socketInfo, playerInfo);
                        break;
                    }

                    /* 读取存档(已完成)  获取文件数据获得客户端之前的游戏数据并返回用户状数据 使客户端进入下一层菜单 */
                    case 202: {
                        PlayerInfo playerInfo = (PlayerInfo) cmdMsg.getData();
                        if (playerInfo.getPlayerStatus() == null)
                            sendFailResponse(socketInfo, "没有存档，请创建新角色重新开始");
                        else sendSuccessResponse(socketInfo, playerInfo);
                        break;
                    }
                    /* 退出 用户自己做 */
                    case 203:
                        break;
                    /* 战斗 ： 直接进入当前用户所达最深层 并使用户进入战斗菜单，并生成怪物*/
                    case 300:{
                        PlayerInfo playerInfo = (PlayerInfo) cmdMsg.getData();
                        Integer maxLevel = playerInfo.getPlayerStatus().getLevel();
                        System.out.println("获取当前最高层数为：" + maxLevel);
                        sendSuccessResponse(socketInfo,maxLevel);

                        System.out.println("生成怪物。。。");
                        List<Monster> monsters = MonsterFactory.createMonsters(maxLevel);
                        sendSuccessResponse(socketInfo,monsters);
                    }
                        break;
                    /* 打开背包 返回背包菜单数据 ： 三个包 ，使用户进入背包菜单*/
                    case 301:{
                        System.out.println("玩家进入背包");
                        sendSuccessResponse(socketInfo,EquipmentSystem.printBagSocket());
                        break;
                    }
                    /* 选择层数： 先获取用户的最深层，返回最深层，客户端可选择最深层以前的所有层，并生成怪物 */
                    case 302: {
                        PlayerInfo playerInfo = (PlayerInfo) cmdMsg.getData();
                        Integer maxLevel = playerInfo.getPlayerStatus().getLevel();
                        System.out.println("获取当前最高层数为：" + maxLevel);
                        sendSuccessResponse(socketInfo, maxLevel);

                        break;
                    }
                    /* 存档，保存playInfo */
                    case 303: {
                        PlayerInfo playerInfo = (PlayerInfo) cmdMsg.getData();
                        playerInfoMap.put(playerInfo.getNickName(), playerInfo);
                        sendSuccessResponse(socketInfo, playerInfo);
                        mainOos = new ObjectOutputStream(new FileOutputStream(file));
                        mainOos.writeObject(playerInfoMap);
                        mainOos.flush();
                        mainOos.reset();
                        break;
                    }
                    /* 返回到上一层 case 200 菜单*/
                    case 304:
                        break;
                    case 305:
                        sendSuccessResponse(socketInfo, playerInfoMap);
                        break;
                    /**
                     * 战斗菜单
                     */
                    /* 攻击 选择敌人进行攻击(允许一对多)按照玩家，怪物顺序 */
                    case 400: {

                        try {
                            // 1. 获取战斗信息
                            BattleInfo battleInfo = (BattleInfo) cmdMsg.getData();

                            // 2. 统一使用0-based索引
                            int monsterNum = battleInfo.getMonsterNum(); // 确保getMonsterNum()返回0-based索引

                            // 3. 验证怪物编号
                            if (monsterNum < 0 || monsterNum >= battleInfo.getMonsterList().size()) {
                                throw new IllegalArgumentException("无效的怪物编号: " + (monsterNum)); // 转换为1-based显示
                            }

                            // 4. 获取战斗对象
                            Monster monster = battleInfo.getMonsterList().get(monsterNum); // 直接使用0-based
                            PlayerStatus playerStatus = battleInfo.getPlayerInfo().getPlayerStatus();

                            // 5. 检查战斗状态完整性
                            if (playerStatus == null || monster == null) {
                                throw new IllegalStateException("战斗状态不完整：玩家状态或怪物信息缺失");
                            }

                            // 6. 输出攻击前状态（调试用）
                            System.out.println("攻击前勇者HP: " + playerStatus.getCurrentHP());
                            System.out.println("攻击前怪物HP: " + monster.getCurrentHP());

                            // 7. 执行玩家攻击
                            battleInfo = BattleSystem.playAttack(battleInfo);

                            // 8. 检查怪物是否死亡
                            boolean monsterDead = BattleSystem.monsterDead(battleInfo);

                            String attackMsg = "你对"+ monster.getMonsterName() +"造成" + battleInfo.getPlayerInfo().getPlayerStatus().getAttack() + "点伤害";

                            sendSuccessResponse(socketInfo,attackMsg);



                            if (monsterDead) {
                                attackMsg = "怪物已消灭";
                                sendSuccessResponse(socketInfo,attackMsg);
                                battleInfo.markMonsterDefeated(monsterNum); // 标记怪物被击败
                            } else {
                                // 只有怪物未死亡时，怪物才反击
                                playerStatus = BattleSystem.monsterAttack(playerStatus, monster);
                                battleInfo.getPlayerInfo().setPlayerStatus(playerStatus); // 更新玩家状态
                                int monsterAttackNum = battleInfo.getMonsterList().get(battleInfo.getMonsterNum()).getAttack() -
                                        (int) (playerStatus.getArmor().getArmor() * 0.2);
                                attackMsg = monster.getMonsterName() + "对你造成" + monsterAttackNum + "点伤害";
                                sendSuccessResponse(socketInfo,attackMsg);
                                if (BattleSystem.playerDead(playerStatus)) {
                                    System.out.println("勇者已死亡");
                                }
                            }

                            // 9. 处理被击败的怪物（延迟清理）
                            if (battleInfo.hasDefeatedMonsters()) {
                                battleInfo.cleanupDefeatedMonsters(); // 安全移除被消灭的怪物
                            }

                            // 10. 检查是否所有怪物被消灭，进入下一层
                            if (battleInfo.getMonsterList().isEmpty()) {
                                battleInfo = BattleSystem.levelUpdate(battleInfo);
                                System.out.println("🎉 恭喜！进入第 " + battleInfo.getPlayerInfo().getPlayerStatus().getCurrentLevel() + " 层！");
                                battleInfo.setRound(0);


                            } else {
                                System.out.println(battleInfo); // 显示当前战斗状态
                            }

                            // 11. 设置战斗结果到响应
                            playerInfoMap.put(battleInfo.getPlayerInfo().getNickName(), battleInfo.getPlayerInfo());
                            sendSuccessResponse(socketInfo, battleInfo);
                            break;
                        }catch (Exception e){
                            System.err.println("程序出错");
                            sendFailResponse(socketInfo,e);
                        }
                    }
                    /* 打开背包 */
                    case 401: {
                        System.out.println("玩家进入背包");
                        sendSuccessResponse(socketInfo,EquipmentSystem.printBagSocket());
                        break;
                    }
                    /* 逃跑 */
                    case 402: {
                        break;
                    }
                    /* 打开武器栏 */
                    case 500: {
                        PlayerInfo playerInfo = (PlayerInfo) cmdMsg.getData();
                        sendSuccessResponse(socketInfo,playerInfo);
                        break;
                    }
                    /* 打开防具栏 */
                    case 501:{
                        PlayerInfo playerInfo = (PlayerInfo) cmdMsg.getData();
                        sendSuccessResponse(socketInfo,playerInfo);
                        break;
                    }
                    /* 打开药水栏 */
                    case 502: {
                        PlayerInfo playerInfo = (PlayerInfo) cmdMsg.getData();
                        sendSuccessResponse(socketInfo,playerInfo);
                        break;
                    }
                    /* 返回 */
                    case 503: {
                        break;
                    }
                    /* 装备武器 */
                    case 600: {
                        EquipmentInfo equipmentInfo = (EquipmentInfo) cmdMsg.getData();
                        Weapon weapon = equipmentInfo.getPlayerInfo().getPlayerStatus().getBag().getWeaponBag().get(equipmentInfo.getNum());
                        if(weapon.getUse()){
                            System.out.println("该武器已装备");
                            sendSuccessResponse(socketInfo,equipmentInfo);
                        }
                        /**
                         * 当前装备武器
                         */
                        equipmentInfo.getPlayerInfo().getPlayerStatus().getWeapon().setUse(false);
                        equipmentInfo.getPlayerInfo().getPlayerStatus().setWeapon(weapon);
                        weapon.setUse(true);
                        //更新勇者攻击力
                        Integer baseAttack = equipmentInfo.getPlayerInfo().getPlayerStatus().getBaseAttack();
                        equipmentInfo.getPlayerInfo().getPlayerStatus().setAttack(baseAttack,weapon);
                        playerInfoMap.put(equipmentInfo.getPlayerInfo().getNickName(),equipmentInfo.getPlayerInfo());
                        sendSuccessResponse(socketInfo,equipmentInfo);
                        break;
                    }
                    /* 丢弃武器 */
                    case 601: {
                        EquipmentInfo equipmentInfo = (EquipmentInfo) cmdMsg.getData();
                        List<Weapon> weaponBag = equipmentInfo.getPlayerInfo().getPlayerStatus().getBag().getWeaponBag();
                        Integer num = equipmentInfo.getNum();
                        Weapon weapon = weaponBag.get(num);
                        System.out.println(weaponBag);
                        weaponBag.remove(weapon);
                        System.out.println(weapon);
                        playerInfoMap.put(equipmentInfo.getPlayerInfo().getNickName(),equipmentInfo.getPlayerInfo());
                        sendSuccessResponse(socketInfo,equipmentInfo);

                        break;
                    }
                    /* 装备防具 */
                    case 602: {
                        EquipmentInfo equipmentInfo = (EquipmentInfo) cmdMsg.getData();
                        Armor armor = equipmentInfo.getPlayerInfo().getPlayerStatus().getBag().getArmorBag().get(equipmentInfo.getNum());
                        if(armor.getUse()){
                            System.out.println("该防具已装备");
                        }
                        /**
                         * 当前装备武器
                         */
                        //更新勇者护甲
                        equipmentInfo.getPlayerInfo().getPlayerStatus().getArmor().setUse(false);
                        equipmentInfo.getPlayerInfo().getPlayerStatus().setArmor(armor);
                        armor.setUse(true);
                        //更新勇者血量
                        Integer baseHP= equipmentInfo.getPlayerInfo().getPlayerStatus().getBaseHP();
                        equipmentInfo.getPlayerInfo().getPlayerStatus().setMaxHP(baseHP+armor.getHealthBonus());
                        playerInfoMap.put(equipmentInfo.getPlayerInfo().getNickName(),equipmentInfo.getPlayerInfo());
                        sendSuccessResponse(socketInfo,equipmentInfo);

                        break;
                    }
                    /* 丢弃防具 */

                    case 603: {
                        EquipmentInfo equipmentInfo = (EquipmentInfo) cmdMsg.getData();
                        List<Armor> armorBag = equipmentInfo.getPlayerInfo().getPlayerStatus().getBag().getArmorBag();
                        Integer num = equipmentInfo.getNum();
                        Armor armor = armorBag.get(num);
                        System.out.println(armorBag);
                        armorBag.remove(armor);
                        System.out.println(armorBag);
                        playerInfoMap.put(equipmentInfo.getPlayerInfo().getNickName(),equipmentInfo.getPlayerInfo());
                        sendSuccessResponse(socketInfo,equipmentInfo);

                        break;
                    }
                    /* 使用药水 */
                    case 604: {
                        //获取客户端命令
                        EquipmentInfo equipmentInfo = (EquipmentInfo) cmdMsg.getData();

                        //获取具体信息
                        PlayerInfo playerInfo = equipmentInfo.getPlayerInfo();
                        Integer num = equipmentInfo.getNum();

                        //执行
                        System.out.println(playerInfo.getPlayerStatus());
                        EquipmentSystem.UsePotion(equipmentInfo);
                        System.out.println(playerInfo.getPlayerStatus());

                        //返回数据
                        playerInfoMap.put(equipmentInfo.getPlayerInfo().getNickName(),equipmentInfo.getPlayerInfo());
                        sendSuccessResponse(socketInfo, equipmentInfo);
                        break;
                    }
                    /* 丢弃药水 */
                    case 605: {
                        EquipmentInfo equipmentInfo = (EquipmentInfo) cmdMsg.getData();
                        List<Potion> potionBag = equipmentInfo.getPlayerInfo().getPlayerStatus().getBag().getPotionBag();
                        Integer num = equipmentInfo.getNum();
                        Potion potion = potionBag.get(num);
                        System.out.println(potionBag);
                        potionBag.remove(potion);
                        System.out.println(potionBag);
                        playerInfoMap.put(equipmentInfo.getPlayerInfo().getNickName(),equipmentInfo.getPlayerInfo());
                        sendSuccessResponse(socketInfo,equipmentInfo);

                        break;
                    }
                    /* 返回背包 */
                    case 606: {
                        break;
                    }
                    /* 生成怪物 */
                    case 888:{
                        if(cmdMsg.getData() instanceof Integer){
                            List<Monster> monsters = MonsterFactory.createMonsters((Integer) cmdMsg.getData());
                            sendSuccessResponse(socketInfo,monsters);
                        }else {
                            PlayerInfo playerInfo = (PlayerInfo) cmdMsg.getData();
                            List<Monster> monsters = MonsterFactory.createMonsters(playerInfo.getPlayerStatus().getCurrentLevel());
                            sendSuccessResponse(socketInfo, monsters);
                            playerInfoMap.put(playerInfo.getNickName(),playerInfo);
                        }
                    }
                        break;
                }
                mainOos = new ObjectOutputStream(new FileOutputStream(file));
                mainOos.writeObject(playerInfoMap);
                mainOos.flush();
                mainOos.reset();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void sendSuccessResponse(SocketInfo socketInfo, Object data) throws IOException {
        CmdMsg<Object> response = new CmdMsg<>(CommandParameters.SUCCESS_CODE, data);
        socketInfo.getOos().writeObject(response);
        socketInfo.getOos().flush();
        socketInfo.getOos().reset();
    }
    public static void sendFailResponse(SocketInfo socketInfo, Object data) throws IOException {
        CmdMsg<Object> response = new CmdMsg<>(CommandParameters.FAIL_CODE, data);
        socketInfo.getOos().writeObject(response);
        socketInfo.getOos().flush();
        socketInfo.getOos().reset();
    }
}



