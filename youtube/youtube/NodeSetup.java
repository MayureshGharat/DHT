/*package edu.upenn.cis.cis555.youtube;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.TimerTask;

import rice.p2p.commonapi.Node;
import rice.environment.Environment;
import rice.pastry.NodeHandle;
import rice.pastry.Id;
import rice.pastry.NodeIdFactory;
import rice.pastry.PastryNode;
import rice.pastry.PastryNodeFactory;
import rice.pastry.socket.SocketPastryNodeFactory;
import rice.pastry.standard.RandomNodeIdFactory;

public class NodeSetup {

	PingPong callingPingPong;
	SearchEngine sEngine;
	Environment env1;
	NodeIdFactory nidFactory;
	NodeIdFactory nidFactory1;
	String BDBpath;
	String storeNumber;
	int maxStorageSize;
	int maxStorageTime;
	YouTubeApp ytApp;
	SearchEngineApp seApp;
	
	public NodeSetup(SearchEngine searchEngine, int bindPort,InetSocketAddress bootAddr, Environment env, String BDBpath,String storeNumber) throws Exception {
		
		this.sEngine = searchEngine;
		this.env1 = env;
		this.BDBpath = BDBpath;
		this.storeNumber = storeNumber;
	 	nidFactory = new RandomNodeIdFactory(env1);
	 	  System.out.println("bdbPATH IN nODEsETUP: "+BDBpath);
	 	// construct the PastryNodeFactory, this is how we use rice.pastry.socket
	 	 PastryNodeFactory factory = new SocketPastryNodeFactory(nidFactory, bindPort, env1);
	 	 
	 	// construct a node, but this does not cause it to boot
	 	 PastryNode node = factory.newNode();
	 	 
		//construct a new PingPong
	//	callingPingPong = new PingPong(node);
	
		*//**
		 * Create an APP
		 *//*
		//construct a new YouTubeApp
	//	ytApp  = new YouTubeApp(callingP2PCache, node, ytClient, BDBpath, storeNumber);//maxStorageSize, maxStorageTime);
		seApp = new SearchEngineApp(node, searchEngine, BDBpath, storeNumber);
	    // in later tutorials, we will register applications before calling boot
	 	node.boot(bootAddr);
	 	
	 	 synchronized(node) {
	 		while(!node.isReady() && !node.joinFailed()) {
	 			// delay so we don't busy-wait
	 				node.wait(500);
	 	   
	 			// abort if can't join
	 				if (node.joinFailed()) {
	 					throw new IOException("Could not join the FreePastry ring.  Reason:"+node.joinFailedReason());
	 				}
	 		}       
	 	 }
	 	System.out.println("Finished creating new node "+node);
	 	final Timer t = new Timer();
	 	t.schedule(new TimerTask() {
	 	public void run() {
		    runPingPong();
	 	}
	 	},1000, 3*1000);
	}
	
	public SearchEngineApp getSearchEngineApp()
	{
		return seApp;
	}
	

	public void runPingPong()
	{
		//while(true)
		{
	      Id randId = nidFactory.generateNodeId();
	      
	      // send to that key
	      callingPingPong.sendMessage(randId, "PING");
		}
	}
	
	
	public Id getIdFromBytes(byte[] material) {
		return Id.build(material);
	}
}
*/