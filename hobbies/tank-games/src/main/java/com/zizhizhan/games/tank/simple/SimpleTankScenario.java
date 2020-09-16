package com.zizhizhan.games.tank.simple;

import com.zizhizhan.games.Team;
import com.zizhizhan.games.tank.Blood;
import com.zizhizhan.games.tank.Tank;
import com.zizhizhan.games.tank.TankScenario;
import com.zizhizhan.games.tank.Wall;


public class SimpleTankScenario extends TankScenario {
	

	public SimpleTankScenario(Tank myTank) {
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
			add(new SimpleTank(200, 50, Team.ROBOT));
			add(new SimpleTank(50, 500, Team.ROBOT));
			add(new SimpleTank(600, 450, Team.ROBOT));
			add(new SimpleTank(600, 100, Team.ROBOT));
			add(new SimpleTank(360, 360, Team.ROBOT));
		}
		if (mediator.bloodCount.get() < 2 && myTank.getHealth() < 90) {
			add(new Blood());
		}
	}
	
	
	public static void main(String[] args) {
		SimpleTankScenario scene = new SimpleTankScenario(new SimpleTank(715,
				500, Team.PLAYER));
		scene.launch();
	}


}
