package com.zizhizhan.games.tank.net.server;

import lombok.extern.slf4j.Slf4j;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class TankServer {
	
	private final int tcpPort;
	private final int udpPort;
	
	private final List<ClientInfo> clients = new ArrayList<>();
	private final AtomicInteger tankIdGen = new AtomicInteger(1000);
		
	private final Executor exec;
	
	public TankServer(){
		ResourceBundle bundle = ResourceBundle.getBundle("server");
		tcpPort = Integer.parseInt(bundle.getString("server.tcp.port"));
		udpPort = Integer.parseInt(bundle.getString("server.udp.port"));
		int threads = Integer.parseInt(bundle.getString("server.threads"));
		
		exec = Executors.newFixedThreadPool(threads);		
	}
	
	public void startup(){
		Thread t = new Thread(new Broadcast());
		t.setDaemon(true);
		t.start();
		
		ServerSocket server;
		try {
			server = new ServerSocket(tcpPort);
			while(true){			
				Socket socket = server.accept();
				exec.execute(new Processor(socket));			
			}
		} catch (IOException ex) {			
			log.error(ex.getMessage());
		}		
	}
	
	private class Processor implements Runnable{
		public Socket socket;
		
		Processor(Socket socket){
			this.socket = socket;
		}

		@Override
		public void run() {
			int tankId = tankIdGen.incrementAndGet();
			try {
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
				dos.writeInt(tankId);
				
				log.debug("New tank connected id: {}.", tankId);
				
				DataInputStream dis = new DataInputStream(socket.getInputStream());
				clients.add(new ClientInfo(socket.getInetAddress().getHostAddress(), dis.readInt()));				
				
			} catch (IOException e) {				
				log.error("New Tank Coming in error:", e);
			}finally{
				try {
					socket.close();
				} catch (IOException e) {
					log.warn("socket not close as expected!", e);
				}
			}		
		}		
	}
	
	private class Broadcast implements Runnable{
		private DatagramSocket ds;
		
		public Broadcast(){
			try {
				ds = new DatagramSocket(udpPort);
				log.info("UDP thread started at port: {}.", udpPort);
			} catch (SocketException ex) {
				log.error("Broadcast start failure!", ex);
			}
		}

		@Override
		public void run() {
			byte[] buf = new byte[8000];
			while(ds != null){
				DatagramPacket dp = new DatagramPacket(buf, buf.length);
				try {
					ds.receive(dp);
					for (int i = 0; i < clients.size(); i++) {
						ClientInfo c = clients.get(i);
						dp.setSocketAddress(new InetSocketAddress(c.ip, c.port));
						ds.send(dp);
					}
					log.info("a packet received!");
				} catch (IOException e) {
					log.error("Can't send the message!", e);
				}			
			}		
		}
		
	}
	
	private class ClientInfo{
		String ip;
		int port;
		public ClientInfo(String ip, int port) {			
			this.ip = ip;
			this.port = port;
		}		
	}
	
	public static void main(String[] args) {
		TankServer server = new TankServer();
		server.startup();
	}

}
