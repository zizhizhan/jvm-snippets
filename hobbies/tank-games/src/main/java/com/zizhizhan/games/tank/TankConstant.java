package com.zizhizhan.games.tank;

import java.util.Locale;
import java.util.ResourceBundle;

public abstract class TankConstant {	
	

	public final static int POLLING_INTERVAL = 100;	
	
	public final static int GAME_WIDTH = 800, GAME_HEIGHT = 600;
	public final static int TANK_WIDTH = 50, TANK_HEIGHT = 50;
	public static final int MISSILE_WIDTH = 12, MISSILE_HEIGHT = 12;
	
	public static final int TANK_XSPEED = 10, TANK_YSPEED = 10;
	public static final int MISSILE_XSPEED = 25, MISSILE_YSPEED = 25;
	
	static final String TITLE;
	static final String MISSILE_COUNT;
	static final String EXPLODE_COUNT;
	static final String TANK_COUNT;
	static final String TANK_HEALTH;
	static final String SCORE;
	
	
	static{
		ResourceBundle bundle = ResourceBundle.getBundle("tank", Locale.CHINESE);
		TITLE = bundle.getString("tank.scenario.title");
		MISSILE_COUNT = bundle.getString("tank.missile.count");
		EXPLODE_COUNT = bundle.getString("tank.explode.count");
		TANK_COUNT = bundle.getString("tank.tank.count");
		TANK_HEALTH = bundle.getString("tank.tank.health");
		SCORE = bundle.getString("tank.tank.score");		
		
	}
	

	private TankConstant() {

	}

}
