/*package edu.upenn.cis.cis555.youtube;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;


import rice.environment.Environment;
import rice.p2p.commonapi.Application;

public class P2PCache {
	int bindPort;
	InetAddress bootAddress;
	int bootStrapPort;
	int daemonPort;
	DaemonThread listenDaemonThread;
	NodeSetup ndSetup;
	YouTubeClient ytClient;
	String BDBpath;
	String storeNumber;
	int storageSize;
	int maxStorageTime;
	public final int NO_OF_THREADS = 8;
	public final int MAXQUEUE = 50000;
	public TestWorkerNode[] worker = new TestWorkerNode[NO_OF_THREADS];
	public Vector<SocketInfoNode> enqueue = new Vector<SocketInfoNode>();
	
	*//** PLAN
	 * in DaemonThread class use callingP2PCache.ndSetUp.getId.
	 * Route it to that node by using callingP2PCache.ndSetUp.app.sendMessage
	 * Receive it
	 *//*
	
	public P2PCache(int bindport, InetAddress bootAddress, int bootStrapPort,int daemonPort, String BDBpath, String storeNumber, int storageSize, int maxStorageTime) {
		this.bindPort = bindport;
		this.bootAddress = bootAddress;
		this.bootStrapPort = bootStrapPort;
		this.daemonPort = daemonPort;
		this.BDBpath = BDBpath;
		this.storeNumber = storeNumber;
		this.storageSize = storageSize;
		this.maxStorageTime = maxStorageTime;
		
		ytClient = new YouTubeClient(BDBpath);

		
		 * Setting up the node
		 
		setUpNode(this);
		
		
		 * Starting Worker Threads
		 
		for(int i = 0; i < NO_OF_THREADS; i++)
		{
			worker[i] = new TestWorkerNode(this,ndSetup,ytClient); 
			worker[i].t = new Thread(worker[i]);
			//Threads.add(worker[i].t);
			worker[i].t.start();
		}
		
		*//**
		 * Starting Daemon Thread
		 *//*
		run();
	}

	private void setUpNode(P2PCache cache) {
		// Loads pastry settings
		Environment env = new Environment();
		env.getParameters().setString("pastry_socket_allow_loopback","true" );
		try {
			InetSocketAddress bootAddr = new InetSocketAddress(bootAddress,bootStrapPort);
			ndSetup = new NodeSetup(cache,bindPort, bootAddr, env ,ytClient, BDBpath, storeNumber, storageSize, maxStorageTime);
		} catch (Exception e) {
			// remind user how to use
			System.out.println("Usage:");
			System.out.println("java [-cp FreePastry-<version>.jar] rice.tutorial.lesson1.DistTutorial localbindport bootIP bootPort");
			System.out.println("example java rice.tutorial.DistTutorial 9001 pokey.cs.almamater.edu 9001");
			 e.printStackTrace();
		}
	}
		
	private void run() {
		listenDaemonThread = new DaemonThread(this);
		listenDaemonThread.t = new Thread(listenDaemonThread);
		listenDaemonThread.t.setName("Listening Thread");
		listenDaemonThread.t.start();
		
		try
		{
			listenDaemonThread.t.join();
		}
		catch(InterruptedException ie)
		{
			ie.printStackTrace();
		}

	}

	public synchronized void putSocket(Socket s, int portNo) {
		SocketInfoNode si= new SocketInfoNode(s,portNo);
		enqueue.add(si);
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
		int storageSize;
		int maxStorageTime;
		
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
			storageSize = Integer.parseInt(cmdLineArgs.remove(0));
			maxStorageTime = Integer.parseInt(cmdLineArgs.remove(0));
			new P2PCache(bindport, bootAddress, bootStrapPort, daemonPort, BDBpath, storeNumber, storageSize, maxStorageTime);
	}
}
*/