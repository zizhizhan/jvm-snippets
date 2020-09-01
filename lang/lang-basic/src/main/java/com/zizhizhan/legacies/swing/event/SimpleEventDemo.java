package com.zizhizhan.legacies.swing.event;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class SimpleEventDemo extends JFrame {
    public SimpleEventDemo() {
        JButton jbtOk = new JButton("OK");
        setLayout(new FlowLayout());
        add(jbtOk);
        ActionListener listener = new OKListener();
        jbtOk.addActionListener(listener);
    }

    public static void main(String[] args) {
        JFrame frame = new SimpleEventDemo();
        frame.setTitle("SimpleEventDemo");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(250, 300);
        frame.setVisible(true);
    }
}

