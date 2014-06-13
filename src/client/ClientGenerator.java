package client;

import java.io.*;
import java.util.concurrent.TimeUnit;
 
public class ClientGenerator {
	
	static int usuarios;
	static int distribucion;
	static int cluster;
	static int usuarios_procesados=0;
	static int[] ClientsArray = null;
	
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		if (args.length > 0) {
		    try {
		    	usuarios = Integer.parseInt(args[0]);
		    	cluster = Integer.parseInt(args[1]);
		    	GeneratorThread.Server_host = args[2];
		    	distribucion = Integer.parseInt(args[3]);
		    } catch (Exception e) {
		        System.err.println("ClientGenerator.jar number_of_users cluster Server_IP Distribucion");
		        System.exit(1);
		    }
		}
		else{
			System.err.println("ClientGenerator.jar number_of_users cluster Server_IP Distribucion");
			System.exit(1);
		}
		ClientsArray = new int[usuarios];
		for(int i=0; i<usuarios; i++){
			GeneratorThread user = new GeneratorThread();
			user.setName(Integer.toString(i));
			new Thread(user).start();
			TimeUnit.MILLISECONDS.sleep(500);
		}
		ClientGenerator.usuarios_procesados=0;
		for(int i=0; i<usuarios; i++){
			RelationsThread user = new RelationsThread(ClientsArray[i]);
			user.setName(Integer.toString(i));
			new Thread(user).start();
			TimeUnit.MILLISECONDS.sleep(200);
		}
	}
}