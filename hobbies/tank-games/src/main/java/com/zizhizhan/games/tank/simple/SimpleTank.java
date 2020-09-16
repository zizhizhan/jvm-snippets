package com.zizhizhan.games.tank.simple;

import java.awt.Color;
import java.awt.Graphics;

import com.zizhizhan.games.Direction;
import com.zizhizhan.games.Team;
import com.zizhizhan.games.tank.Missile;
import com.zizhizhan.games.tank.Tank;
import com.zizhizhan.games.tank.TankConstant;

public class SimpleTank extends Tank {

	public SimpleTank() {		
		this(360, 360, Team.ROBOT);
	}
	
	public SimpleTank(int x, int y, Team team) {		
		this.x = x;
		this.y = y;
		this.team = team;
		this.width = TankConstant.TANK_WIDTH;
		this.height = TankConstant.TANK_HEIGHT;
	}
	
	
	public void drawComposite(Graphics g) {
		super.drawComposite(g);
		
		Color c = g.getColor();
		if(team == Team.ROBOT){
			g.setColor(Color.RED);
		}else{
			g.setColor(Color.LIGHT_GRAY);
		}
		g.fillOval(x, y, TankConstant.TANK_WIDTH, TankConstant.TANK_HEIGHT);
		g.setColor(c);
		drawBarrel(g);
		drawBlood(g);
		
	}
	
	private void drawBlood(Graphics g){
		Color c = g.getColor();
		g.setColor(Color.MAGENTA);
		g.drawRect(x, y - 10, TankConstant.TANK_WIDTH, 5);
		int w = TankConstant.TANK_WIDTH * getHealth()/100 ;
		g.fillRect(x, y - 10, w, 5);
		g.setColor(c);
	}
		
	private void drawBarrel(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.decode("0xFFFF00"));

		int startX = x + TankConstant.TANK_WIDTH / 2;
		int startY = y + TankConstant.TANK_HEIGHT / 2;
		int endX = startX;
		int endY = startY;

		int delta = 4;

		switch (launchDirection) {
		case TOP:
			endY = y - delta;
			break;
		case RIGHT:
			endX = x + TankConstant.TANK_WIDTH + delta;
			break;
		case BOTTOM:
			endY = y + TankConstant.TANK_HEIGHT + delta;
			break;
		case LEFT:
			endX = x - delta;
			break;
		case TOP_RIGHT:
			endX = x + TankConstant.TANK_WIDTH;
			endY = y;
			break;
		case TOP_LEFT:
			endX = x;
			endY = y;
			break;
		case BOTTOM_RIGHT:
			endX = x + TankConstant.TANK_WIDTH;
			endY = y + TankConstant.TANK_HEIGHT;
			break;
		case BOTTOM_LEFT:
			endX = x;
			endY = y + TankConstant.TANK_HEIGHT;
			break;
		}

		g.drawLine(startX, startY, endX, endY);
		g.setColor(c);
	}
	
	@Override
	protected Missile createMissile(Direction d) {
		return new SimpleMissile(x + TankConstant.TANK_WIDTH / 2 - TankConstant.MISSILE_WIDTH / 2, y + TankConstant.TANK_HEIGHT / 2
				- TankConstant.MISSILE_HEIGHT / 2, d, this);
	}

}
