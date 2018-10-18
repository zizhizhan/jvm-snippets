package me.jameszhan.pattern.reactor.nio.main;

import me.jameszhan.pattern.reactor.nio.core.*;

import java.io.IOException;
import java.nio.ByteBuffer;

import me.jameszhan.pattern.reactor.nio.core.DatagramPacket;


/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/17
 * Time: 下午9:15
 */
public class ReactorMain {

    public static void main(String[] args) throws IOException {
        SessionHandler<ByteBuffer> byteBufferSessionHandler = new LoggingHandler<ByteBuffer>() {
            @Override
            String decode(ByteBuffer buffer) {
                return new String(buffer.array(), 0, buffer.limit(), ISO8859_1);
            }
            @Override
            ByteBuffer encode(String response, ByteBuffer request) {
                return ByteBuffer.wrap(response.getBytes(ISO8859_1));
            }
        };
        SessionHandler<DatagramPacket> datagramPacketSessionHandler = new LoggingHandler<DatagramPacket>() {
            @Override
            String decode(DatagramPacket packet) {
                return new String(packet.buf.array(), 0, packet.buf.limit(), ISO8859_1);
            }

            @Override
            DatagramPacket encode(String response, DatagramPacket request) {
                return new DatagramPacket(request.address, ByteBuffer.wrap(response.getBytes(ISO8859_1)));
            }
        };

        Reactor reactor = new Reactor();
        reactor.register(new TcpChannel(8886, byteBufferSessionHandler))
                .register(new TcpChannel(8887, byteBufferSessionHandler))
                .register(new UdpChannel(8888, datagramPacketSessionHandler))
                .register(new UdpChannel(8889, datagramPacketSessionHandler))
                .start();
    }

}
