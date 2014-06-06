package client;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;
 
public class RelationsThread extends Thread{

	static String Server_host;
	static String Cache_host;

	public void run() {
		int[] intArray = null;
		int new_friends = 0;
		int friendships_procesadas=0;
		int User_id = ClientGenerator.usuarios_procesados+1;
		ClientGenerator.usuarios_procesados=ClientGenerator.usuarios_procesados+1;
		Socket socket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		
		try {
			socket = new Socket(Server_host, 55555);
 
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
 
			String fromServer;
			String fromUser = null;
			
			while ((fromServer = in.readLine()) != null) {
				System.out.println("Server - " + fromServer);
				sleep(1000);
				if (fromServer.equals("exit"))
					break;
				if (fromServer.startsWith("Followers_set")){
					break;
				}
				if (fromServer.startsWith("......")){
			
					new_friends = friendsByDistribution(ClientGenerator.distribucion);
					intArray = new int[new_friends];
					int[] arr = new int[ClientGenerator.usuarios];
					
					for (int i = 0; i < arr.length; i++) {
				        arr[i] = i+1;
				    }
					shuffleArray(arr);
					boolean esta = false;
					int pos = 0;
					for (int i = 0; i < new_friends; i++) {
						if (arr[i]==User_id)
						{
							esta = true;
							pos = i;
						}
						else
							intArray[i] = arr[i];
				    }
					if (esta)
						intArray[pos]=arr[new_friends+1];
					
					
					fromUser ="insertfriendship," + User_id + "," + intArray[friendships_procesadas];
					friendships_procesadas=friendships_procesadas+1;
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
						fromUser ="insertfollowersbycluster," + ClientGenerator.usuarios_procesados + "," + new_friends + "," + ClientGenerator.cluster;
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
	
	private int friendsByDistribution(int distribucion) {
		if (distribucion==1){
			double rand = Math.random();
			double d = rand * ClientGenerator.usuarios;
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