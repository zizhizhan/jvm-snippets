package com.zizhizhan.games.tank.net.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.ResourceBundle;

import com.zizhizhan.games.tank.Tank;
import com.zizhizhan.games.tank.net.TankMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TankClient {

	private String serverName;
	private int serverUdp;
	
	//private TankScenario scenario;
	private Tank tank;
	
	private DatagramSocket ds = null;
	private int udpPort;
	
	public TankClient(){
				
	}
	
	public void connect() throws UnknownHostException, IOException{
		if(isListening()){
			
			ResourceBundle bundle = ResourceBundle.getBundle("server");
			int tcp = Integer.parseInt(bundle.getString("server.tcp.port"));
			serverUdp = Integer.parseInt(bundle.getString("server.udp.port"));
			serverName = bundle.getString("server.host");
			log.info("Server info {\n\t"
					+ "host: " + serverName + "\n\t" 
					+ "udp: " + serverUdp + "\n\t"
					+ "tcp: " + tcp + "\n"
					+ "}");

			try (Socket s = new Socket(serverName, tcp)) {
				DataInputStream dis = new DataInputStream(s.getInputStream());
				tank.setId(dis.readInt());
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());
				dos.writeInt(udpPort);
			}
			
			TankMessage msg = new TankMessage(tank.getId());
			send(msg);
						
			new ListeningThread().start();
		}
		
	}
	
	public void send(TankMessage msg) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(msg);
			byte[] buf = baos.toByteArray();
			DatagramPacket dp = new DatagramPacket(buf, buf.length, new InetSocketAddress(serverName, serverUdp));
			ds.send(dp);			
		} catch (IOException e) {
			log.error("send message failure!", e);
		}
	}
	
	public boolean isListening() {
		boolean success = false;
		Random rand = new Random(System.currentTimeMillis());	
		 
		do {
			udpPort = rand.nextInt(20000) + 2000;
			try {
				ds = new DatagramSocket(udpPort);
				log.info("Client start successful, udp port: " + udpPort);
				success = true;
				break;
			} catch (BindException e) {
				log.warn("port " + udpPort + " have been used, try the next one", e);
			} catch (SocketException e) {
				log.error("can't start the udp socket", e);
				break;
			}
		} while (true);	
		return success;
	}
	
	private class ListeningThread extends Thread{

		@Override
		public void run() {
			byte[] buf = new byte[8000];
			while(ds != null){
				DatagramPacket dp = new DatagramPacket(buf, buf.length);
				try {
					ds.receive(dp);
					ByteArrayInputStream bais = new ByteArrayInputStream(buf, 0, dp.getLength());
					ObjectInputStream ois = new ObjectInputStream(bais);
					update((TankMessage)ois.readObject());
				} catch (IOException e) {
					log.warn("update message info failure!", e);
				} catch (ClassNotFoundException e) {
					log.error("can't resolve the message.", e);
				}				
			}			
		}

		private void update(TankMessage msg) {
			log.info("update tankMessage {}.", msg);
		}		
	}

}
