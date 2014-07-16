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
		friends_your_cache = (int)(new_friends*60)/100;
		if (friends_your_cache < ClientGenerator.ClustersArray[(cache-1)])
			return friends_your_cache;
		else
			return ClientGenerator.ClustersArray[(cache-1)]-1;
	}

	private int friendsByDistribution(int distribucion, int usuarios_totales) {
		if (distribucion==1){
			double rand = Math.random();
			double d = rand * usuarios_totales;
			int friends_num = (int)d;
			return friends_num;
		}
		else
			return 0;
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