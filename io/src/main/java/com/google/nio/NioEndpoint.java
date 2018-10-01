package com.google.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioEndpoint {
	
	
	public void start() throws IOException{				
		
		Selector selector = Selector.open();
		
		ServerSocketChannel ssc = ServerSocketChannel.open();
		InetSocketAddress addr = new InetSocketAddress(InetAddress.getLocalHost(), 9000);
		ssc.socket().bind(addr);
		
		ssc.configureBlocking(false);
		
		SelectionKey k = ssc.register(selector, SelectionKey.OP_ACCEPT);
		printKeyInfo(k);
		
		for(;;){
			int op = selector.select();
			if(op > 0){
				Set<SelectionKey> keys = selector.selectedKeys();
				Iterator<SelectionKey> itor = keys.iterator();
				while(itor.hasNext()){
					k = itor.next();
					printKeyInfo(k);					
					if(k.isAcceptable()){
						Socket socket = ((ServerSocketChannel)k.channel()).accept().socket();
						SocketChannel sc = socket.getChannel();
						sc.configureBlocking(false);
						sc.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
					}
					
					
					itor.remove();
				}
			
					
			}
			
				
		}		
		
	}
	
	private static void printKeyInfo(SelectionKey sk){ 
		
		String s = new String(); 
		s = "Att: " + (sk.attachment() == null ? "no" : "yes"); 
		s += ", Read: " + sk.isReadable(); 
		s += ", Acpt: " + sk.isAcceptable();
		s += ", Cnct: " + sk.isConnectable();
		s += ", Wrt: " + sk.isWritable();
		s += ", Valid: " + sk.isValid();
		s += ", Ops: " + sk.interestOps();
		
		System.out.println(s);
	}

	
	
	public static void main(String[] args) throws IOException {
		NioEndpoint endpoint = new NioEndpoint();
		endpoint.start();
	}

}
