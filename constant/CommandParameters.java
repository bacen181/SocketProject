package com.sc.month2.StageProject.constant;

import java.io.Serializable;
import java.util.List;

public enum CommandParameters implements Serializable {
    /* 一级菜单 */
    LOGIN(100, "登录"),
    REGISTER(101, "注册"),
    EXIT(102, "退出"),

    /* 二级菜单 */
    NEW_GAME(201, "新的开始"),
    LOAD_GAME(202, "读取存档"),
    EXIT_LEVEL2(203, "退出"),

    /* 三级菜单 */

    START_BATTLE(300, "战斗"),
    OPEN_BACKPACK(301, "打开背包"),
    SELECT_LEVEL(302, "选择层数"),
    SAVE_GAME(303, "存档"),
    GET_LEVEL_ALL(305,"获取排行榜"),
    BACK_TO_PREVIOUS(304, "返回上一级"),

    /* 战斗菜单 */
    ATTACK(400, "攻击"),
    OPEN_BACKPACK_IN_BATTLE(401, "打开背包"),
    ESCAPE(402, "逃跑"),

    /* 背包菜单 */
    OPEN_WEAPON(500, "武器"),
    OPEN_ARMOR(501, "防具"),
    OPEN_POTION(502, "药水"),
    BACK(503, "返回"),

    /* 物品操作 */
    EQUIP_WEAPON(600, "装备武器"),
    DISCARD_WEAPON(601, "丢弃武器"),
    EQUIP_ARMOR(602, "装备防具"),
    DISCARD_ARMOR(603, "丢弃防具"),
    USE_POTION(604, "使用药水"),
    DISCARD_POTION(605, "丢弃药水"),
    BACK_FROM_BACKPACK(606, "返回背包"),

    //响应成功
    SUCCESS_CODE(200,"成功"),
    FAIL_CODE(460,"失败"),

    //生成怪物
    CREATE_MONSTER(888,"生成怪物");

    private final int id;  // 命令编号
    private final String description;  // 命令描述
    private List<CommandParameters> commandParameters;

    CommandParameters(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据ID查找对应的枚举常量
     */
    public static CommandParameters fromId(int id) {
        for (CommandParameters cmd : values()) {
            if (cmd.id == id) {
                return cmd;
            }
        }
        throw new IllegalArgumentException("未知的命令ID: " + id);
    }

}