package com.zizhizhan.games.tank.image;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;

import com.zizhizhan.games.Direction;
import com.zizhizhan.games.Team;
import com.zizhizhan.games.tank.Missile;
import com.zizhizhan.games.tank.Tank;

import static com.zizhizhan.games.tank.TankConstant.*;

public class ImageTank extends Tank {

	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Map<Direction, Image> images = new HashMap<Direction, Image>();
	
	static{
		for(Direction d : Direction.values()){
			if(d != Direction.STOP){
				images.put(d, tk.getImage(ImageTank.class.getClassLoader().getResource("tank_images/tank_" +
						d.toString() + ".gif")));
			}
		}			
	}
	
	public ImageTank(int x, int y, Team team) {		
		this.x = x;
		this.y = y;
		this.team = team;
		this.width = TANK_WIDTH;
		this.height = TANK_HEIGHT;			
	}
	
	
	public void drawComposite(Graphics g) {
		super.drawComposite(g);
		g.drawImage(images.get(launchDirection), x, y, null);
	}
	
	@Override
	protected Missile createMissile(Direction d) {
		Missile m = new ImageMissile(x + TANK_WIDTH / 2 - MISSILE_WIDTH / 2, y + TANK_HEIGHT / 2
				- MISSILE_HEIGHT / 2, d, this);			
		return m;
	}

}
