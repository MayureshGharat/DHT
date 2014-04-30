/*package edu.upenn.cis.cis555.youtube;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

import rice.environment.Environment;

public class SearchEngine {
	int bindPort;
	InetAddress bootAddress;
	int bootStrapPort;
	int daemonPort;
	ListeningDaemonThread daemonThread;
	NodeSetup ndSetup;
	//YouTubeClient ytClient;
	String BDBpath;
	String storeNumber;
	public final int NO_OF_THREADS = 8;
	public final int MAXQUEUE = 50000;
	public EngineWorker[] worker = new EngineWorker[NO_OF_THREADS];
	public Vector<SocketInfoNode> enqueue = new Vector<SocketInfoNode>();
	
	*//**
	 * 
	 * @param bindport = Binding Port of the node
	 * @param bootAddress = IP address of the machine 
	 * @param bootStrapPort = Boot node port
	 * @param daemonPort = Listening port for Daemon Thread 
	 * @param bDBpath = DB path
	 * @param storeNumber = Use to identify each store uniquely in the file Sytem
	 *//*
	public SearchEngine(int bindport, InetAddress bootAddress,int bootStrapPort, int daemonPort, String bDBpath,String storeNumber) {
		this.bindPort = bindport;
		this.bootAddress = bootAddress;
		this.bootStrapPort = bootStrapPort;
		this.daemonPort = daemonPort;
		this.BDBpath = bDBpath;
		this.storeNumber = storeNumber;
		System.out.println("BDBpath in SearchEngine Constructor: "+BDBpath);
		
		 * Setting up the node
		 
		setUpNode(this);
	
		
		 * Starting Worker Threads
		 
		for(int i = 0; i < NO_OF_THREADS; i++)
		{
			worker[i] = new EngineWorker(this, ndSetup); 
			worker[i].t = new Thread(worker[i]);
			worker[i].t.start();
		}
		
		*//**
		 * Starting Daemon Thread
		 *//*
		run();
	}

		
	private void run() {
		daemonThread = new ListeningDaemonThread(this);
		daemonThread.t = new Thread(daemonThread);
		daemonThread.t.setName("Listening Thread");
		daemonThread.t.start();
		
		try
		{
			daemonThread.t.join();
		}
		catch(InterruptedException ie)
		{
			ie.printStackTrace();
		}

	}


	private void setUpNode(SearchEngine searchEngine) {
		// Loads pastry settings
		Environment env = new Environment();
		env.getParameters().setString("pastry_socket_allow_loopback","true" );
		try {
			InetSocketAddress bootAddr = new InetSocketAddress(bootAddress,bootStrapPort);
			ndSetup = new NodeSetup(searchEngine,bindPort, bootAddr, env , BDBpath, storeNumber);
		} catch (Exception e) {
			// remind user how to use
			System.out.println("Usage:");
			System.out.println("java [-cp FreePastry-<version>.jar] rice.tutorial.lesson1.DistTutorial localbindport bootIP bootPort");
			System.out.println("example java rice.tutorial.DistTutorial 9001 pokey.cs.almamater.edu 9001");
			 e.printStackTrace();
		}
		
	}

	public synchronized void putSocket(Socket s){//, int portNo) {
		SocketInfoNode si= new SocketInfoNode(s);//,portNo);
		enqueue.add(si);
		notifyAll();
	}
	
	public synchronized void putSocketInfoNode(SocketInfoNode sockInfoNode)
	{
		enqueue.add(sockInfoNode);
		notifyAll();
	}
	
	
	public synchronized SocketInfoNode getSocket() //throws InterruptedException 
	{
		notifyAll();
		SocketInfoNode si= new SocketInfoNode();
		try
		{
			while (enqueue.size() == 0)
			{
				wait();
			}
			si =  enqueue.firstElement();
			enqueue.removeElement(si);
			return si;
		}
		catch(InterruptedException excp)
		{
			return null;	
		}
	}
	
	
	
	
	
	public static void main(String[] args) {
		Vector<String> cmdLineArgs = new Vector<String>();
		int bindport = 0;
		int daemonPort = 0;
		InetAddress bootAddress = null;
		int bootStrapPort;
		String BDBpath;
		String storeNumber;
		
			System.out.println("Mayuresh R. Gharat");
			System.out.println("SEAS LOGIN: mayuresh");
			for (String temp : args) {
				cmdLineArgs.add(temp);
			}
			storeNumber = cmdLineArgs.remove(0);
			bindport = Integer.parseInt(storeNumber);
			try {
				bootAddress = InetAddress.getByName(cmdLineArgs.remove(0));
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			bootStrapPort = Integer.parseInt(cmdLineArgs.remove(0));
			daemonPort = Integer.parseInt(cmdLineArgs.remove(0));
			BDBpath = cmdLineArgs.remove(0);
			System.out.println("BDBpath in main: "+BDBpath);
			new SearchEngine(bindport, bootAddress, bootStrapPort, daemonPort, BDBpath, storeNumber);
	}

}
*/