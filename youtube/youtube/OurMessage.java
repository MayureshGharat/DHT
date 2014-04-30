package edu.upenn.cis.cis555.youtube;

import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.NodeHandle;

public class OurMessage implements rice.p2p.commonapi.Message {
	NodeHandle from;
	String content;
	boolean wantResponse = true;
	Id fromId;
	
	public OurMessage(NodeHandle from, String content, Id fromid)
	{
		this.from = from;
		this.content = content;
		this.fromId = fromId;
	}

	
	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}
}
