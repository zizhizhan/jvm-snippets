package com.zizhizhan.games.tank;

import java.util.List;

import com.zizhizhan.games.Component;
import com.zizhizhan.games.Direction;
import com.zizhizhan.games.Team;

import static com.zizhizhan.games.tank.TankConstant.*;

public abstract class Missile extends TankComposite {

	private static final double DELTA = 0.75;
	protected Component parent;
	protected Direction dir;

	public Missile() {
		super();
	}

	public Missile(List<Component> components) {
		super(components);
	}

	public Component getParent() {
		return parent;
	}
	
	protected abstract Explode createExplode();

	protected void preDraw() {
		CollisionMediator.getSingleton().collision(this);			
		if(!alive.get()){	
			parent.remove(this);			
			return;
		}
	}

	protected void postDraw() {
		moving();
	}

	public void hit(Missile missile) {
		if (this.parent != missile.parent && this.alive.compareAndSet(true, false)) {
			missile.setAlive(false);	
			missile.dir = Direction.STOP;
			this.dir = Direction.STOP;
			parent.add(createExplode());
			parent.remove(this);
		}					
	}

	public void hit(Wall c) {
		if(this.alive.compareAndSet(true, false)){
			this.dir = Direction.STOP;
			parent.add(new Explode(x, y, this.parent));
			parent.remove(this);
		}		
	}

	public void hit(Tank t) {
		if (t.getTeam() != team && t.isAlive() && this.alive.compareAndSet(true, false)) {
			if (t.getTeam() == Team.ROBOT) {
				t.setHealth(t.getHealth() - 10);
			} else {
				t.setHealth(t.getHealth() - 5);
			}			
			
			if (t.getHealth() <= 0) {
				t.setAlive(false);				
			}		
			this.dir = Direction.STOP;
			parent.add(createExplode());	
			parent.remove(this);
		}		
	}

	private void moving() {		
		switch (dir) {
		case TOP:		
			y -= MISSILE_YSPEED;
			break;
		case RIGHT:			
			x += MISSILE_XSPEED;
			break;
		case BOTTOM:			
			y += MISSILE_YSPEED;
			break;
		case LEFT:			
			x -= MISSILE_XSPEED;
			break;
		case TOP_RIGHT:					
			y -= MISSILE_YSPEED * DELTA;
			x += MISSILE_XSPEED * DELTA;
			break;
		case TOP_LEFT:	
			y -= MISSILE_YSPEED * DELTA;
			x -= MISSILE_XSPEED * DELTA;
			break;
		case BOTTOM_RIGHT:			
			y += MISSILE_YSPEED * DELTA;
			x += MISSILE_XSPEED * DELTA;
			break;
		case BOTTOM_LEFT:			
			y += MISSILE_YSPEED * DELTA;
			x -= MISSILE_XSPEED * DELTA;
			break;		
		}
		
		if (x < 0 || y < 0 || x > GAME_WIDTH || y > GAME_HEIGHT) {
			if (this.alive.compareAndSet(true, false)) {
				parent.remove(this);
			}
		}
	}

}