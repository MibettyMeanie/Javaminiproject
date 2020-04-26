package com.soc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionToClients {
	
	Socket socket;
	String name;
	Server S;
	Socket send;
	String sendname;
	
	
	
	public void sendit(String mess, Socket s) throws IOException {
		OutputStreamWriter os3;
		
		os3= new OutputStreamWriter(s.getOutputStream());
		PrintWriter out3 = new PrintWriter(os3);
		out3.println(mess);
		out3.flush();
	}
	
	
	ConnectionToClients(Socket s) throws IOException{
		
		ConnectionToClients y = this;
		
		
		Thread listen = new Thread() {
			public void run() {
				while (true) {	
					try {
						y.socket=s;
						System.out.println("Listening started");
						String name="";
						
						BufferedReader bt1;
						bt1 = new BufferedReader(new InputStreamReader(s.getInputStream()));
						String la = bt1.readLine();
						System.out.println(la);
						if(la!=null) {
							String[] prefix = la.split(":");
							if(prefix[0].contentEquals("name")) {
								y.name = prefix[1];
							}else if(prefix[0].contentEquals("to")) {
								
								int flag=0;
								
								for(int i=0;i<S.clientList.size();i++) {
									
									int var = S.clientList.get(i).name.compareToIgnoreCase(prefix[1]);
									
									if(var==0) {
										flag=1;
										y.sendname=(S.clientList.get(i).name);
										System.out.println(name);
										y.send = S.clientList.get(i).socket;
										S.clientList.get(i).send= y.socket;
										S.clientList.get(i).sendname= y.name;
										break;
										
									}
								}
								if (flag==1) {
									System.out.println("valid user");
									sendit("status:valid:"+y.sendname+":"+y.name,s);
									sendit("newmess:"+y.name+":"+y.sendname,y.send);
								}else {
									System.out.println("invalid user");
									sendit("status:invalid",s);
								}
							}else if(prefix[0].contentEquals("mess")) {
								sendit("from:"+y.name+":"+y.sendname+":"+prefix[1],send);
							}
						}
						
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		};
		listen.start();
		
	}

}
