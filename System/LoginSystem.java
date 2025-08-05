package com.sc.month2.StageProject.System;

import com.sc.month2.StageProject.Entity.pojo.info.LoginInfo;
import com.sc.month2.StageProject.Entity.pojo.player.PlayerInfo;
import com.sc.month2.StageProject.Entity.pojo.player.SocketInfo;
import com.sc.month2.StageProject.Socket.GameServerSocket;
import com.sc.month2.StageProject.Socket.PlayerSocket;
import com.sc.month2.StageProject.constant.CmdMsg;
import com.sc.month2.StageProject.constant.CommandParameters;
import com.sc.month2.StageProject.util.ConsoleUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.util.Map;
import java.util.Scanner;

public class LoginSystem implements Serializable {
    static Scanner input = new Scanner(System.in);

    SocketInfo socketInfo;



    public void setSocketInfo(ObjectInputStream ois,ObjectOutputStream oos) {
        this.socketInfo.setOis(ois);
        this.socketInfo.setOos(oos);
    }

    //玩家列表
    static Map<String,PlayerInfo> playerInfoMap;
    //当前玩家
    PlayerInfo playerInfo;

    public LoginSystem(Map<String,PlayerInfo> playerInfoMap, ServerSocket serverSocket,PlayerInfo playerInfo){
        this.playerInfoMap = playerInfoMap;

        this.playerInfo = playerInfo;
    }

    public LoginSystem(Map<String,PlayerInfo> playerInfoMap, ServerSocket serverSocket){
        this.playerInfoMap = playerInfoMap;

    }

    /**
     * 登录目录
     */
    public static void loginMenu(SocketInfo socketInfo,String ip) throws Exception {
        while (true){
            System.out.println(
                    "\n" +
                            "╔════════════════════════════════════════════════════════════════╗\n" +
                            "║ ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★ ║\n" +
                            "║ ★                                                           ★ ║\n" +
                            "║ ★    ██████╗ ██╗   ██╗██████╗ ██╗  ██╗███████╗██████╗       ★ ║\n" +
                            "║ ★    ██╔══██╗██║   ██║██╔══██╗██║ ██╔╝██╔════╝██╔══██╗      ★ ║\n" +
                            "║ ★    ██████╔╝██║   ██║██████╔╝█████╔╝ █████╗  ██████╔╝      ★ ║\n" +
                            "║ ★    ██╔══██╗██║   ██║██╔══██╗██╔═██╗ ██╔══╝  ██╔══██╗      ★ ║\n" +
                            "║ ★    ██║  ██║╚██████╔╝██║  ██║██║  ██╗███████╗██║  ██║      ★ ║\n" +
                            "║ ★    ╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝      ★ ║\n" +
                            "║ ★                                                           ★ ║\n" +
                            "║ ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★ ║\n" +
                            "╚════════════════════════════════════════════════════════════════╝\n" +
                            "            >> 【骷髅打金服】上线就送VIP15，装备秒回收！ <<\n" +
                            "            >> 是兄弟就来砍我！一刀爆神装，在线发工资！ <<"
            );


            PlayerInfo playerInfo = null;
            if (playerInfo == null){
                playerInfo = new PlayerInfo(socketInfo.getOos(),ip,socketInfo.getOis());
            }
            System.out.print(
                    "\n" +
                            ">> 开局只靠双拳，打出一片天\n" +
                            "[1] 老铁登录\n" +
                            "[2] 新人注册\n" +
                            "[3] 怂了溜了\n" +
                            "\n" +
                            "兄弟，手速选择（1/2/3）："
            );
            if (!input.hasNextInt()) {
                System.out.println("错误：请输入数字！");
                input.next(); // 清除无效输入
                continue;
            }
            int opNum = input.nextInt();
            input.nextLine(); // 清除换行符
            switch (opNum){
                case 1: {
                    System.out.println("请输入用户名(昵称)：");
                    String name = input.next();
                    System.out.println("请输入密码：");
                    String password = input.next();
                    LoginInfo loginInfo = new LoginInfo(name,password);
                    CmdMsg respMsg = PlayerSocket.sendMsgToServer(CommandParameters.LOGIN,socketInfo,loginInfo);
                    if(respMsg.getCmd().equals(CommandParameters.SUCCESS_CODE)){


                        System.out.println("登录成功。。。等待系统响应");
                        System.out.println("正在清理控制台。。。。。");
                        playerInfo = (PlayerInfo) respMsg.getData();
                        Thread.sleep(1000);
                        ConsoleUtils.clearConsole();
                        GameEntryMenu(playerInfo,socketInfo);
                    }else {
                        System.out.println("登录失败，请检查是否输入错误，并重新输入");
                        break;
                    }
                    break;
                }
                case 2: {
                    //Todo
                    register(playerInfo);


                    break;
                }
                case 3:{
                    System.out.println("退出游戏");
                    return ;
                }
            }
        }
    }
    public static PlayerInfo login(Map<String, PlayerInfo> playerInfoMap,
                             SocketInfo socketInfo,
                             LoginInfo loginInfo) {
        try {


            // 1. 参数校验
            if (loginInfo == null || loginInfo.getNickName() == null) {
                GameServerSocket.sendFailResponse(socketInfo, "登录信息不完整");
                return null;
            }

            // 2. 检查用户名是否存在
            if (!playerInfoMap.containsKey(loginInfo.getNickName())) {
                GameServerSocket.sendFailResponse(socketInfo, "用户名不存在");
                return null;
            }

            // 3. 获取玩家信息
            PlayerInfo storedInfo = playerInfoMap.get(loginInfo.getNickName());

            // 4. 验证密码
            if (!isPasswordValid(storedInfo, loginInfo)) {
                GameServerSocket.sendFailResponse(socketInfo, "账号或密码错误");
                return null;
            }


            return storedInfo;
            // 5. 登录成功
        } catch (IOException e) {
            System.err.println("登录过程发生IO异常");
            throw new RuntimeException("系统错误，请稍后再试", e);
        }

    }

