/*package edu.upenn.cis.cis555.youtube;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import rice.p2p.commonapi.Application;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.IdFactory;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.Node;
import rice.p2p.commonapi.RouteMessage;
import rice.environment.Environment;
import rice.pastry.NodeHandle;
import rice.pastry.Id;
import rice.pastry.NodeIdFactory;
import rice.pastry.PastryNode;
import rice.pastry.PastryNodeFactory;
import rice.pastry.socket.SocketPastryNodeFactory;
import rice.pastry.standard.RandomNodeIdFactory;


public class YouTubeApp implements Application{

	Node node;
	Endpoint endpoint;
	Hashtable<String,String> DHT = new Hashtable<String,String>();
	YouTubeClient callingYtClient;
	P2PCache callingP2PCache;
	//boolean senderflag = false;
	//boolean selfFlag;
	String xmlResponse = "";
	Hashtable<String,String> hashCache = new Hashtable<String, String>();
	boolean responseSetFlag = false;
	String BDBpath;
	String storeNumber;
	StoreNodeWrapper storeWrapper;
	public HashMap<String,ArrayList<SocketInfoResponse>> pendingSearchRequests;
	//int maxStorageSize;
	//int maxStorageTime;
	
	public YouTubeApp(P2PCache cache, PastryNode node,YouTubeClient ytClient, String bDBpath, String storeNumber) {
		callingP2PCache = cache;
		this.node =node;
		this.BDBpath = BDBpath;
		this.storeNumber = storeNumber;
		this.endpoint = node.buildEndpoint(this, "YouTube App");
		this.endpoint.register();
	}

	public synchronized void requestVideoSearch(String keyword , SocketInfoResponse sri){
		//Construct KeywordSearchMsg, route it, put entry in pending Search requests
		addPendingRequest(keyword,sri);
		IdFactory idFactory = node.getIdFactory();
		Id keywordId = (Id) idFactory.buildId(keyword);
		//OurMessage m = new OurMessage(node.getLocalNodeHandle(),keyword,node.getId());
		KeywordMsg ksMsg = new KeywordMsg(keyword,node.getLocalNodeHandle(), node.getId());
		endpoint.route(keywordId, ksMsg, null);
	}
	
	public void addPendingRequest(String key, SocketInfoResponse val){
		if(pendingSearchRequests.containsKey(key)){
			ArrayList<SocketInfoResponse> tempList = pendingSearchRequests.get(key);
			tempList.add(val);
			pendingSearchRequests.put(key, tempList);
		}else{
			ArrayList<SocketInfoResponse> tempList = new ArrayList<SocketInfoResponse>();
			tempList.add(val);
			pendingSearchRequests.put(key, tempList);
		}
	}
	
	

	@Override
	public void deliver(rice.p2p.commonapi.Id arg0, Message arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean forward(RouteMessage arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void update(rice.p2p.commonapi.NodeHandle arg0, boolean arg1) {
		// TODO Auto-generated method stub
		
	}

	//NodeFactory nodeFactory;
	Node node;
	Endpoint endpoint;
	Hashtable<String,String> DHT = new Hashtable<String,String>();
	YouTubeClient callingYtClient;
	P2PCache callingP2PCache;
	boolean senderflag = false;
	boolean selfFlag;
	String xmlResponse = "";
	Hashtable<String,String> hashCache = new Hashtable<String, String>();
	boolean responseSetFlag = false;
	String BDBpath;
	String storeNumber;
	StoreNodeWrapper storeWrapper;
	int maxStorageSize;
	int maxStorageTime;
	
	YouTubeApp(){}
	
	public YouTubeApp(P2PCache cache,Node node,YouTubeClient ytClient, String BDBpath, String storeNumber) //int maxStorageSize, int maxStorageTime)
	{
		callingP2PCache = cache;
		this.node =node;
		this.BDBpath = BDBpath;
		this.storeNumber = storeNumber;
		this.endpoint = node.buildEndpoint(this, "YouTube App");
		this.endpoint.register();
		//this.maxStorageSize = maxStorageSize;
		//this.maxStorageTime = maxStorageTime;
	}
	
	@Override
	public void deliver(Id id, Message message) {
		
		 * if Flag == false
		 * Call the You tube client initializeParam 
		 * Call Search Videos
		 * 
		 
		OurMessage om = (OurMessage)message;
		{
			if(this.senderflag == false)
			{
				//callingYtClient.initializeParam(this, storeNumber, maxStorageSize, maxStorageSize);//storeWrapper);
				String xmlString = callingYtClient.searchVideos(om.content);
				System.out.println("RECIEVED "+om.content+" from "+om.from);
				if(om.wantResponse)
				{
					//String xmlString = callingYtClient.searchVideos(om.content);
					OurMessage reply = new OurMessage(node.getLocalNodeHandle(),xmlString,node.getId());
					System.out.println("Sending"+xmlString+ " to: "+om.from);
					reply.wantResponse = false;
					//this.senderflag = true;
					endpoint.route(null, reply, om.from);
				}
			}
			else if(this.senderflag== true)
			{
			
			 if Flag == true 
			 * Make flag = false
			 * 
			  
				System.out.println("RECIEVED "+om.content+" from "+om.from);
				if(node.getLocalNodeHandle() == om.from)
				{
					System.out.println("YOU PINGED YOURSELF");
					this.senderflag = false;
				//	callingYtClient.initializeParam(this, storeNumber,maxStorageSize, maxStorageTime);//storeWrapper);
					String xmlString = callingYtClient.searchVideos(om.content);
					this.xmlResponse = xmlString;
					String[] respArray = xmlString.split("~", 2);
					hashCache.put(respArray[0], respArray[1]);
					System.out.println("xmlString in MyApp:::: "+this.xmlResponse);
					this.responseSetFlag = true;
				}
				else
				{
					this.xmlResponse = om.content;
					String[] respArray = xmlResponse.split("~", 2);
					hashCache.put(respArray[0], respArray[1]);
					System.out.println("xmlString in MyApp: "+this.xmlResponse);
					this.senderflag = false;
					this.responseSetFlag = true;
				}
				
			}
		}
	}
	
	public void sendMessage(Id idToSendTo, String msgToSend)
	{
		{
			OurMessage m = new OurMessage(node.getLocalNodeHandle(),msgToSend,node.getId());
			System.out.println("Sending Keyword: "+msgToSend+" to "+idToSendTo);
			this.senderflag =true;
			endpoint.route(idToSendTo, m, null);
		}
	}

	@Override
	public boolean forward(RouteMessage arg0) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public void update(NodeHandle arg0, boolean arg1) {
		// TODO Auto-generated method stub
	}
	
	
	
	//**************************************************************************************************************************
	
	*//**
	 * New Change
	 *//*
	
	public synchronized void requestVideoSearch(String keyword , SocketInfoResponse sri){
		//Construct KeywordSearchMsg, route it, put entry in pending Search requests
		addPendingRequest(keyword,sri);
		
		IdFactory idFactory = node.getIdFactory();
		Id keywordId = idFactory.buildId(keyword);
		KeywordSearchMsg ksMsg = new KeywordSearchMsg(keyword,node.getLocalNodeHandle());
		endpoint.route(keywordId, ksMsg, null);
	}
	
	


}
*/