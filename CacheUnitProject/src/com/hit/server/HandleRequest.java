package com.hit.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Socket;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.hit.dm.DataModel;
import com.hit.services.CacheUnitController;

public class HandleRequest<T> implements Runnable {
	
	public final static String ACTION = "action";
	public final static String DELETE = "DELETE";
	public final static String GET = "GET";
	public final static String ADD = "UPDATE";
	
	private Socket socket;
	private CacheUnitController<T> controller;
	
	//C'tor
	public HandleRequest(Socket s, CacheUnitController<T> controller) {
		this.socket = s;
		this.controller = controller;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void run() { // Receive Json request
		Type ref;
		T req = null;
		DataModel<T>[] dataModels;
		
		try {
			DataOutputStream write = new DataOutputStream(socket.getOutputStream());
			DataInputStream reader = new DataInputStream(socket.getInputStream());
			req = (T) reader.readUTF();	//read the Request
			if(req.equals("stat")) {	//if the client want the statistic
				write.writeUTF(controller.showStatistic());
			} else {	//if the client want to do some Action
				ref = new TypeToken<Request<DataModel<T>[]>>(){}.getType(); //parse the Json
				Request<DataModel<T>[]> request = new Gson().fromJson((String) req, ref); //Set Request Object
				
				switch(request.getHeaders().get(ACTION)) { // Choosing the action that we get from client
				case DELETE : if(controller.delete(request.getBody())) {	//Delete data
					write.writeUTF("Succeeded"); }
				else { write.writeUTF("Faild"); } break;
				case ADD 	: if(controller.update(request.getBody())) {	//Add Data
					write.writeUTF("Succeeded"); }
				else { write.writeUTF("Faild"); } break;
				case GET 	: 
				dataModels = controller.get(request.getBody());		//Get Data
				String sendToClient = "";
				for(int i = 0; i < dataModels.length; i++) {
					sendToClient += "Data [ id = " + dataModels[i].getDataModelId() + 
							", content = " + dataModels[i].getContent() + " ]\n";
				}	
				write.writeUTF(sendToClient); break;
				default 	: System.out.println("The action is not valid!");
				}
			}
			
		} catch(JsonIOException | JsonSyntaxException | IOException e) {  e.getMessage(); }
		
	}

}
