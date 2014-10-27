/*
 * PARA CREAR EL DATASET
 * 
 * clusters = Numero de clusters en el sistema
 * Server_host = Ip del Server 
 * distribucion = Numero de la distribución para la creación del dataset: 1 lineal, 2 long-tail
 * porc_amigos_en_cache = Porcentaje del total de amigos que estaran en la misma cache
 * 
 */

package client;

import java.io.*;
import java.util.concurrent.TimeUnit;
import java.util.Scanner; 
public class ClientGenerator {
	
	public static int porc_amigos_en_cache = 0;
	static int usuarios_por_cluster;
	static int distribucion;
	static int clusters;
	static int[] ClustersArray = null;
	static int[][] ClientsArray = null;
	static int[] cache1,cache2,cache3,cache4,cache5;
	
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		if (args.length > 0) {
		    try {
		    	clusters = Integer.parseInt(args[0]);
		    	GeneratorThread.Server_host = args[1];
		    	distribucion = Integer.parseInt(args[2]);
		    	porc_amigos_en_cache = Integer.parseInt(args[3]);
		    } catch (Exception e) {
		        System.err.println("ClientGenerator.jar clusters Server_IP Distribucion");
		        System.exit(1);
		    }
		}
		else{
			System.err.println("ClientGenerator.jar clusters Server_IP Distribucion");
			System.exit(1);
		}
		ClustersArray = new int[clusters];
		Scanner keyboard = null;
		for(int i=0; i<clusters; i++){
			keyboard = new Scanner(System.in);
			System.out.println("Users for cache number "+ (i+1) +":");
			int myint = keyboard.nextInt();
			ClustersArray[i]=myint;
		}
		keyboard.close();
		
		ClientsArray = new int[clusters][getMaxValue(ClustersArray)];
		for(int i=0; i<clusters; i++){
			for(int j=0; j<ClustersArray[i]; j++){
				GeneratorThread user = new GeneratorThread(i,j);
				new Thread(user).start();
				TimeUnit.MILLISECONDS.sleep(500);
			}
		}
		int user_count=0;
		for(int i=0; i<ClientGenerator.clusters; i++){
			if (i==0){
				cache1 = new int[ClientGenerator.ClustersArray[i]];
				for (int j = 0; j < ClientGenerator.ClustersArray[i]; j++) {
					cache1[j] = user_count+1;
					user_count++;
			    }
			}
			else if (i==1){
				cache2 = new int[ClientGenerator.ClustersArray[i]];
				for (int j = 0; j < ClientGenerator.ClustersArray[i]; j++) {
					cache2[j] = user_count+1;
					user_count++;
			    }
			}
			else if (i==2){
				cache3 = new int[ClientGenerator.ClustersArray[i]];
				for (int j = 0; j < ClientGenerator.ClustersArray[i]; j++) {
					cache3[j] = user_count+1;
					user_count++;
			    }
			}
			else if (i==3){
				cache4 = new int[ClientGenerator.ClustersArray[i]];
				for (int j = 0; j < ClientGenerator.ClustersArray[i]; j++) {
					cache4[j] = user_count+1;
					user_count++;
			    }
			}
			else{
				cache5 = new int[ClientGenerator.ClustersArray[i]];
				for (int j = 0; j < ClientGenerator.ClustersArray[i]; j++) {
					cache5[j] = user_count+1;
					user_count++;
			    }
			}
		}
		
		for(int i=0; i<clusters; i++){
			for(int j=0; j<ClustersArray[i]; j++){
				RelationsThread user = new RelationsThread(ClientsArray[i][j],(i+1));
				new Thread(user).start();
				TimeUnit.MILLISECONDS.sleep(5000);
			}
		}
	}
	
	public static int getMaxValue(int[] array){  
	      int maxValue = array[0];  
	      for(int i=1;i < array.length;i++){  
	      if(array[i] > maxValue){  
	      maxValue = array[i];  
	         }  
	     }  
	     return maxValue;  
	}
}