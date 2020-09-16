package com.zizhizhan.games.tank;

import java.awt.Color;
import java.awt.Graphics;

import com.zizhizhan.games.Component;
import com.zizhizhan.games.Composite;

public class Explode extends Composite{
	
	private int[] diameter = {1, 2, 4, 8, 16, 24, 18, 9, 6, 3};	
	private int superPing = 1;
	protected int step = 0;
	protected Component parent;
	
	public Explode(int x, int y, Component parent) {
		this.x = x;
		this.y = y;	
		this.parent = parent;
	}
	
	public Explode(int x, int y,  Component parent, int superPing) {	
		this(x, y, parent);
		this.superPing = superPing;		
	}
	
	public void draw(Graphics g){		
		if(!alive.get()) {	
			parent.remove(this);
			return;
		}
		
		if(step == diameter.length) {
			setAlive(false);
			step = 0;
			return;
		}
		
		Color c = g.getColor();
		g.setColor(Color.LIGHT_GRAY);
		g.fillOval(x, y, diameter[step], superPing * diameter[step]);
		g.setColor(c);
		
		step++;
	}


}
