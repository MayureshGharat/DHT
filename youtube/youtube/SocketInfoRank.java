package edu.upenn.cis.cis555.youtube;

import java.util.HashMap;

public class SocketInfoRank extends SocketInfoNode{

	HashMap<String, Long> urlRankmap = new HashMap<String, Long>();
	
	public void seturlRank(String url, long rank)
	{
		urlRankmap.put(url, rank);
	}
	
	public HashMap<String, Long> geturlRankMap()
	{
		return urlRankmap;
	}
}
