package com.hit.server;

import java.io.IOException;

import com.hit.util.CLI;

public class CacheUnitServerDriver {
	
	public CacheUnitServerDriver() {}
	
	public static void main(String[] args) throws IOException {
		CLI cli = new CLI(System.in, System.out);
		Server server = new Server();
		cli.addPropertyChangeListener(server);	//cli listen to Server
		new Thread(cli).start();

	}

}

