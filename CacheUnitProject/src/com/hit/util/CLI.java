package com.hit.util;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class CLI implements Runnable {

	public static final String START = "start";
	public static final String SHUTDOWN = "stop";
	public static final String LRU = "LRU";
	public static final String NRU = "NRU";
	public static final String RANDOM = "RANDOM";
	
	private Scanner input;
	private PrintWriter output;
	private PropertyChangeSupport listener;
	
	//C'tor
	public CLI(InputStream in, OutputStream out) {
		
		input = new Scanner(in);
		output = new PrintWriter(out);
		listener = new PropertyChangeSupport(this);
		
	}
	
	//Add Listener
	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		
		listener.addPropertyChangeListener(pcl);
		
	}
	// Remove Listener
	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		
		listener.removePropertyChangeListener(pcl);
		
	  }
	
	
	 // print the String
	public void write(String string) 
	{
		System.out.println(string);
	}
	
	
	@Override
	public void run() {
		boolean flag = true; // Check start/stop command
		boolean start = true; // Flag to avoid server duplicates  
		String[] command = new String[3]; // Array of commands required for input
		// command[0] = start/stop 		command[1] = Algorithm 		command[2] = capacity
		
		write("Do you want start the server? strat/stop");
		while(flag) {
			command[0] = input.next().toLowerCase(); // Only lower characters for command
			while(!command[0].equals(START) && !command[0].equals(SHUTDOWN)) {
				write("Not a valid command");
				command[0] = input.next().toLowerCase();
			}
			
			switch (command[0]) {
			case START:// The user need put here configuration for server 
				if(start) { // Only single start order
					write("Chose the algorithm work with : LRU/NRU/RANDOM");
					command[1] = input.next().toUpperCase(); // Only upper characters for command
					while(!command[1].equals(LRU) && !command[1].equals(NRU) && !command[1].equals(RANDOM)) {
						write("Not a valid command");
						command[1] = input.next().toUpperCase();
					}
			
					write("Chose the capacity of cache : min(4)/max(100)");
					int capacity = input.nextInt(); // Only upper characters for command
					while(capacity < 4 || capacity > 100) {
						write("Capacity not in range");
						capacity = input.nextInt();
					}
					command[2] = String.valueOf(capacity);
					listener.firePropertyChange(CLI.START, null, command);	//Send to Server
					
				} else { write("Server alredy runnig"); }
				start = false;
				
				break;

			case SHUTDOWN:
				write("Server is Shutdown");
				flag = false;
				if(!start) {
					listener.firePropertyChange(CLI.SHUTDOWN, null, null);	//Send to Server
				}
				input.close();
				output.close();
			}
		}
	}

}
