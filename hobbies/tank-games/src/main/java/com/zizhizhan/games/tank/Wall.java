package com.zizhizhan.games.tank;

import java.awt.Color;
import java.awt.Graphics;

public class Wall extends TankComposite{	

	public Wall(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;		
	}
		
	public void draw(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.DARK_GRAY);
		g.fillRect(x, y, width, height);
		g.setColor(c);
	}

}
