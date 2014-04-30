package edu.upenn.cis.cis555.youtube;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ListeningThread implements Runnable{
	ServerSocket connectionSocket;
	private static int listeningPortNo;
	Thread t;
	HttpServer callingServer;
	//public boolean threadFlag;
	
	ListeningThread(){}
	
	public ListeningThread(HttpServer s)
	{
		callingServer = s;
		//threadFlag = s.getThreadFlag();
	}
	
	public void run()
	{
		//if(callingServer.getThreadFlag() == true)
//		{
		System.out.println("in run method of ListeningThread.java");
			Socket clientSocket = null;
			connectionSocket = null;
			listeningPortNo = callingServer.portNo;
		
			try
			{
				connectionSocket = new ServerSocket(listeningPortNo);
				System.out.println("Now listening on: "+listeningPortNo);
			}
			catch(IOException ioe)
			{
				System.err.println("IOException in run() of ListeningThread: "+ioe.getMessage());
				ioe.printStackTrace();
			}
		
			while (true) {	
				//System.out.println("flag: "+callingServer.getThreadFlag()+ "in while of listeningthread...1");
//				if(callingServer.getThreadFlag() ==false)
//				{
//					System.out.println("in if loop of while(true)");
//					//callingServer.emptyEnqueue();
//					for(Thread t : callingServer.getThreadList())
//					{
//						t.interrupt();
//					}
//					Thread.currentThread().interrupt();
//					break;
//				}
//				else
				{
				//if(callingServer.getThreadFlag() != false)
				//{
//			if(callingServer.getThreadFlag() == false)
//			{
//				System.out.println("Thread Flag: "+callingServer.getThreadFlag());
//				try {
//					connectionSocket.close();
//					callingServer.enqueue.size();
//					
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
////				int size =  callingServer.getThreadList().size();
////				for(int j=0; j<=size ; j++)
////				{
////					Thread t = callingServer.getThreadList().remove(j);
////					System.out.println("THREAD STOP SERVER: "+t.getName());
////					t.interrupt();
////				}
//				for(Thread t: callingServer.getThreadList())
//				{
//					System.out.println("Listening thread loop: "+t.getName());
//					t.interrupt();
//				}
//				System.out.println("BREAK from while in Listening Thread");
//				break;
//			}
				
			
					if(callingServer.enqueue.size()<=callingServer.MAXQUEUE){
					//	System.out.println("In callingServer.enqueue.size()<=callingServer.MAXQUEUE: "+callingServer.getThreadFlag());
						
						try{	
//							if(callingServer.getThreadFlag() == true)
//							{
							//	System.out.println("Accepting a connection: Listening Thread"); 
								connectionSocket.setSoTimeout(500);
								clientSocket = connectionSocket.accept();	
								//connectionSocket.setSoTimeout(100);
								callingServer.putSocket(clientSocket,listeningPortNo);
							//	System.out.println("Accepted a connection");
							//}
						}
						catch(Exception e){
							if(callingServer.getThreadFlag() ==false)
							{
								System.out.println("in if loop of while(true)");
								callingServer.emptyEnqueue();
								for(Thread t : callingServer.getThreadList())
								{
									t.interrupt();
								}
								Thread.currentThread().interrupt();
								break;
							}
							//e.printStackTrace();
							//System.out.println("Cannot accept more connections");
						}
					}
				}
//			else
//			{
//				break;
//			}
//		}
			
			}
	}
}