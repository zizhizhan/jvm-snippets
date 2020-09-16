package com.zizhizhan.games.tank.net;

public class TankAddedMessage extends TankMessage {
	
	private static final long serialVersionUID = 1L;
	
	public TankAddedMessage(int tankId) {
		super(tankId);
		
	}

}
