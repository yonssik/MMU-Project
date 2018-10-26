package com.hit.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class CacheUnitClient {

	public static final String LOCAL_HOST = "localhost";
	public static final Integer PORT = 12345;
	
	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;
	private InetAddress address;
	
	//Default C'tor
	public CacheUnitClient() {}
	
	
	/**
	 * Send Request From Client to Server 
	 * @param request - Json
	 * @return response from the Server
	 */
	public String send(String request) {
		String response = null;
		
		try {
			address = InetAddress.getByName(LOCAL_HOST);	
			socket = new Socket(address, PORT);		//connect to server
			output = new DataOutputStream(socket.getOutputStream());
			input = new DataInputStream(socket.getInputStream());
			output.writeUTF(request);	//write to server
			output.flush();
			
			response = (String) input.readUTF(); //the respond - read the server input
			
			
			socket.close();
			output.close();
			input.close();
			return response;	//return to CacheUnitClientObserver
		} catch(IOException e) {
			return "No connection to server";
		}
	}
	
}
