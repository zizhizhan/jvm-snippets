package com.zizhizhan.games.tank.image;

import com.zizhizhan.games.Team;
import com.zizhizhan.games.tank.Blood;
import com.zizhizhan.games.tank.Tank;
import com.zizhizhan.games.tank.TankScenario;
import com.zizhizhan.games.tank.Wall;

public class ImageTankScenario extends TankScenario {

	public ImageTankScenario(Tank myTank) {
		super();
		this.myTank = myTank;		
		add(myTank);		
		add(new Wall(675, 400, 30, 200));
		add(new Wall(520, 300, 285, 30));
		add(new Wall(520, 400, 155, 30));
		
		add(new Wall(200, 440, 30, 160));
		add(new Wall(0, 300, 240, 30));
		
		add(new Wall(360, 20, 40, 300));
		
	}

	protected void polling(){
		if (mediator.tankCount.get() < 2) {
			add(new ImageTank(50, 50, Team.ROBOT));
			add(new ImageTank(150, 50, Team.ROBOT));
			add(new ImageTank(250, 50, Team.ROBOT));
			add(new ImageTank(450, 50, Team.ROBOT));
			add(new ImageTank(550, 50, Team.ROBOT));
			add(new ImageTank(650, 50, Team.ROBOT));
			
			add(new ImageTank(50, 450, Team.ROBOT));
			add(new ImageTank(150, 450, Team.ROBOT));
			add(new ImageTank(250, 450, Team.ROBOT));
			add(new ImageTank(450, 450, Team.ROBOT));
			add(new ImageTank(550, 450, Team.ROBOT));		
		}
		if (mediator.bloodCount.get() < 2 && myTank.getHealth() < 90) {
			add(new Blood());
		}
	}

	public static void main(String[] args) {
		ImageTankScenario scene = new ImageTankScenario(new ImageTank(715,500, Team.PLAYER));
		scene.launch();
	}

}
