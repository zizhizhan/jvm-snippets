package com.zizhizhan.games.tank.image;


import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;


import com.zizhizhan.games.Component;
import com.zizhizhan.games.tank.Explode;

public class ImageExplode extends Explode {
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();	
	private static Image[] images = new Image[11];
	
	private int step;
	

	static{
		for(int i = 0; i <= 10; i++){
			images[i] = tk.getImage(ImageTank.class.getClassLoader().getResource("tank_images/" + i + ".gif"));
		}			
	}
	

	public ImageExplode(int x, int y, Component parent) {
		super(x, y, parent);		
	}
	
	
	public void draw(Graphics g){		
		if(!alive.get()) {	
			parent.remove(this);
			return;
		}
		
		if(step == images.length) {
			setAlive(false);
			step = 0;
			return;
		}
		
		g.drawImage(images[step], x, y, null);
		
		step++;
	}


	
	
	

}
