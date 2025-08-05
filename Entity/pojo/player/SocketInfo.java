package com.sc.month2.StageProject.Entity.pojo.player;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SocketInfo {
    protected transient ObjectOutputStream oos;
    protected transient ObjectInputStream ois;




    public ObjectInputStream getOis() {
        return ois;
    }

    public void setOis(ObjectInputStream ois) {
        this.ois = ois;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public void setOos(ObjectOutputStream oos) {
        this.oos = oos;
    }

    public SocketInfo(ObjectOutputStream oos, ObjectInputStream ois) {
        this.oos = oos;
        this.ois = ois;
    }
}
