package com.zizhizhan.legacy.swing;

import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JButton;

public class RefuseButton extends JButton {

    private static final long serialVersionUID = 5520557254624479948L;

    private RefuseButton friend;

    public RefuseButton(String title) {
        super(title);
        this.enableEvents(MouseEvent.MOUSE_MOTION_EVENT_MASK);
    }

    public void setFriend(RefuseButton friend) {
        this.friend = friend;
    }

    @Override
    protected void processMouseMotionEvent(MouseEvent e) {
        super.processMouseMotionEvent(e);
        this.setVisible(false);
        friend.setVisible(true);
    }

    public static void main(String[] args) {
        JFrame jf = new JFrame("抓不到的按钮");
        jf.setSize(400, 400);
        jf.setLocation(100, 100);
        RefuseButton b1 = new RefuseButton("快来抓我呀");
        RefuseButton b2 = new RefuseButton("快来抓我呀");
        jf.add(b1, "North");
        jf.add(b2, "South");
        b1.setFriend(b2);
        b2.setFriend(b1);
        jf.setVisible(true);
        b2.setVisible(false);
    }

}
