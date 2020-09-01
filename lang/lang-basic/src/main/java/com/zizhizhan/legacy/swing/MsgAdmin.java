package com.zizhizhan.legacy.swing;

import java.net.*;
import java.util.*;

public class MsgAdmin {

    private DatagramSocket mysocket;

    public MsgAdmin() {
        try {
            this.mysocket = new DatagramSocket(1236);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap<String, String> recevice() {
        HashMap<String, String> hm = new HashMap<String, String>();
        try {
            byte[] buf = new byte[1024];
            DatagramPacket dp = new DatagramPacket(buf, 1024);
            mysocket.receive(dp);
            String str = new String(buf, 0, dp.getLength());
            hm.put("content", str);
            hm.put("ip", dp.getAddress().toString().substring(1, dp.getAddress().toString().length()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return hm;
    }

    public void send(String msg) {

        try {
            DatagramPacket dp = new DatagramPacket(msg.getBytes(), msg.getBytes().length,
                    InetAddress.getByName("127.0.0.1"), 1236);
            mysocket.send(dp);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
