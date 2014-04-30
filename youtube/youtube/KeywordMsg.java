package edu.upenn.cis.cis555.youtube;

import java.util.UUID;

import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.NodeHandle;

public class KeywordMsg implements Message{

	private static final long serialVersionUID = 1L;
	
	String keyword;
	NodeHandle from;
	Id fromId;
	UUID requestId;
	
	public KeywordMsg(String keyword, NodeHandle nodeHandle, Id fromId, UUID reqId){
		this.keyword = keyword;
		this.from = nodeHandle;
		this.fromId = fromId;
		this.requestId = reqId;
	}
	
	public String getKeyword(){
		return keyword;
	}
	
	public NodeHandle getSenderNodeHandle(){
		return from;
	}

	public int getPriority() {
		return 1;
	}
	
	public UUID getRequestId()
	{
		return requestId;
	}

}
