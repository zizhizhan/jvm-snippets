package com.zizhizhan.legacies.swing;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class MyGUI {

    /**
     * @param args
     */
    public static void main(String[] args) {
        final Frame f = new Frame("我的文档");
        f.setSize(600, 400);
        f.setLocation(100, 100);
        f.setBackground(Color.PINK);
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        MenuBar mb = new MenuBar();

        Menu m1 = new Menu("File");
        Menu m2 = new Menu("Edit");
        Menu m3 = new Menu("Source");

        MenuItem mi1 = new MenuItem("Open");
        MenuItem mi2 = new MenuItem("Save");
        MenuItem mi3 = new MenuItem("Exit");
        MenuItem mi4 = new MenuItem("Copy");
        MenuItem mi5 = new MenuItem("Paste");
        MenuItem mi6 = new MenuItem("Delete");
        MenuItem mi7 = new MenuItem("Clear");
        MenuItem mi8 = new MenuItem("New");


        m1.add(mi8);
        m1.add(mi1);
        m1.add(mi2);
        m1.add(mi3);
        m2.add(mi4);
        m2.add(mi5);
        m2.add(mi6);
        m3.add(mi7);

        mb.add(m1);
        mb.add(m2);
        mb.add(m3);

        f.setMenuBar(mb);

        final TextArea ta = new TextArea(22, 1);
        f.add(ta, "North");

        final PopupMenu pm = new PopupMenu("RightKey");

        pm.add("a string");


        ta.addMouseListener(new MouseAdapter() {

            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    pm.show(e.getComponent(), e.getX(), e.getY());
                }
            }

            ;

        });


        mi1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FileDialog fd = new FileDialog(f, "Open My File", FileDialog.LOAD);
                fd.setVisible(true);
                String file = fd.getDirectory() + fd.getFile();
                if (file != null) {
                    try {
                        FileInputStream fis = new FileInputStream(file);
						/*BufferedReader br = new BufferedReader(new InputStreamReader(fis));
						String x = "";
						while(br.ready()){
							x += br.readLine() + "\n";
						}
						ta.setText(x);*/

                        byte[] b = new byte[10 * 1024];
                        int len = fis.read(b);
                        ta.append(new String(b, 0, len));

                        fis.close();


                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        mi3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        f.setVisible(true);


    }

}
