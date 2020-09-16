package com.zizhizhan.games.tank;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.zizhizhan.games.Component;
import com.zizhizhan.games.Scenario;

public abstract class TankScenario extends TankComposite implements Scenario {

	private Frame frame = new TankFrame();
	protected ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);	
	protected CollisionMediator mediator = CollisionMediator.getSingleton();
	protected Tank myTank;

	
	public TankScenario() {
		super();
	}

	public boolean add(Component c) {
		c.setScenario(this);
		return super.add(c);
	}

    public void draw(Graphics g) {

        super.draw(g);

        Color c = g.getColor();
        g.setColor(Color.WHITE);
        if (myTank != null) {
            g.drawString(TankConstant.TANK_HEALTH + ": " + myTank.getHealth(), 10, 50);
        }
        if (mediator != null) {
            g.drawString(TankConstant.SCORE + ": " + mediator.score.get(), 10, 70);
            g.drawString(TankConstant.TANK_COUNT + ": " + mediator.tankCount.get(), 10, 90);
            g.drawString(TankConstant.MISSILE_COUNT + ": " + mediator.missileCount.get(), 10, 110);
            g.drawString(TankConstant.EXPLODE_COUNT + ": " + mediator.explodeCount.get(), 10, 130);
        }
        g.setColor(c);
    }

	abstract protected void polling();
	
	public void launch() {

		mediator.setScenario(this);
		frame.addKeyListener(myTank.getKeyListener());

		exec.scheduleAtFixedRate(new Runnable() {
			public void run() {
				frame.repaint();
			}
		}, 0, TankConstant.POLLING_INTERVAL, TimeUnit.MILLISECONDS);

		exec.scheduleAtFixedRate(new Runnable() {
			public void run() {
				polling();
			}
		}, 0, TankConstant.POLLING_INTERVAL * 50, TimeUnit.MILLISECONDS);

	}

	public TankScenario(List<Component> components) {
		super(components);
	}

	class TankFrame extends Frame {

		private static final long serialVersionUID = 1L;
		private Image backScreen = null;

		public TankFrame() {
			this.setSize(TankConstant.GAME_WIDTH, TankConstant.GAME_HEIGHT);
			this.setResizable(false);
			this.setBackground(Color.BLACK);
			this.setVisible(true);
			this.setTitle(TankConstant.TITLE);

			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			});
		}

		@Override
		public void paint(Graphics g) {
			draw(g);
		}

		@Override
		public void update(Graphics g) {

			if (backScreen == null) {
				backScreen = this.createImage(TankConstant.GAME_WIDTH, TankConstant.GAME_HEIGHT);
			}

			Graphics offScreen = backScreen.getGraphics();
			Color c = offScreen.getColor();
			offScreen.setColor(Color.BLACK);
			offScreen.fillRect(0, 0, TankConstant.GAME_WIDTH, TankConstant.GAME_HEIGHT);
			offScreen.setColor(c);
			paint(offScreen);

			g.drawImage(backScreen, 0, 0, null);
		}

	}

}