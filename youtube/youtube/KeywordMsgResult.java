package edu.upenn.cis.cis555.youtube;

import java.util.HashMap;
import java.util.UUID;

import rice.p2p.commonapi.Message;

public class KeywordMsgResult implements Message{

	private static final long serialVersionUID = 1L;
	
	String keyword;
	String results;
	UUID requestId;
	HashMap<String, Long> urlTfIdf = new HashMap<String, Long>();
 	
	public KeywordMsgResult(String keyword,String results, UUID reqId, HashMap<String, Long>urlTfIdf){
		this.keyword = keyword;
		this.results = results;
		this.requestId = reqId;
		this.urlTfIdf = urlTfIdf;
	}
	
	public String getKeyword(){
		return keyword;
	}
	
	public String getResults(){
		return results;
	}

	public int getPriority() {
		return 1;
	}
	
	public UUID getRequestId()
	{
		return requestId;
	}

	public HashMap<String, Long> getUrlTfIdf()
	{
		return urlTfIdf;
	}
}
