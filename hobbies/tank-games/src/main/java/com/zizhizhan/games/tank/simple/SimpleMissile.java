package com.zizhizhan.games.tank.simple;

import java.awt.Color;
import java.awt.Graphics;

import com.zizhizhan.games.Component;
import com.zizhizhan.games.Direction;
import com.zizhizhan.games.tank.Explode;
import com.zizhizhan.games.tank.Missile;
import com.zizhizhan.games.tank.TankConstant;

public class SimpleMissile extends Missile {
	
	public SimpleMissile(int x, int y, Direction dir, Component parent) {
		this.x = x;
		this.y = y;
		this.width = TankConstant.MISSILE_WIDTH;
		this.height = TankConstant.MISSILE_HEIGHT;
		this.dir = dir;		
		this.parent = parent;
		this.team = parent.getTeam();
	}
	
	protected void drawComposite(Graphics g) {		
		super.drawComposite(g);
		
		Color c = g.getColor();
		switch (team) {
		case PLAYER:
			g.setColor(Color.GREEN);
			break;
		case BLUE:
			g.setColor(Color.BLUE);
			break;
		default:
			g.setColor(Color.RED);
			break;
		}
		
		g.fillOval(x, y, TankConstant.MISSILE_WIDTH, TankConstant.MISSILE_HEIGHT);
		g.setColor(c);
	}

	@Override
	protected Explode createExplode() {
		return new Explode(x, y, parent);
	}

	

	
	

}
