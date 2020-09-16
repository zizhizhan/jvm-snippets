package com.zizhizhan.games.tank;

import java.util.ArrayList;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.zizhizhan.games.Component;

public class CollisionMediator {
	
	private final static CollisionMediator singleton = new CollisionMediator();	
	public List<Tank> tanks = new ArrayList<Tank>();
	public AtomicInteger missileCount = new AtomicInteger(0);
	public AtomicInteger explodeCount = new AtomicInteger(0);
	public AtomicInteger tankCount = new AtomicInteger(0);
	public AtomicInteger bloodCount = new AtomicInteger(0);
	public AtomicInteger score = new AtomicInteger(0);
	
	
	TankScenario scenario;
	
	public static CollisionMediator getSingleton(){
		return singleton;
	}
	
	private CollisionMediator(){
		
	}

	
	public void collision(Missile m, List<Component> lists) {
		for (int k = 0; k < lists.size(); k++) {
			Component c = lists.get(k);
			if(isPremitAttack(m, c)){
				if (c instanceof Missile) {
					if (isCollided(m, c)) {
						Missile m2 = (Missile) c;
						m.hit(m2);
						if (m.getParent() == scenario.myTank
								|| m2.getParent() == scenario.myTank || m2.isAlive()) {
							score.getAndAdd(25);
						}
					}
				}
				if (c instanceof Tank) {
					collision(m, c.getComposites());
					if (isCollided(m, c)) {
						m.hit((Tank) c);
						if(m.getParent() == scenario.myTank){
							score.getAndAdd(50);
						}
					}				
				}
				if(c instanceof Wall){
					if (isCollided(m, c)) {
						m.hit((Wall) c);						
					}	
				}
			}		
		}
	}
	
	public void collision(Tank m) {
		for (int k = 0; k < scenario.getComposites().size(); k++) {
			Component c = scenario.getComposites().get(k);
			if(m != c && m.isAlive() && isCollided(m, c)){
				if(c instanceof Tank){
					m.collideTank((Tank)c);
				}
				if(c instanceof Wall){
					m.collideWall((Wall)c);
				}
				if(c instanceof Blood){
					m.eat((Blood)c);
					if(m == scenario.myTank){
						score.getAndAdd(100);
					}
				}
			}
		}
	}
	
	public void collision(Missile m) {
		collision(m, scenario.getComposites());
	}
	
	public void setScenario(TankScenario scenario) {
		this.scenario = scenario;
	}
	
	public boolean isPremitAttack(Missile m, Component c){
		return (m.isAlive() && m != c  && c != m.getParent());
	}
	
	public boolean isCollided(Component c1, Component c2){
		return (c1.getScope().intersects(c2.getScope()));
	}

	
	
}
