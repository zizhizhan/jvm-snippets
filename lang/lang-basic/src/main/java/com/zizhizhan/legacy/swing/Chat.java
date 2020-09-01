package com.zizhizhan.legacy.swing;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.text.*;
import java.awt.event.*;


public class Chat extends JFrame {
	
	private static final long serialVersionUID = -4726885011152229925L;

	private MsgAdmin msgConsole = new MsgAdmin();
	

	public Chat() {
		super("QQ");
		
		setSize(400,500);
		setLocation(200, 100);	
		
		setLayout(new BorderLayout());				
		
		/*
		final DefaultListModel dlm = new DefaultListModel();
		
		
		list = new JList(dlm);
		
		list.setAutoscrolls(true);
		list.setVisibleRowCount(18);*/
	
		final JTextArea list = new JTextArea(14,0);
		list.setEditable(false);
		final JScrollPane jsp = new JScrollPane(list);
		
		Thread t = new Thread(){
			public void run(){				
				while(true){					
					HashMap<String,String> hm = msgConsole.recevice();	
					
					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
										
					list.append("\n" + hm.get("ip") + "  " + sdf.format(new Date()) + "\n" + hm.get("content"));
					final JScrollBar vbar = jsp.getVerticalScrollBar();
					final AdjustmentListener al = new AdjustmentListener(){
						public void adjustmentValueChanged(AdjustmentEvent e) {
							vbar.setValue(vbar.getMaximum());							
						}
					};
					vbar.addAdjustmentListener(al);					
					vbar.addMouseMotionListener(new MouseMotionAdapter(){
						public void mouseDragged(MouseEvent e) {
							vbar.removeAdjustmentListener(al);
						}
					});					
				}
			}			
		};		
		t.start();
					
		
		JPanel msgList = new JPanel(new BorderLayout());
				
		JPanel tool = new JPanel(new GridLayout(1,10));		
		Color mycolor = new Color(0x008888);
		tool.setBackground(mycolor);
		
		try{	
			
			JButton foreColor = new JButton(new ImageIcon("Editor\\foreColor.gif"));	
			JButton backColor = new JButton(new ImageIcon("Editor\\backColor.gif"));	
			JButton em = new JButton(new ImageIcon("Editor\\em.gif"));
			JButton img = new JButton(new ImageIcon("Editor\\img.gif"));	
			JButton cut = new JButton(new ImageIcon("Editor\\cut.gif"));	
			JButton paste = new JButton(new ImageIcon("Editor\\paste.gif"));
			
			tool.add(foreColor);
			tool.add(backColor);
			tool.add(em);
			tool.add(img);
			tool.add(cut);
			tool.add(paste);	
			
			for(int i = 0; i < tool.getComponents().length; i++){
				tool.getComponents()[i].setBackground(mycolor);
				((JButton)tool.getComponents()[i]).setBorder(null);
				((JButton)tool.getComponents()[i]).setSize(20, 20);				
			}
			
			for(int i = 0; i < 12; i++){
				JLabel jl = new JLabel();
				tool.add(jl);			
			}	
			
		}catch(Exception ex){
			ex.printStackTrace();
		}	
		
		msgList.add(jsp,"Center");
		msgList.add(tool,"South");	
		
		JPanel msgSendPanel = new JPanel(new BorderLayout());
		
		final JTextArea sendBox = new JTextArea();
		JScrollPane scrollbox = new JScrollPane(sendBox);	
		
		JPanel oper = new JPanel(new GridLayout(1,4));		
		
		
		JButton recordBtn = new JButton("A");
		recordBtn.setFont(new Font("楷体_GB2312",Font.LAYOUT_LEFT_TO_RIGHT,12));
		recordBtn.setMargin(new Insets(0,0,0,0));
		JButton closeBtn = new JButton("B");
		closeBtn.setFont(new Font("楷体_GB2312",Font.LAYOUT_LEFT_TO_RIGHT,12));
		closeBtn.setMargin(new Insets(0,0,0,0));
		JButton sendBtn = new JButton("C");
		sendBtn.setFont(new Font("楷体_GB2312",Font.LAYOUT_LEFT_TO_RIGHT,12));
		sendBtn.setMargin(new Insets(0,0,0,0));
		
		closeBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		sendBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Thread t = new Thread(){				
					public void run(){
						if(sendBox.getText().equals("")){
							JOptionPane.showMessageDialog(null, "MessageDialog");
							return;
						}
						msgConsole.send(sendBox.getText());	
						sendBox.setText("");
					}
				};
				t.start();				
			}
		});
		
		oper.add(recordBtn);
		for(int i = 0; i < 4; i++){
			JLabel jl = new JLabel();
			oper.add(jl);			
		}			
		oper.add(closeBtn);
		oper.add(sendBtn);
		 
		msgSendPanel.add(scrollbox,"Center");
		msgSendPanel.add(oper,"South");
		
		add(msgList,"North");
		add(msgSendPanel,"Center");		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	
	public static void main(final String[] args) {
		new Chat();
	}

}
