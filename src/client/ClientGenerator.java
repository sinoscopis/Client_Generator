package client;

import java.io.*;
import java.util.concurrent.TimeUnit;
 
public class ClientGenerator {
	
	static int usuarios_por_cluster;
	static int distribucion;
	static int clusters;
	static int[][] ClientsArray = null;
	
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		if (args.length > 0) {
		    try {
		    	usuarios_por_cluster = Integer.parseInt(args[0]);
		    	clusters = Integer.parseInt(args[1]);
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
		ClientsArray = new int[clusters][usuarios_por_cluster];
		for(int i=0; i<clusters; i++){
			for(int j=0; j<usuarios_por_cluster; j++){
				GeneratorThread user = new GeneratorThread(i,j);
				new Thread(user).start();
				TimeUnit.MILLISECONDS.sleep(100);
			}
		}
		for(int i=0; i<clusters; i++){
			for(int j=0; j<usuarios_por_cluster; j++){
				RelationsThread user = new RelationsThread(ClientsArray[i][j]);
				new Thread(user).start();
				TimeUnit.MILLISECONDS.sleep(200);
			}
		}
	}
}