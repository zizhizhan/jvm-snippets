package com.zizhizhan.games.tank;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import com.zizhizhan.games.Direction;
import com.zizhizhan.games.Team;

import static com.zizhizhan.games.tank.TankConstant.*;

public abstract class Tank extends TankComposite {

	protected static final double DELTA = 0.75;
	protected static Random rand = new Random(System.currentTimeMillis());	
	
	private int previousX, previousY;
	
	protected Direction launchDirection = Direction.BOTTOM;
	protected Direction dir = Direction.STOP;
	
	private boolean leftPressed = false;
	private boolean upPressed = false;
	private boolean rightPressed = false;
	private boolean downPressed = false;
	
	private int health = 100;
	private int id;
	
	private int step;		
	
	abstract protected Missile createMissile(Direction d);
	
	protected void preDraw(){
		CollisionMediator.getSingleton().collision(this);
		
		if(!alive.get() && team == Team.ROBOT){
			for(int i = 0; i < this.getComposites().size(); i++){
				remove(this.getComposites().get(i));
			}			
			scenario.remove(this);				
			return;
		}		
	}
	
	protected void postDraw(){
		moving();
		ai();
	}
	
	protected void ai(){
		if(team == Team.ROBOT){
			Direction[] dirs = Direction.values();
			if(step == 0) {
				step = rand.nextInt(16) + 3;
				int rn = rand.nextInt(dirs.length);
				dir = dirs[rn];
			}			
			step--;
			
			if(this.alive.get() && rand.nextInt(40) > 32 && this.dir != Direction.STOP){ 
				this.fire();
			}
		}
	}

	private void moving() {	
		previousX = x;
		previousY = y;
		switch (dir) {
		case TOP:
			y -= TANK_YSPEED;
			break;
		case RIGHT:
			x += TANK_XSPEED;
			break;
		case BOTTOM:
			y += TANK_YSPEED;
			break;
		case LEFT:
			x -= TANK_XSPEED;
			break;
		case TOP_RIGHT:
			y -= TANK_YSPEED * DELTA;
			x += TANK_XSPEED * DELTA;
			break;
		case TOP_LEFT:
			y -= TANK_YSPEED * DELTA;
			x -= TANK_XSPEED * DELTA;
			break;
		case BOTTOM_RIGHT:
			y += TANK_YSPEED * DELTA;
			x += TANK_XSPEED * DELTA;
			break;
		case BOTTOM_LEFT:
			y += TANK_YSPEED * DELTA;
			x -= TANK_XSPEED * DELTA;
			break;
		}
		if (dir != Direction.STOP) {
			launchDirection = dir;
		}
		if (x < 0 || x + TANK_WIDTH > GAME_WIDTH) {
			x = previousX;
		}
		if (y < 40 || y + TANK_HEIGHT > GAME_HEIGHT) {
			y = previousY;
		}		
	}
	
	private void reset(){
		this.x = previousX;
		this.y = previousY;
	}
	
	public void collideWall(Wall w){
		reset();
	}
	
	public void collideTank(Tank t){
		if(this != t){
			this.reset();
			t.reset();
		}
	}
	
	public void eat(Blood c) {
		c.setAlive(false);
		this.setHealth(100);
	}

	protected void fire() {
		Missile m = createMissile(launchDirection);	
		this.add(m);			
	}

	private void superFire() {
		for(Direction d : Direction.values()){
			if(d != Direction.STOP){
				Missile m = createMissile(d);			
				this.add(m);
			}
		}
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public KeyListener getKeyListener() {
		return new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				switch (key) {
				case KeyEvent.VK_F2:
					if(alive.compareAndSet(false, true)){	
						health = 100;	
					}
					break;
				case KeyEvent.VK_LEFT:
					leftPressed = true;
					break;
				case KeyEvent.VK_UP:
					upPressed = true;
					break;
				case KeyEvent.VK_RIGHT:
					rightPressed = true;
					break;
				case KeyEvent.VK_DOWN:
					downPressed = true;
					break;
				}
				locateDirection();
			}
	
			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();
				switch (key) {
				case KeyEvent.VK_UP:
					upPressed = false;
					break;
				case KeyEvent.VK_RIGHT:
					rightPressed = false;
					break;
				case KeyEvent.VK_DOWN:
					downPressed = false;
					break;
				case KeyEvent.VK_LEFT:
					leftPressed = false;
					break;
				case KeyEvent.VK_SPACE:				
					if(isAlive()){
						fire();
					}
					break;
				case KeyEvent.VK_ENTER:
					if(isAlive()){
						superFire();
					}
					break;
				}
				locateDirection();
			}
			
			private void locateDirection() {
				if (upPressed && !rightPressed && !downPressed && !leftPressed) {
					dir = Direction.TOP;
				} else if (upPressed && rightPressed && !downPressed && !leftPressed) {
					dir = Direction.TOP_RIGHT;
				} else if (!upPressed && rightPressed && !downPressed && !leftPressed) {
					dir = Direction.RIGHT;
				} else if (!upPressed && rightPressed && downPressed && !leftPressed) {
					dir = Direction.BOTTOM_RIGHT;
				} else if (!upPressed && !rightPressed && downPressed && !leftPressed) {
					dir = Direction.BOTTOM;
				} else if (!upPressed && !rightPressed && downPressed && leftPressed) {
					dir = Direction.BOTTOM_LEFT;
				} else if (!upPressed && !rightPressed && !downPressed && leftPressed) {
					dir = Direction.LEFT;
				} else if (upPressed && !rightPressed && !downPressed && leftPressed) {
					dir = Direction.TOP_LEFT;
				} else if (upPressed && rightPressed && !downPressed && leftPressed) {
					dir = Direction.TOP;
				} else if (upPressed && rightPressed && downPressed && !leftPressed) {
					dir = Direction.RIGHT;
				} else if (!upPressed && rightPressed && downPressed && leftPressed) {
					dir = Direction.BOTTOM;
				} else if (upPressed && !rightPressed && downPressed && leftPressed) {
					dir = Direction.LEFT;
				} else {
					dir = Direction.STOP;
				}
			}
	
		};
	}

}