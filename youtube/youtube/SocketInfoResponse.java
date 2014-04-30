package edu.upenn.cis.cis555.youtube;

import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class SocketInfoResponse extends SocketInfoNode{
	

	//String response;
	//String keyword;
	int numberOfKeywords;
	HashMap<String, ArrayList<String>> keywordUrlMap = new HashMap<String, ArrayList<String>>();
	/**
	 * HashMap of url to (tf1*idf1,tf2*idf2, .....,tfn*idfn)
	 */
	//HashMap<String, ArrayList<Long>>  urlTfIdfmap = new HashMap<String, ArrayList<Long>>();
	HashMap<String, Long> totalTfIdfMap = new HashMap<String, Long>();
	
 	HashMap<String, Long> urlRankMap = new HashMap<String, Long>();
	
	PrintStream out;
	
	public SocketInfoResponse(Socket socket , PrintStream out, int numberOfKeywords) {
		super(socket);
		this.out = out;
		this.numberOfKeywords = numberOfKeywords;
	}
	
	public void setResponse(String keyword,String response){
		//this.response = response;
		//this.keywordUrlMap.put(keyword, response);
		if(response.contains("&~&"))
		{
			String[] respArray = response.split("&~&");
			ArrayList<String> urlList = new ArrayList<String>();
			for(String s : respArray)
			{
				urlList.add(s);
			}
			
			this.keywordUrlMap.put(keyword, urlList);
		}
	}
	
	
	public void setTfIdfResponse(String url, long tfIdf)
	{
		/*if(urlTfIdfmap.containsKey(url))
		{
			ArrayList<Long> lst = urlTfIdfmap.get(url);
			lst.add(tfIdf);
			urlTfIdfmap.put(url,lst);
		}
		else
		{
			ArrayList<Long> lst = new ArrayList<Long>();
			lst.add(tfIdf);
			urlTfIdfmap.put(url, lst);
		}*/
		if(totalTfIdfMap.containsKey(url))
		{
			long l = totalTfIdfMap.get(url);
			l = l+tfIdf;
			totalTfIdfMap.put(url, l);
		}
		else
		{
			totalTfIdfMap.put(url, tfIdf);
		}
	}
	
	/*public void setKeyword(String keyword){
		this.keyword = keyword;
	}*/
	
	public int getHashMapSize()
	{
		return keywordUrlMap.size();
	}
	
	public PrintStream getPrintStream(){
		return out;
	}
	
	/*public String getResponse(){
		return response;
	}*/
	
	public HashMap<String,ArrayList<String>> getResponse()
	{
		/*StringBuilder sbResponse = new StringBuilder();
		for(Map.Entry mapEntry: keywordUrlMap.entrySet())
		{
			sbResponse.append(mapEntry.getValue());
			sbResponse.append("&~&");
		}
		
		return sbResponse.toString();*/
		
		return keywordUrlMap;
	}
	
	public HashSet<String> getAllResponseUrls()
	{
		//StringBuilder sbResponse = new StringBuilder();
		HashSet<String> urlSet = new HashSet<String>();
		for(Map.Entry mapEntry: keywordUrlMap.entrySet())
		{
			/*sbResponse.append(mapEntry.getValue());
			sbResponse.append("&~&");*/
			/*String s = (String) mapEntry.getValue();
			System.out.println(" S: "+s);
			if(s.contains("&~&"))
			{
				String[] urlExtractor = s.split("&~&");
				for(String str : urlExtractor)
				{
					urlSet.add(s);
				}
			}
			else
			{
				urlSet.add((String) mapEntry.getValue());
			}*/
			
			ArrayList<String> ulList = (ArrayList<String>) mapEntry.getValue();
			for(String s : ulList)
			{
				urlSet.add(s);
			}
		}
		
		//return sbResponse.toString();
		return urlSet;
	}
	
	public void setUrlRank(String url, long rank)
	{
		urlRankMap.put(url, rank);
	}
	
	public HashMap<String, Long> getUrlRankMap()
	{
		return urlRankMap;
	}
	
	public HashMap<String, Long> getUrlTfIdfMap()
	{
		return totalTfIdfMap;
	}
	
	

}
