package client;

import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
 
public class ClientGenerator {
	
	static int usuarios;
	static int distribucion;
	static int cluster;
	static int usuarios_procesados=0;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		if (args.length > 0) {
		    try {
		    	usuarios = Integer.parseInt(args[0]);
		    	cluster = Integer.parseInt(args[1]);
		    	GeneratorThread.Server_host = args[2];
		    	distribucion = Integer.parseInt(args[3]);
		    } catch (Exception e) {
		        System.err.println("ClientGenerator.jar number_of_users Server_IP Distribucion");
		        System.exit(1);
		    }
		}
		else{
			System.err.println("ClientGenerator.jar number_of_users Server_IP Distribucion");
			System.exit(1);
		}
		
		for(int i=1; i<=usuarios; i++){
			GeneratorThread user = new GeneratorThread();
			user.setName(Integer.toString(i));
			new Thread(user).start();
			TimeUnit.SECONDS.sleep(2);
		}
		for(int i=1; i<=usuarios; i++){
			RelationsThread user = new RelationsThread();
			user.setName(Integer.toString(i));
			new Thread(user).start();
			TimeUnit.SECONDS.sleep(2);
		}
	}
}