package com.zizhizhan.games.samples;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.plaf.metal.MetalBorders;

public class SampleScenario extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JLabel bgLabel;

	private JLabel opLabel;

	private Point position = new Point(0, 0);

	SampleScenario() {

		initializeMenuBar();
		initializeCartoon();

		this.getContentPane().setBackground(Color.BLACK);
		this.setSize(800, 600);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);

	}

	private void initializeCartoon() {

		bgLabel = new JLabel();
		opLabel = new JLabel();

		opLabel.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
				position = e.getPoint();
			}

			public void mouseReleased(MouseEvent e) {
			}

		});

		opLabel.addMouseMotionListener(new MouseMotionListener() {

			public void mouseDragged(MouseEvent e) {
				JLabel l = (JLabel) e.getSource();
				int x = (int) (l.getX() + e.getX() - position.getX());
				int y = (int) (l.getY() + e.getY() - position.getY());
				l.setLocation(x, y);
			}

			public void mouseMoved(MouseEvent e) {
			}
		});

		this.getLayeredPane().add(bgLabel, new Integer(Integer.MIN_VALUE));
		this.getLayeredPane().add(opLabel, new Integer(Integer.MIN_VALUE + 1));

	}

	private void initializeMenuBar() {

		JMenuBar mb = new JMenuBar();

		JMenu m = new JMenu("File");

		JMenuItem mi1 = new JMenuItem("Set Background");
		JMenuItem mi2 = new JMenuItem("Loading Image");
		JMenuItem mi3 = new JMenuItem("Exit");

		mi1.addActionListener(this);
		mi2.addActionListener(this);
		mi3.addActionListener(this);

		m.add(mi1);
		m.add(mi2);
		m.add(mi3);

		mb.add(m);

		setJMenuBar(mb);

	}

	public static void main(String[] args) {
		new SampleScenario();
	}

	public void actionPerformed(ActionEvent e) {
		dispatch(e);
	}

	private void dispatch(ActionEvent e) {
		Object src = e.getSource();
		if (src instanceof JMenuItem) {
			if (((JMenuItem) src).getText().equals("Exit")) {
				doExit();
			} else {
				doAction((JMenuItem) src);
			}
		}
	}

	private void doExit() {
		System.exit(0);
	}

	private void doAction(JMenuItem src) {
		JFileChooser fc = new JFileChooser("./");
	
		int result = fc.showOpenDialog(this);

		if (result == JFileChooser.APPROVE_OPTION) {
			File f = fc.getSelectedFile();

			if (src.getText().equals("Set Background")) {
				ImageIcon img = new ImageIcon(f.getPath());

				bgLabel.setIcon(img);
				int wid = (getWidth() > img.getIconWidth() ? getWidth() : img
						.getIconWidth());
				int hei = (getHeight() > img.getIconHeight() ? getHeight()
						: img.getIconHeight());

				bgLabel.setBounds(0, 0, wid, hei);

			} else {

				ScheduledExecutorService timer = Executors
						.newScheduledThreadPool(1);
				try {
					timer.scheduleAtFixedRate(new CartoonImage(f, opLabel), 0,
							100, TimeUnit.MILLISECONDS);
				} catch (IOException e) {
					e.printStackTrace();
				}
				opLabel.setBounds(300, 200, 64, 128);
				opLabel.setBorder(new MetalBorders.Flush3DBorder());
				opLabel.setOpaque(false);
			}

			((JPanel) getContentPane()).setOpaque(false);
		}

	}

	public class CartoonImage implements Runnable {
		BufferedImage image;
		JLabel label;
		int i;

		public CartoonImage(File f, JLabel label) throws IOException {
			this.image = ImageIO.read(f);
			this.label = label;
		}

		public void run() {
			if (i == 8) {
				i = 0;
			}
			ImageIcon img = new ImageIcon(image.getSubimage(i++ * 64, 0, 64,
					128));
			label.setIcon(img);
		}
	}

}
