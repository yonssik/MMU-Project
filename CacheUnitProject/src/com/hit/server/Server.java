package com.hit.server;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import com.hit.services.CacheUnitController;
import com.hit.util.CLI;

public class Server implements PropertyChangeListener, Runnable{

	public static final Integer PORT_LISTENER = 12345;
	
	private CacheUnitController<String> cuc;
	private ServerSocket server;
	private Socket connection;
	private Thread serverThread = null;
	private Boolean status;
	private String[] configuration;
	
	//C'tor
	public Server() throws IOException {
		server = new ServerSocket(PORT_LISTENER);
	}
	
	
	@Override
	public void run() {	//run the server and wait for Request
		System.out.println("Server starts...........Input 'stop' for shut down");
		cuc = cuc.getInstance();
		cuc.config(configuration);
		try {
			while(status) {
				connection = server.accept(); 	//wait for request
			
				new Thread(new HandleRequest<String>(connection, cuc)).start();
			}
		} catch(Exception e){}
	}

	@Override
	
	public void propertyChange(PropertyChangeEvent evt) {	//Listen to CLI, received Command
		
		if(evt.getPropertyName().equals(CLI.START)) {	//if START run the Server
			status = true;
			configuration = (String[]) evt.getNewValue();
			serverThread = new Thread(this);
			serverThread.start();
		} else {	//if STOP, Stop the Server
			try {
				status = false;
				cuc.cacheCleaner();
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
	}
}
