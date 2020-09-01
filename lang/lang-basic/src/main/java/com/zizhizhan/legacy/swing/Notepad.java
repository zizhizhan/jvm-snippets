package com.zizhizhan.legacy.swing;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;


public class Notepad extends JFrame {

    private static final long serialVersionUID = 1L;

    public Notepad() {

        super("我的记事本");
        setSize(600, 400);
        setLocation(100, 100);

        JMenuBar jmb = new JMenuBar();

        JMenu jm1 = new JMenu("文件");
        JMenuItem jmi1 = new JMenuItem("新建");
        final JMenuItem jmi2 = new JMenuItem("打开");
        JMenuItem jmi3 = new JMenuItem("关闭");
        jm1.add(jmi1);
        jm1.add(jmi2);
        jm1.add(jmi3);

        JMenu jm2 = new JMenu("编辑");
        JMenuItem jmi4 = new JMenuItem("复制");
        JMenuItem jmi5 = new JMenuItem("剪切");
        JMenuItem jmi6 = new JMenuItem("粘贴");
        jm2.add(jmi4);
        jm2.add(jmi5);
        jm2.add(jmi6);

        jmb.add(jm1);
        jmb.add(jm2);
        setJMenuBar(jmb);

        final JTextArea jta = new JTextArea(19, 20);

        JScrollPane jsp = new JScrollPane(jta);
        jsp.setSize(600, 400);

        add(jsp, "North");


        jmi1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO 自动生成方法存根
                jta.setText("");
            }
        });

        jmi2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO 自动生成方法存根

                JFileChooser chooser = new JFileChooser();
                chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        // TODO 自动生成方法存根
                        return true;
                    }

                    @Override
                    public String getDescription() {
                        // TODO 自动生成方法存根
                        return null;
                    }
                });

                int ret = chooser.showOpenDialog(jmi2);


                if (ret == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    try {
                        FileInputStream fis = new FileInputStream(file);
                        byte[] buf = new byte[10240];
                        int len = fis.read(buf);
                        jta.setText(new String(buf, 0, len));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setVisible(true);
    }

    public static void main(String[] args) {

        new Notepad();

    }


}
