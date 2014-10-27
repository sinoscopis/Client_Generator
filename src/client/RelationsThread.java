/*
 * Relaciona unos usuarios con otros dependiendo de la distribucion
 */

package client;

import java.io.*;
import java.net.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
 
public class RelationsThread extends Thread{

	static String Server_host;
	static String Cache_host;
	static int id_user, in_cache;
	int[] cache1_t = ClientGenerator.cache1;
	int[] cache2_t = ClientGenerator.cache2;
	int[] cache3_t = ClientGenerator.cache3;
	int[] cache4_t = ClientGenerator.cache4;
	int[] cache5_t = ClientGenerator.cache5;

	public RelationsThread(int i, int j) {
		id_user=i;
		in_cache=j;
		}

	public void run() {
		int User_id=id_user;
		int[] intArray = null;
		int new_friends = 0;
		int friendships_procesadas=0;
		Socket socket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		int Usuarios_totales=0;
		
		try {
			socket = new Socket(Server_host, 55555);
 
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
 
			String fromServer;
			String fromUser = null;
			
			while ((fromServer = in.readLine()) != null) {
				System.out.println("Server - " + fromServer);
				sleep(100);
				try {
					Usuarios_totales=Integer.parseInt(fromServer);
				}
				catch (Exception e){
					//e.printStackTrace();
				}
				if (fromServer.equals("exit"))
					break;
				if (fromServer.startsWith("Followers_set")){
					break;
				}
				if (fromServer.startsWith("......")){
					fromUser ="countusers";
					if (fromUser != null) {
						System.out.println("Client - " + fromUser);
						synchronized (socket){
							out.println(fromUser);
						}
					}
				}
				if (fromServer.startsWith("friends")){
					if (friendships_procesadas<new_friends)
					{
						fromUser ="insertfriendship," + User_id + "," + intArray[friendships_procesadas];
						friendships_procesadas=friendships_procesadas+1;
						if (fromUser != null) {
							System.out.println("Client - " + fromUser);
							synchronized (socket){
								out.println(fromUser);
							}
						}
					}
					else
					{
						break;
					}
				}
				if (Usuarios_totales != 0){
					new_friends = friendsByDistribution(ClientGenerator.distribucion,Usuarios_totales);
					if (new_friends == 0){
						break;
					}
					else{
						intArray = new int[new_friends];
						int in_your_cache = distributeFriendships(in_cache,new_friends);
						
						add_friendships(in_cache, intArray,in_your_cache,new_friends,User_id);
						
						fromUser ="insertfriendship," + User_id + "," + intArray[friendships_procesadas];
						friendships_procesadas=friendships_procesadas+1;
						Usuarios_totales = 0;
						if (fromUser != null) {
							System.out.println("Client - " + fromUser);
							synchronized (socket){
								out.println(fromUser);
							}
						}
					}
				}
			}
		} catch (UnknownHostException e) {
			System.err.println("Cannot find the host: " + Server_host);
			System.exit(1);
		} catch (IOException e) {
			System.out.println("Couldn't read/write from the connection: " +e.toString() );
			e.printStackTrace();
			System.exit(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally { //Make sure we always clean up	
			try {
				out.close();
				in.close();
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void add_friendships(int cache, int[] intArray, int friends_cache, int friends_tot, int user_id) {
		boolean esta = false;
		int pos = 0;
		int fin = 0;
		shuffleArray(cache1_t);
		shuffleArray(cache2_t);
		shuffleArray(cache3_t);
		shuffleArray(cache4_t);
		shuffleArray(cache5_t);
		int[] resto = null;
		if (cache == 1){
			for (int i = 0; i < friends_cache; i++) {
				
					if (cache1_t[i]==user_id)
					{
						esta = true;
						pos = i;
					}
					else
						intArray[i] = cache1_t[i];
					fin=i;
				    }
			if (esta)
				intArray[pos]=cache1_t[fin+1];
			int[] concatenate = combine(cache2_t, cache3_t);
			int[] concatenate2 = combine(concatenate, cache4_t);
			resto = combine(concatenate2, cache5_t);
		}
		else if (cache == 2){
			for (int i = 0; i < friends_cache; i++) {
				
					if (cache2_t[i]==user_id)
					{
						esta = true;
						pos = i;
					}
					else
						intArray[i] = cache2_t[i];
					fin=i;
				    }
			if (esta)
				intArray[pos]=cache2_t[fin+1];
			int[] concatenate = combine(cache1_t, cache3_t);
			int[] concatenate2 = combine(concatenate, cache4_t);
			resto = combine(concatenate2, cache5_t);
		}
		else if (cache == 3){
			for (int i = 0; i < friends_cache; i++) {
				
					if (cache3_t[i]==user_id)
					{
						esta = true;
						pos = i;
					}
					else
						intArray[i] = cache3_t[i];
					fin=i;
				    }
			if (esta)
				intArray[pos]=cache3_t[fin+1];
			int[] concatenate = combine(cache1_t, cache2_t);
			int[] concatenate2 = combine(concatenate, cache4_t);
			resto = combine(concatenate2, cache5_t);
		}
		else if (cache == 4){
			for (int i = 0; i < friends_cache; i++) {
				
					if (cache4_t[i]==user_id)
					{
						esta = true;
						pos = i;
					}
					else
						intArray[i] = cache4_t[i];
					fin=i;
				    }
			if (esta)
				intArray[pos]=cache4_t[fin+1];
			int[] concatenate = combine(cache1_t, cache2_t);
			int[] concatenate2 = combine(concatenate, cache3_t);
			resto = combine(concatenate2, cache5_t);
		}
		else{
			for (int i = 0; i < friends_cache; i++) {
				
					if (cache5_t[i]==user_id)
					{
						esta = true;
						pos = i;
					}
					else
						intArray[i] = cache5_t[i];
					fin=i;
				    }
			if (esta)
				intArray[pos]=cache5_t[fin+1];
			int[] concatenate = combine(cache1_t, cache2_t);
			int[] concatenate2 = combine(concatenate, cache3_t);
			resto = combine(concatenate2, cache4_t);
		}
		shuffleArray(resto);
		esta = false;
		pos = 0;
		fin = 0;
		for (int i = friends_cache; i < friends_tot; i++) {
			
			if (resto[(i-friends_cache)]==user_id)
			{
				esta = true;
				pos = i-friends_cache;
			}
			else
				intArray[i] = resto[(i-friends_cache)];
			fin=i;
		    }
	if (esta)
		intArray[pos]=resto[fin+1];
		
	
	}
	
	public static int[] combine(int[] a, int[] b){
        int length = a.length + b.length;
        int[] result = new int[length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

	private int distributeFriendships(int cache, int new_friends) {
		int friends_your_cache = 1;
		friends_your_cache = (int)(new_friends*ClientGenerator.porc_amigos_en_cache)/100;
		if (friends_your_cache < ClientGenerator.ClustersArray[(cache-1)])
			return friends_your_cache;
		else
			return ClientGenerator.ClustersArray[(cache-1)]-1;
	}

	private int friendsByDistribution(int distribucion, int usuarios_totales) {
		int friends_num=0;
		if (distribucion==1){
			double rand = Math.random();
			double d = rand * usuarios_totales;
			friends_num = (int)d;
			return friends_num;
		}
		else if (distribucion==2){
			int[] distribution = {5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,8,8,8,8,8,8,8,8,8,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,14,14,14,14,14,14,14,14,14,14,14,14,14,15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,16,16,16,16,16,16,16,16,16,16,16,17,17,17,17,17,17,17,17,17,17,17,17,17,17,17,17,17,17,17,17,17,17,17,18,18,18,18,18,18,18,18,18,18,18,19,19,19,19,19,19,19,19,20,20,20,20,20,20,20,21,21,21,21,21,21,21,21,21,21,21,21,21,21,21,22,22,22,22,22,22,22,22,23,23,23,23,23,23,23,24,24,24,24,24,24,24,24,24,24,24,24,24,24,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,26,26,26,26,26,26,26,26,26,26,26,26,26,27,27,27,27,27,27,27,27,27,27,27,28,28,28,28,28,28,28,28,28,28,28,28,28,28,29,29,29,29,29,29,29,29,29,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,31,31,31,31,31,31,31,31,31,31,31,31,32,32,32,32,32,32,32,32,32,33,33,33,33,33,33,33,33,33,33,33,33,34,34,34,34,34,34,34,34,34,34,35,35,35,35,35,35,35,35,35,35,36,36,36,36,36,37,37,37,37,37,37,37,37,37,37,37,37,38,38,38,38,38,38,39,39,39,39,39,39,39,39,39,40,40,40,40,40,40,40,40,40,40,41,41,41,41,42,42,42,42,42,42,42,42,42,42,42,42,42,43,43,43,43,43,43,43,43,43,43,44,44,44,44,44,44,44,44,45,45,45,45,45,45,45,46,46,46,46,46,46,47,47,47,47,47,47,47,47,47,48,48,48,48,48,48,48,48,48,49,49,49,49,49,49,49,49,50,50,50,50,50,50,50,51,51,51,51,51,51,51,51,51,52,52,52,52,52,52,52,53,53,53,53,53,53,53,53,53,53,53,53,54,54,54,54,55,55,55,55,55,55,55,55,55,55,56,56,56,56,57,57,57,57,57,58,58,58,58,58,59,59,59,59,60,60,60,60,60,60,60,60,61,61,61,62,62,62,62,62,62,62,63,63,63,63,63,64,64,64,65,65,65,65,65,65,66,66,66,67,67,68,68,69,69,69,69,69,70,70,70,70,71,71,71,72,72,72,73,73,73,73,73,73,73,73,74,74,74,74,74,75,75,75,76,77,77,77,77,77,78,78,78,78,78,79,79,79,79,79,80,80,80,80,80,81,81,81,82,82,82,83,83,85,86,87,87,87,87,88,88,88,89,89,89,89,89,89,90,90,90,90,90,90,90,91,92,92,92,92,93,95,95,95,95,96,96,97,97,97,97,99,99,99,99,101,102,102,103,103,104,105,106,106,106,107,107,108,109,109,110,111,112,113,113,114,114,115,116,117,117,120,120,120,121,121,123,123,123,123,124,124,125,125,125,125,126,126,126,126,128,128,128,128,129,129,130,130,131,132,132,134,135,137,138,139,140,140,142,143,144,146,147,150,150,153,153,154,155,155,155,156,157,158,160,161,162,163,164,164,165,167,167,168,169,169,172,173,174,174,176,180,181,182,183,183,184,185,185,185,186,192,197,197,199,199,201,202,207,208,209,212,212,217,218,221,223,226,226,226,228,231,234,234,235,241,241,241,243,247,249,250,256,257,259,265,265,268,272,273,274,274,279,293,308,308,316,316,319,335,343,345,352,356,363,367,375,376,381,383,384,393,403,413,413,421,421,423,425,435,437,443,445,447,457,458,462,468,471,477,485,485,489,491,492,495,495,495,496,497,500,500,500};
			double rand = Math.random();
			double d = rand * 1000;
			int index = (int)d;
			friends_num = distribution[index];
			return friends_num;
		}
		
		else
			return friends_num;
	}
	
	static void shuffleArray(int[] arr)
	  {
	    Random rnd = new Random();
	    for (int i = arr.length - 1; i > 0; i--)
	    {
	      int index = rnd.nextInt(i + 1);
	      // Simple swap
	      int a = arr[index];
	      arr[index] = arr[i];
	      arr[i] = a;
	    }
	  }

	public static String randomIdentifier(int max_length) {
		final String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";
		final java.util.Random rand = new java.util.Random();
		// consider using a Map<String,Boolean> to say whether the identifier is being used or not 
		final Set<String> identifiers = new HashSet<String>();
			StringBuilder builder = new StringBuilder();
		    while(builder.toString().length() == 0) {
		    	double randNumber = Math.random();
				double d1 = randNumber * max_length;
				int length = (int)d1;
		        for(int i = 0; i < length; i++)
		            builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
		        if(identifiers.contains(builder.toString())) 
		            builder = new StringBuilder();
		    }
		    return builder.toString();
	}
} 