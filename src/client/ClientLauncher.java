package client;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
 
public class ClientLauncher {
	
	static int clientes;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		if (args.length > 0) {
		    try {
		    	clientes = Integer.parseInt(args[0]);
		    	ClientThread.Server_host = args[1];
		    	ClientThread.Cache_host = args[2];
		    } catch (Exception e) {
		        System.err.println("ClientLauncher.jar number_of_clients Server_IP Cache_IP");
		    }
		}
		else{
			System.err.println("ClientLauncher.jar number_of_clients Server_IP Cache_IP");
			System.exit(1);
		}
	
		Set<ClientThread> ClientThreads = new HashSet<ClientThread>();
		
		for(int i=1; i<=clientes; i++){
			ClientThread cliente = new ClientThread();
			cliente.setName(Integer.toString(i));
			new Thread(cliente).start();
			ClientThreads.add((ClientThread) cliente);
			TimeUnit.SECONDS.sleep(2);
		}
	}
}