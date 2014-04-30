/*package edu.upenn.cis.cis555.youtube;

import java.net.ServerSocket;
import java.net.Socket;

public class ListeningDaemonThread implements Runnable {

	Thread t;
	SearchEngine sEngine;
	private int listeningPortNo;
	ServerSocket connectionSocket;
	
	public ListeningDaemonThread(SearchEngine searchEngine) {
		sEngine = searchEngine;
	}

	@Override
	public void run() {
		
		Socket clientSocket = null;
		connectionSocket = null;
		listeningPortNo = sEngine.daemonPort;
		
		try
		{
			connectionSocket = new ServerSocket(listeningPortNo);
			System.out.println("Daemon Thread listening on port: "+listeningPortNo);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		while(true)
		{
			try
			{
				if(sEngine.enqueue.size() <= sEngine.MAXQUEUE)
				{
					clientSocket = connectionSocket.accept();
					sEngine.putSocket(clientSocket);
					System.out.println("Accepted a connection");
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

}
*/