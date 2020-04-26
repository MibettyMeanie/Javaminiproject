package com.soc;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	public ArrayList<ConnectionToClients> clientList= new ArrayList<ConnectionToClients>();
	
	
	public Server (int port) throws IOException  {
		System.out.println("Welcome to Chai Chat Club");
		ServerSocket ss= new ServerSocket(port);
        Server y = this;
		
		Thread accept = new Thread() {
			 public void run(){
	                while(true){
	                    try{
	                        Socket s = ss.accept();
	                        System.out.println("User Accepted");
	                        ConnectionToClients x= new ConnectionToClients(s);
	                        x.S=y;
	                        clientList.add(x);
	                        System.out.println("User added to list");
	                    }
	                    catch(IOException e){ e.printStackTrace(); }
	                }
			 }
		};
        accept.start();
	}

}


