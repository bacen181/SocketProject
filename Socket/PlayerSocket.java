package com.sc.month2.StageProject.Socket;

import com.sc.month2.StageProject.Entity.pojo.player.SocketInfo;
import com.sc.month2.StageProject.System.LoginSystem;
import com.sc.month2.StageProject.constant.CmdMsg;
import com.sc.month2.StageProject.constant.CommandParameters;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;


public class PlayerSocket implements Serializable {
    private static final int SERVER_PORT = 8888;

    public static void main(String[] args) {

        try {
            Socket socket = new Socket("localhost",SERVER_PORT);
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            SocketInfo socketInfo = new SocketInfo(oos,ois);
            System.out.println("正在连接服务器。。。。");


            Thread.sleep(1500);
            LoginSystem.loginMenu(socketInfo,socket.getInetAddress().getHostAddress());



        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param commandParameter  命令
     * @param data  用户数据
     * @return  服务端返回的命令
     * @throws Exception
     */
    public static CmdMsg sendMsgToServer(CommandParameters commandParameter, SocketInfo socketInfo, Object data) throws Exception {
        CmdMsg cmdMsg = new CmdMsg(commandParameter, data);

        socketInfo.getOos().writeObject(cmdMsg);
        socketInfo.getOos().reset();
        socketInfo.getOos().flush();

       CmdMsg respMsg = (CmdMsg) socketInfo.getOis().readObject();
        return respMsg;
    }
    public static CmdMsg getMsgFromServer( SocketInfo socketInfo) throws Exception {

        CmdMsg respMsg = (CmdMsg) socketInfo.getOis().readObject();
        return respMsg;
    }




}
