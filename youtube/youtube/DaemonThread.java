/*package edu.upenn.cis.cis555.youtube;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


public class DaemonThread implements Runnable{
	ServerSocket connectionSocket;
	private static int listeningPortNo;
	Thread t;
	P2PCache callingP2PCache;

	DaemonThread(){}

	public DaemonThread(P2PCache cache)
	{
		callingP2PCache = cache;
	}

	public void run()
	{
		Socket clientSocket = null;
		connectionSocket = null;
		listeningPortNo = callingP2PCache.daemonPort;

		try
		{
			connectionSocket = new ServerSocket(listeningPortNo);
			System.out.println("Daemon Thread listening on: "+listeningPortNo);
		}
		catch(IOException ioe)
		{
			System.err.println("IOException in run() of ListeningThread: "+ioe.getMessage());
			ioe.printStackTrace();
		}

		while (true) 
		{
			if(callingP2PCache.enqueue.size()<=callingP2PCache.MAXQUEUE){
			try{	
				clientSocket = connectionSocket.accept();
				callingP2PCache.putSocket(clientSocket,listeningPortNo);				
				System.out.println("ACCEPTED A CONNECTION:"+clientSocket.getLocalPort());
			}
			catch(Exception e){
				e.printStackTrace();
				System.out.println("Cannot accept more connections");
			}
		}
		}
	}
}
*/