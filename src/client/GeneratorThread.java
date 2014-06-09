package client;

import java.io.*;
import java.net.*;
import java.util.HashSet;
import java.util.Set;
 
public class GeneratorThread extends Thread{

	static String Server_host;
	static String Cache_host;
	
	public void run() {
		Socket socket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		int User_pos = ClientGenerator.usuarios_procesados;
		ClientGenerator.usuarios_procesados=ClientGenerator.usuarios_procesados+1;
		
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
				if (fromServer.startsWith("inserted,")){
					String[] peticion = fromServer.split(",", 2);
					int client_id = Integer.parseInt(peticion[1]);
					ClientGenerator.ClientsArray[User_pos]=client_id;
					break;
				}
				if (fromServer.startsWith("......")){
					fromUser = "insertuser,"+randomIdentifier(15)+","+ClientGenerator.cluster;
					if (fromUser != null) {
						System.out.println("Client - " + fromUser);
						synchronized (socket){
							out.println(fromUser);
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