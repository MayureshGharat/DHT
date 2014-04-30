package edu.upenn.cis.cis555.youtube;

import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class SocketInfoNodeResponse extends SocketInfoNode{
	
	int numberOfKeywords;
	HashMap<String, ArrayList<String>> keywordUrlMap = new HashMap<String, ArrayList<String>>();
	HashMap<String, ArrayList<Posting>> keywordPostingMap = new HashMap<String, ArrayList<Posting>>();
	/**
	 * Url <-> Posting
	 */
	HashMap<String, Posting> urlPostingMap = new HashMap<String, Posting>();
	
	HashMap<String, Double> totalTfIdfMap = new HashMap<String, Double>();
	
	HashMap<String, Long> urlRankMap = new HashMap<String, Long>();

	PrintStream out;
	
	public SocketInfoNodeResponse(Socket socket , PrintStream out, int numberOfKeywords) {
		super(socket);
		this.out = out;
		this.numberOfKeywords = numberOfKeywords;
	}
	
	public void setkeyWordUrlMap(String Keyword, ArrayList<String> urlList)
	{
		keywordUrlMap.put(Keyword, urlList);
	}
	
	
	public void setKeyWordPostingMap(String keyword, ArrayList<Posting> postingList)
	{
		keywordPostingMap.put(keyword, postingList);
	}
	
	public HashMap<String, ArrayList<String>> getKeyWordUrlMap()
	{
		return keywordUrlMap;
	}
	
	public HashMap<String, ArrayList<Posting>> getKeywordPostingMap()
	{
		return keywordPostingMap;
	}
	
	public void setUrlPostingMap(ArrayList<Posting> pList)
	{
		for(Posting p : pList)
		{
			urlPostingMap.put(p.getUrl(), p);
		}
	}
	
	public HashSet<String> getAllResponseUrls()
	{
		//StringBuilder sbResponse = new StringBuilder();
		HashSet<String> urlSet = new HashSet<String>();
		for(Map.Entry mapEntry: keywordUrlMap.entrySet())
		{	
			ArrayList<String> ulList = (ArrayList<String>) mapEntry.getValue();
			for(String s : ulList)
			{
				urlSet.add(s);
			}
		}
		return urlSet;
	}
	
	public void setTotalTfIdf(String url, Double tfIdf)
	{
		if(totalTfIdfMap.containsKey(url))
		{
			double l = totalTfIdfMap.get(url);
			l = l+tfIdf;
			totalTfIdfMap.put(url, l);
		}
		else
		{
			totalTfIdfMap.put(url, tfIdf);
		}
	}
	
	public void setUrlRank(String url, long rank)
	{
		urlRankMap.put(url, rank);
	}
	
	public HashMap<String, Long> getUrlRankMap()
	{
		return urlRankMap;
	}
	
	public HashMap<String, Double> getUrlTfIdfMap()
	{
		return totalTfIdfMap;
	}
	
	
	public HashMap<String, Posting> getUrlPostingMap()
	{
		return urlPostingMap;
	}
	
	
	
	
}
