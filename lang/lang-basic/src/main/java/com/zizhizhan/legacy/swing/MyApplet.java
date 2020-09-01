package com.zizhizhan.legacy.swing;

import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class MyApplet extends Applet {
    /**
     *
     */
    private static final long serialVersionUID = -5163816529862749179L;
    int ix, iy, fx, fy;
    Image img;

    public void init() {
        System.out.println("init");
        Button bt = new Button("AppletButton");
        bt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    getAppletContext().showDocument(
                            new URL("http://localhost/postinfo.html"), "_blank");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        add(bt);
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                ix = e.getX();
                iy = e.getY();
            }

            public void mouseReleased(MouseEvent e) {
                Graphics g = getGraphics();
                g.setColor(Color.ORANGE);
                g.drawLine(ix, iy, e.getX(), e.getY());

            }
        });
        setSize(600, 400);

        getImage(getDocumentBase(), "T1.gif");
    }

    public void start() {
        System.out.println("start");
    }

    public void stop() {
        System.out.println("stop");
    }

    public void destory() {
        System.out.println("destory");
    }

    public void paint(Graphics g) {
        Font f = new Font("楷体_gb2312", Font.BOLD, 30);
        g.setFont(f);
        g.setColor(Color.PINK);
        g.drawString("My Applet 启动了，^_^，效果不错吧", 20, 30);
        g.drawImage(img, 0, 0, this);
    }
}
