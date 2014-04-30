package edu.upenn.cis.cis555.youtube;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import rice.p2p.commonapi.Message;

public class SearchResponseMsg implements Message{
	
	 private static final long serialVersionUID = 1L;
	 
	 String Keyword;
	 ArrayList<Posting> postingList;
	 
	 UUID requestId;
	 double Idf;
	 HashMap<String, Double> urlTfIdf = new HashMap<String, Double>();
	// HashMap<String, Posting> urlPostingMap = new HashMap<String, Posting>();
	 // HashSet<String> urlSet = new HashSet<String>();
	 ArrayList<String> urlList = new ArrayList<String>();
	 
	 public SearchResponseMsg(String keyword, ArrayList<Posting> pList, UUID reqId, double idf)
	 {
		 this.Keyword = keyword;
		 this.postingList = pList;
		 this.requestId = reqId;
		 this.Idf = idf;
		 
		 calculate();
	 }
	 
	 public void calculate()
	 {
		 for(Posting p : postingList)
		 {
			 //urlSet.add(p.getUrl());
			 urlList.add(p.getUrl());
			 double tfIdf = Idf * p.getTf();
			 urlTfIdf.put(p.getUrl(), tfIdf);
		
			 
		 }
	 }
	 
	 
	 
	 public String getKeyword()
	 {
		 return Keyword;
	 }
	 
	 public int getPriority() 
	 {
			return 1;
	 }
	 
	 public ArrayList<Posting> getPostingList()
	 {
		 return postingList;
	 }
	
	public UUID getRequestId()
	{
		return requestId;
	}
	
	public HashMap<String, Double> getTfIdfMap()
	{
		return urlTfIdf;
	}
	
	public ArrayList<String> getUrlList()
	{
		return urlList;
	}
}
