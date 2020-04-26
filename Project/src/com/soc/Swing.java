package com.soc;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.*;
public class Swing  {
	
	public ArrayList<newframe> framelist= new ArrayList<newframe>();
	private JFrame mainFrame;
	private JLabel heading;
	private JPanel entername;
	private JPanel enterto;
	private JPanel status;
	public Socket s;
	
	
	public Swing() {
		prepareGUI();
	}
		
	
	public void prepareGUI() {
		
		Thread GUI = new Thread() {
			public void run() {
				  mainFrame = new JFrame("JChat");
			      mainFrame.setSize(600,500);
			      mainFrame.setLayout(new GridLayout(4, 0));

			      heading = new JLabel("WELCOME TO JChat",JLabel.CENTER );
			      heading.setFont(new Font("Calibri", Font.BOLD, 20));
			      entername = new JPanel();
			      enterto = new JPanel(); 
			      
			      status = new JPanel();
			      
			      entername.setLayout(new FlowLayout());
			      enterto.setLayout(new FlowLayout());
			      
			      JLabel name = new JLabel("ENTER NAME");
			      JTextField nameinp= new JTextField(10);
			      JButton b1 = new JButton("Enter");
			      
			      b1.addActionListener((e)->{
						try {
							Add_User(nameinp.getText());
						} catch (UnknownHostException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					});
			      
			      
			      
			      entername.add(name);
			      entername.add(nameinp);
			      entername.add(b1);
			      
			      JLabel to = new JLabel("SEND TO");
			      JTextField send= new JTextField(10);
			      JButton b2 = new JButton("Enter");
			      
			      
			      enterto.add(to);
			      enterto.add(send);
			      enterto.add(b2);
			      
			      b2.addActionListener((e)->{
			    	  	try {
			    	  		System.out.println(send.getText());
							send("to:"+send.getText());
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						send.setText(null);
					});
			      
			      
			      
			      
			      mainFrame.add(heading);
			      mainFrame.add(entername);
			      mainFrame.add(enterto);
			     
			      mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
			      mainFrame.setVisible(true);  
			}
			
		};
		
		GUI.start();
		  
	      
	}
	
	public void send (String G) throws IOException {
		OutputStreamWriter os = new OutputStreamWriter(s.getOutputStream());
		PrintWriter out = new PrintWriter(os);
		out.println(G);
		out.flush();
	}
	
	
	
	
	public void Add_User(String name) throws UnknownHostException, IOException {
		
		Thread user = new Thread() {
			public void run() {
				String ip = "localhost";
				int port =9999;
				try {
					s = new Socket(ip,port);
					send("name:"+name);
					while(true) {
						String prev=null;
						try {
							BufferedReader bt;
							bt = new BufferedReader(new InputStreamReader(s.getInputStream()));
							String la = bt.readLine();
							
							if(la!=prev) {
								System.out.println(la);
								prev = la;
								String[] prefix = la.split(":");
								if(prefix[0].contentEquals("status")) {
									
									System.out.println("called handle func");
									if(prefix[1].contentEquals("valid")){
										newframe x= new newframe(s, prefix[3],prefix[2]);
										System.out.println("newframe created");
										framelist.add(x);
									}else {
										JLabel sta = new JLabel("INVALID USER",JLabel.CENTER );
										 status.add(sta);
										 mainFrame.add(status);
										 
										 mainFrame.setVisible(true); 
										 
										Thread.sleep(1000);
										sta.setText(null);
										 
									}
									
								}else if(prefix[0].contentEquals("from")) {
									
									for(int i=0;i<framelist.size();i++) {
										int var = framelist.get(i).name.compareToIgnoreCase(prefix[2]);
										if(var==0) {
											framelist.get(i).reply(prefix[1],prefix[3]);
											break;
										}
									}
								}else if(prefix[0].contentEquals("newmess")) {
									newframe x= new newframe(s, prefix[2],prefix[1]);
									System.out.println("newframe created");
									framelist.add(x);
								}
							}
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			
		};
		
		
		user.start();
		
		
	} 
	
}