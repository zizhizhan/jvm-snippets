package com.zizhizhan.legacies.io.net;

import java.net.InetAddress;

public class FQDN {

    public static void main(String[] args) throws Exception {
        InetAddress localAddr = InetAddress.getLocalHost();
        System.out.println("LocalHost: ");
        System.out.println("本机名: " + localAddr.getHostName());
        System.out.println("CanonicalHostName:" + localAddr.getCanonicalHostName());
        System.out.println("Addr: " + localAddr.getHostAddress());
        System.out.println("======================\n\n\n");

        String[] ipOrNames = {
                "krb5.zizhizhan.com",
                "192.168.1.100",
                "kafka.zizhizhan.com",
                "192.168.1.163",
                "192.168.1.3",
                "192.168.1.6"
        };

        for (String ipOrName : ipOrNames) {
            InetAddress addr = InetAddress.getByName(ipOrName);
            System.out.println(ipOrName);
            System.out.println("\tHostName:  " + addr.getHostName());
            System.out.println("\tCanonicalHostName:  " + addr.getCanonicalHostName());
            System.out.println("\tIP:  " + addr.getHostAddress());
        }
    }

}