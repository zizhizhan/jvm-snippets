package com.zizhizhan.games;

import java.awt.Graphics;
import java.awt.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Composite implements Component {
		
	protected List<Component> components;
	protected int x, y, width, height;	
	protected AtomicBoolean alive;
	protected Scenario scenario;
	protected Team team;
	
		
	public Composite(){
		this(new ArrayList<Component>());	
	}
	
	public Composite(List<Component> components) {		
		this.components = components;
		this.alive = new AtomicBoolean(true);
		this.team = Team.ROBOT;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public boolean add(Component c) {		
		return components.add(c);
	}
	
	public boolean remove(Component c) {
		return components.remove(c);
	}
	
	public Scenario getScenario() {
		return scenario;
	}

	public void setScenario(Scenario scenario) {
		this.scenario = scenario;
	}

	public boolean isAlive() {
		return alive.get();
	}

	public void setAlive(boolean alive) {
		this.alive.set(alive);
	}

	public Rectangle getScope(){
		return new Rectangle(x, y, width, height);
	}	
		
	public List<Component> getComposites() {
		return components;		
	}
	
	public void draw(Graphics g) {			
		for(int i = 0; i < components.size(); i++){
			Component c = components.get(i);
			c.draw(g);			
		}	
	}	
	
	
}
