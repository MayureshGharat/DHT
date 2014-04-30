/*package edu.upenn.cis.cis555.youtube;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import rice.p2p.commonapi.Application;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.IdFactory;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.Node;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.RouteMessage;

public class SearchEngineApp implements Application {

	Node node;
	Endpoint endpoint;
	SearchEngine seEngine;
	String BDBpath;
	String storeNumber;
	//HashMap<String, String> 
	
	
	 * DataStructure to hold the pending Request
	 
	//public HashMap<String,ArrayList<SocketInfoResponse>> pendingSearchRequests;
	
//	public HashMap<UUID,SocketInfoResponse> pendingRequests;// = new HashMap<UUID,SocketInfoResponse>();

	public HashMap<UUID,SocketInfoNodeResponse> pendingRequests;
	
	public SearchEngineApp(Node node, SearchEngine sEngine, String dbPath, String storeNumber)
	{
		this.seEngine = sEngine;
		this.node = node;
		this.BDBpath = dbPath;
		this.storeNumber = storeNumber;
		this.endpoint = node.buildEndpoint(this, "SearchEngineApp");
		this.endpoint.register();
		
//		pendingSearchRequests = new HashMap<String,ArrayList<SocketInfoResponse>>();	
		//pendingRequests = new HashMap<UUID, SocketInfoResponse>();
		
		pendingRequests = new HashMap<UUID, SocketInfoNodeResponse>();
	}
	
	private void addPendingRequest(String keyword, SocketInfoResponse SocketInfoNodeResponse sri) {
		if(pendingSearchRequests.containsKey(keyword))
		{
			//ArrayList<SocketInfoResponse> tempList = pendingSearchRequests.get(keyword);
			

			ArrayList<SocketInfoNodeResponse> tempList = new ArrayList<SocketInfoNodeResponse>();
			tempList.add(sri);
			pendingSearchRequests.put(keyword, tempList);
		}
		else
		{
		//	ArrayList<SocketInfoResponse> tempList = new ArrayList<SocketInfoResponse>();

			ArrayList<SocketInfoNodeResponse> tempList = new ArrayList<SocketInfoNodeResponse>();
		
			tempList.add(sri);
			pendingSearchRequests.put(keyword, tempList);
		}	
	}
	
	*//**
	 * 
	 * Maintain a requestId to SocketInfoResponse mapping
	 * 
	 *//*
	private void addPendingRequestNew(UUID reqId, SocketInfoResponse SocketInfoNodeResponse sri) {
		if(pendingRequests.containsKey(reqId))
		{
			//ArrayList<SocketInfoResponse> tempList = pendingSearchRequests.get(keyword);
			//tempList.add(sri);
			//pendingSearchRequests.put(keyword, tempList);
			
		}
		else
		{
			ArrayList<SocketInfoResponse> tempList = new ArrayList<SocketInfoResponse>();
			tempList.add(sri);
			pendingSearchRequests.put(keyword, tempList);
		}
		System.out.println("In addPendingRequestNew...");
		pendingRequests.put(reqId, sri);
	}
	
	
	@Override
	public void deliver(Id nodeId, Message msg)  {
		*//**
		 * Check the kind of message "Keyword" or the "Result"
		 *//*
		
		System.out.println("In deliver");
		if(msg instanceof KeywordMsg)
		{
			KeywordMsg keyMsg = (KeywordMsg)msg; 
			String keyword = keyMsg.getKeyword();
			NodeHandle senderNodeHandle = keyMsg.getSenderNodeHandle();
			UUID requestId = keyMsg.getRequestId();
			
			System.out.println("RECEIVED KEYWORD REQUEST "+keyword);
			
			
			
			//**************************************************************
			*//**
			 * Get the response from the database
			 *//*
			//String  = "";//Get the token
			
			StoreNodeWrapper swrapper = new StoreNodeWrapper(BDBpath, storeNumber);
			IndexEntry iEntry = swrapper.getIndexEntry(keyword);
			ArrayList<Posting> postingList  = iEntry.getPostingList(); //Get the Posting list
			double idf = iEntry.getIdf(); // Get the Idf for the corresponding Index Entry
			
			SearchResponseMsg searchResultMsg = new SearchResponseMsg(keyword, postingList, requestId, idf);
			System.out.println("Sending Result to: "+senderNodeHandle);
			endpoint.route(null, searchResultMsg, senderNodeHandle);			
			
			
			
			
			
			
			//**************************************************************
			
			
			*//**
			 * Get the result of the response from the database
			 *//*
			//String response = youtubeClient.searchVideos(keyword);
			StoreNodeWrapper swrapper = new StoreNodeWrapper(BDBpath, storeNumber);
			System.out.println("bdbPATH: "+BDBpath+" StoreNUMBER: "+storeNumber);
			
			*//**
			 * Getting all the urls for the given keyWord
			 *//*
			HashSet<String> urlSet = swrapper.getCacheEntry(keyword).getUrls();
			
			*//**
			 * Get the Url to TfIdf map for the keyword
			 *//*
			HashMap<String, Long> urlTfIdf = swrapper.getCacheEntry(keyword).getTfIdfMap();
			
			StringBuilder sbResponse = new StringBuilder();
			if(urlSet.size() > 0)
			{
				for(String url :  urlSet)
				{
					sbResponse.append(url);
					sbResponse.append("&~&");
				}
			}
			
			
			KeywordMsgResult resultMsg = new KeywordMsgResult(keyword,sbResponse.toString(), requestId, urlTfIdf);
			System.out.println("Sending Result to: "+senderNodeHandle);
			endpoint.route(null, resultMsg, senderNodeHandle);
		}
		//***********************************************************
		else if(msg instanceof SearchResponseMsg)
		{
			SearchResponseMsg searchresultMsg  = (SearchResponseMsg) msg;
			String keyword = searchresultMsg.getKeyword();
			UUID requestId = searchresultMsg.getRequestId();
			HashMap<String, Double> urlTfIdf = searchresultMsg.getTfIdfMap();
			ArrayList<String> urlList  = searchresultMsg.getUrlList();
			ArrayList<Posting> postingList = searchresultMsg.getPostingList();
			
			System.out.println("Request Id in deliver: "+requestId);
			System.out.println("RECEIVED RESULTS FOR "+keyword + "=> "+ urlList);
			System.out.println("Boolean: "+pendingRequests.containsKey(requestId));
			
			
			if(pendingRequests.containsKey(requestId))
			{
				System.out.println("Pending Request contains the key");
				SocketInfoNodeResponse sri = this.pendingRequests.get(requestId);
				sri.setkeyWordUrlMap(keyword, urlList);
			    *//**
			     * Set the Url <-> Posting 
			     *//*
				sri.setUrlPostingMap(postingList);
				
				*//**
				 * Get the url to TfIdfs for each url  and populate url<->totalTfIdfs in SocketResponseInfo Class
				 *//*
				for(Map.Entry mEntry : urlTfIdf.entrySet())
				{
					sri.setTotalTfIdf((String)mEntry.getKey(), (Double)mEntry.getValue());
				}
				
				*//**
				 * Check whether result for all keywords is recieved
				 *//*
				if(sri.numberOfKeywords == sri.keywordUrlMap.size())
				{
					pendingRequests.remove(requestId);
					HashSet<String> urlSet = sri.getAllResponseUrls();
			     	StoreNodeWrapper swrapper = new StoreNodeWrapper(BDBpath, storeNumber);
					
			     
			     	for(String url : urlSet)
					{
						System.out.println("UrLs: "+url);
					}
					*//**
					 * Get pageranks from the database for the urls
					 *//*
					for(String url : urlSet)
					{
						System.out.println("uRL FOR paGE rANKING: "+url);
						*//**
						 * Get the Ranks for the url from the database
						 *//*
						long urlRank = swrapper.getUrlInfo(url).getPageRank();
						sri.setUrlRank(url, urlRank);
					}
					
					seEngine.putSocketInfoNode(sri);
					//seEngine.putSocketInfoNode(sockInfrank);
				}
				else
				{
					addPendingRequestNew(requestId, sri);
				}			
			}
		}
		//*************************************************************
		else if(msg instanceof KeywordMsgResult)
		{
			KeywordMsgResult resultMsg = (KeywordMsgResult)msg;
			String keyword = resultMsg.getKeyword();
			String results = resultMsg.getResults();
			UUID requestId = resultMsg.getRequestId();
			HashMap<String, Long> urlTfIdf = resultMsg.getUrlTfIdf();
			System.out.println("Request Id in deliver: "+requestId);
			System.out.println("RECEIVED RESULTS FOR "+keyword + "=> "+ results);
			System.out.println("Boolean: "+pendingRequests.containsKey(requestId));
			
			ArrayList<SocketInfoResponse> socketList = getSocketListForKeyword(keyword);
			if(socketList == null){
				System.err.println("Keyword mismatch in Pending Search Requests Hashmap");
				return;
			}
			for(SocketInfoResponse sri:socketList){
				sri.setKeyword(keyword);
				sri.setResponse(results);
				seEngine.putSocketInfoNode(sri);
			}
			
			if(this.pendingRequests.containsKey(requestId))
			{
				System.out.println("Pending Request contains the key");
				SocketInfoResponse sri = this.pendingRequests.get(requestId);
				sri.setResponse(keyword, results);
				
				*//**
				 * Get the url to TfIdfs for each url  and populate url<->totalTfIdfs in SocketResponseInfo Class
				 *//*
				for(Map.Entry mEntry : urlTfIdf.entrySet())
				{
					sri.setTfIdfResponse((String)mEntry.getKey(), (Long)mEntry.getValue());
				}
				
//				StoreNodeWrapper swrapper = new StoreNodeWrapper(BDBpath, storeNumber);
//				StoreCache cacheEntry = swrapper.getCacheEntry(keyword);
//				HashMap<String, Long> urlTfIdfMap = cacheEntry.getTfIdfMap();
				
				
				
				if(sri.numberOfKeywords == sri.keywordUrlMap.size())
				{
					pendingRequests.remove(requestId);
					HashSet<String> urlSet = sri.getAllResponseUrls();
			     	StoreNodeWrapper swrapper = new StoreNodeWrapper(BDBpath, storeNumber);
					
			     	
					for(String url : urlSet)
					{
						System.out.println("UrLs: "+url);
					}
					*//**
					 * Get pageranks from the database for the urls
					 *//*
					for(String url : urlSet)
					{
						long urlRank = swrapper.getUrlInfo(url).getPageRank();
						sri.setUrlRank(url, urlRank);
					}
					
					seEngine.putSocketInfoNode(sri);
					//seEngine.putSocketInfoNode(sockInfrank);
				}
				else
				{
					addPendingRequestNew(requestId, sri);
				}
			}
			
		}
	}
	
	public void sendKeyWord(String keyword, SocketInfoResponse SocketInfoNodeResponse sri, UUID requestId)
	{
		//addPendingRequest(keyword, sri);
		System.out.println("Request Id in send: "+requestId);
		addPendingRequestNew(requestId, sri);
		IdFactory idFactory = this.node.getIdFactory();
		Id keywordId = idFactory.buildId(keyword);
		KeywordMsg keyMsg = new KeywordMsg(keyword, node.getLocalNodeHandle(), node.getId(),requestId);
		System.out.println("Sending "+keyword+ " to "+keywordId);
		endpoint.route(keywordId, keyMsg, null);
	}

	




	@Override
	public boolean forward(RouteMessage arg0) {
		// TODO Auto-generated method s
		System.out.println("In forward");
		return true;
	}

	@Override
	public void update(NodeHandle arg0, boolean arg1) {
		// TODO Auto-generated method stub
		
	}
	
	public ArrayList<SocketInfoResponse> getSocketListForKeyword(String key){
		return pendingSearchRequests.get(key);
	}

}
*/