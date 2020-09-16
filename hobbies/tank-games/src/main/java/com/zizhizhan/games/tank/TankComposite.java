package com.zizhizhan.games.tank;

import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;


import com.zizhizhan.games.Component;
import com.zizhizhan.games.Composite;

public abstract class TankComposite extends Composite {
	
	public TankComposite() {
		super(new LinkedList<Component>());
	}

	public TankComposite(List<Component> components) {
		super(components);	
	}
	
	protected void drawComposite(Graphics g){
		super.draw(g);	
	}	
	
	@Override
	public void draw(Graphics g) {
		preDraw();		
			
		drawComposite(g);
		
		postDraw();
	}
	
	
	protected void postDraw() {
	
	}

	protected void preDraw() {	
		
	}

	@Override
	public boolean add(Component c) {
		boolean success = super.add(c);
		if(success){
			if(c instanceof Missile){
				CollisionMediator.getSingleton().missileCount.incrementAndGet();
				return true;
			}else if(c instanceof Explode){
				CollisionMediator.getSingleton().explodeCount.incrementAndGet();
			}else if(c instanceof Tank){
				CollisionMediator.getSingleton().tankCount.incrementAndGet();
			}else if(c instanceof Blood){
				CollisionMediator.getSingleton().bloodCount.incrementAndGet();
			}	
		}		
		return success;			
	}

	@Override
	public boolean remove(Component c) {
		boolean success = super.remove(c);
		if(success){
			if(c instanceof Missile){
				CollisionMediator.getSingleton().missileCount.decrementAndGet();
				return true;
			} else if(c instanceof Explode){
				CollisionMediator.getSingleton().explodeCount.decrementAndGet();
			}else if(c instanceof Tank){
				CollisionMediator.getSingleton().tankCount.decrementAndGet();
			}else if(c instanceof Blood){
				CollisionMediator.getSingleton().bloodCount.decrementAndGet();
			}	
		}
		return success;
	}

	
	
	
	

}
