package com.soc;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class newframe {
	private DefaultListModel<String>listmodel = new DefaultListModel<String>();
	private JList<String>messagelist = new JList<String>(listmodel);
	private JTextField input = new JTextField();
	public Socket l;
	public String name;
	private JFrame chatbox;
	public String to;
	
	
	
	public newframe(Socket S,String na,String to){
		
		newframe n= this;
		
		
		Thread frame = new Thread() {
			public void run() {
				n.l = S;
				n.name = na;
				n.to= to;
				chatbox= new JFrame("Chatbox "+n.name+" to "+n.to);
				chatbox.setSize(400,300);
				chatbox.setLayout(new BorderLayout());
				chatbox.add(new JScrollPane(messagelist),BorderLayout.CENTER);
				chatbox.add(input,BorderLayout.SOUTH);
				input.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						String text = input.getText();
						try {
							send(text,S);
							listmodel.addElement("You: "+ text);
							input.setText(null);							
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						
					}
				});
				 chatbox.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
				 chatbox.setVisible(true);  
				
			}
		};
		frame.start();
		
	}
	
	public void send (String G, Socket s) throws IOException {
		OutputStreamWriter os = new OutputStreamWriter(s.getOutputStream());
		PrintWriter out = new PrintWriter(os);
		out.println("mess:"+G);
		out.flush();
	}
	
	public void reply(String from, String mess) {
		listmodel.addElement(from+": "+ mess);
		 chatbox.setVisible(true);  
	}

}