package com.zizhizhan.games.tank.image;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;

import com.zizhizhan.games.Component;
import com.zizhizhan.games.Direction;
import com.zizhizhan.games.tank.Explode;
import com.zizhizhan.games.tank.Missile;

import static com.zizhizhan.games.tank.TankConstant.*;


public class ImageMissile extends Missile {	
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();	
	private static Map<Direction, Image> images = new HashMap<Direction, Image>();
	

	static{
		for(Direction d : Direction.values()){
			if(d != Direction.STOP){
				Image img = tk.getImage(ImageTank.class.getClassLoader().getResource("tank_images/missile_" +
						d.toString() + ".gif"));
				images.put(d, img);
			}
		}			
	}
	
	
	public ImageMissile(int x, int y, Direction dir, Component parent) {
		this.x = x;
		this.y = y;
		this.width = MISSILE_WIDTH;
		this.height = MISSILE_HEIGHT;
		this.dir = dir;		
		this.parent = parent;
		this.team = parent.getTeam();
	}
	
	protected void drawComposite(Graphics g) {		
		super.drawComposite(g);
		
		g.drawImage(images.get(dir), x, y, null);
	}

	@Override
	protected Explode createExplode() {		
		return new ImageExplode(x, y, parent);
	}

}
