package com.hit.server;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.hit.dm.DataModel;


public class Request<T> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	T body;
	Map<String, String> headers;
	
	//C'tor
	public Request(Map<String, String> headers, T body) {
		
		this.headers = new HashMap<>(headers);
		this.body = body;
	}
	
	//return the Body
	public T getBody() { return	body; }
	
	//return the Headers
	public Map<String, String> getHeaders() { return headers; }
	
	//Set Body
	public void setBody(T body) { this.body = body; }
	
	//Set Headers
	public void setHeaders(Map<String, String> headers) { this.headers = headers; }
	
	@Override
	public String toString() {
		return "Request [ body = " + body + ", headers = " + headers + " ]";
	}
	

}
