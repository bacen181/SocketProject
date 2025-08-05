package com.sc.month2.StageProject.constant;

import java.io.Serializable;

public class CmdMsg<T> implements Serializable {

    private CommandParameters cmd;
    private T data;

    public CmdMsg(CommandParameters cmd, T data) {
        this.cmd = cmd;
        this.data = data;
    }

    public CmdMsg(CommandParameters cmd) {
        this.cmd = cmd;
    }

    public CmdMsg() {
    }

    @Override
    public String toString() {
        return "CmdMsg{" +
                "cmd=" + cmd +
                ", data=" + data +
                '}';
    }

    public CommandParameters getCmd() {
        return cmd;
    }

    public void setCmd(CommandParameters cmd) {
        this.cmd = cmd;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


}
