package com.google.nio;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Test {

    public static void main(String[] args) {

        SocketAcceptor acceptor = new SocketAcceptor();
        try {
            acceptor.bind(new InetSocketAddress[]{
                    new InetSocketAddress("localhost", 8555),
                    new InetSocketAddress("localhost", 8556)
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
