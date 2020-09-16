package com.zizhizhan.games.tank.net;

public class TankMessage implements Message {
	
	private static final long serialVersionUID = 1L;
	
	private int tankId;
	private int x;
	private int y;	
	
	public TankMessage(int tankId) {
		super();
		this.tankId = tankId;
	}
	
	public int getTankId() {
		return tankId;
	}
	public void setTankId(int tankId) {
		this.tankId = tankId;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	public void update(NetScenario scenario){
		
	}

}