    // 密码验证逻辑抽离
    private static boolean isPasswordValid(PlayerInfo storedInfo, LoginInfo loginInfo) {
        return storedInfo != null
                && storedInfo.getPassword() != null
                && storedInfo.getPassword().equals(loginInfo.getPassword());
    }



    public static void register(PlayerInfo playerInfo) {

        System.out.println("请输入用户名(昵称)：");
        String name = input.next();
        System.out.println("请输入密码：");
        String password = input.next();
        try {

            playerInfo.setNickName(name);
            playerInfo.setPassword(password);


            CmdMsg<PlayerInfo> cmdMsg = new CmdMsg<>(CommandParameters.REGISTER,playerInfo);

            playerInfo.getOos().writeObject(cmdMsg);
            playerInfo.getOos().flush();

            CmdMsg respMsg = (CmdMsg) playerInfo.getOis().readObject();
            if(respMsg.getCmd().equals(CommandParameters.SUCCESS_CODE)){
                System.out.println("注册成功");
                Thread.sleep(1000);
                ConsoleUtils.clearConsole();
            }else {
                System.out.println(respMsg.getData());
                Thread.sleep(1000);
                ConsoleUtils.clearConsole();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void GameEntryMenu(PlayerInfo playerInfo,SocketInfo socketInfo) throws InterruptedException {

        while (true) {
            try {
                // 1. 显示菜单选项
                System.out.println("请选择操作：");
                System.out.println("1. 新的开始");
                System.out.println("2. 读取存档");
                System.out.println("3. 退出");
                System.out.print("请输入选项(1-3): ");

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
                    case 1: {
                        CmdMsg respMsg = PlayerSocket.sendMsgToServer(CommandParameters.NEW_GAME,socketInfo,playerInfo);
                        playerInfo = (PlayerInfo) respMsg.getData();
                        if(respMsg.getCmd().equals(CommandParameters.SUCCESS_CODE)){
                            System.out.println("正在初始化角色数据。。。。");
                            Thread.sleep(800);
                            System.out.println("角色加载成功。。。\n" + respMsg.getData());
                            Thread.sleep(500);
                            System.out.println("等待服务器响应。。。。。");
                            System.out.println("正在清理控制台。。。。。");
                            ConsoleUtils.clearConsole();
                            System.out.println("————听同村李叔说————");
                            Thread.sleep(1000);
                            System.out.println("这个骷髅打金区域爆率贼高。。");
                            Thread.sleep(1000);
                            System.out.println("四处碰壁的你选择来这边碰碰运气......(按任意键继续......)");
                            input.next();

                            playerInfo = BattleSystem.GameMainMenu(playerInfo,socketInfo);
                        }else {
                            System.out.println(respMsg.getData());
                        }
                        break;
                    }
                    case 2: {
                        CmdMsg respMsg = PlayerSocket.sendMsgToServer(CommandParameters.LOAD_GAME,socketInfo,playerInfo);

                        if(respMsg.getCmd().equals(CommandParameters.SUCCESS_CODE)){
                            System.out.println("正在加载角色数据。。。。");
                            Thread.sleep(800);
                            System.out.println("角色加载成功。。。\n" + respMsg.getData());
                            Thread.sleep(200);
                            System.out.println("等待服务器响应。。。。。");
                            Thread.sleep(500);
                            ConsoleUtils.clearConsole();
                            playerInfo = BattleSystem.GameMainMenu(playerInfo,socketInfo);
                        }else {
                            System.out.println(respMsg.getData());
                        }
                        break;
                    }
                    case 3: {
                        System.out.println("返回上一级");
                        return;
                    }
                    default:
                        System.out.println("错误：无效选项，请重新输入！");
                }

            } catch (Exception e) {
                System.out.println("发生错误: " + e);
                input.nextLine(); // 清除错误输入
                return;
            }
        }
    }




}
