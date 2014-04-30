/*package edu.upenn.cis.cis555.youtube;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class EngineWorker implements Runnable {

	Thread t;
	SearchEngine seEngine;
	SearchEngineApp seEngineApp;
	
	EngineWorker(SearchEngine seEngine, NodeSetup ndSetup)
	{
		this.seEngine = seEngine;
		this.seEngineApp = ndSetup.getSearchEngineApp();
	}
	
	private void sendData(PrintStream out, String results) {
		// TODO Auto-generated method s  tub
		
	}
	
	private void sendData(PrintWriter write, String results)
	{
		String response = "HTTP/1.1 200 OK\r\n"+
						  "Content-Length: "+results.length()+"\r\n\r\n"+results;
		write.print(response);
		write.close();
		
	}
	
	private void sendBadRequest(PrintStream out) {
		// TODO Auto-generated method stub
		
	}
	
	*//**
	 * ADAPTED FROM: 
	 * function to sort a hashmap based on values
	 * @param m : HashMap to be sorted according to values
	 * @return 
	 *//*
	private ArrayList sortByValue(final HashMap<String, Long> m) {
	        ArrayList keys = new ArrayList();
	        keys.addAll(m.keySet());
	        Collections.sort(keys, new Comparator() {
	            public int compare(Object o1, Object o2) {
	                Object v1 = m.get(o1);
	                Object v2 = m.get(o2);
	                if (v1 == null) {
	                	if(v2 == null)
	                	{
	                		return 0;
	                	}
	                	else
	                	{
	                		return 1;
	                	}
	                		
	                    //return (v2 == null) ? 0 : 1;
	                }
	                else if (v1 instanceof Comparable) {
	                    return ((Comparable) v2).compareTo(v1);
	                }
	                else {
	                    return 0;
	                }
	            }
	        });
	        return keys;
	    }

	@Override
	public void run() {
		while(true)
		{
			Socket socket = null;
			try
			{
				
				 * Get the request or result object from the main Vector
				 
				Object siObj = seEngine.getSocket();
				
				if(siObj instanceof SocketInfoNodeResponse)
				{
					SocketInfoNodeResponse sri = (SocketInfoNodeResponse) siObj;
					socket = sri.getSocket();
					PrintStream out = sri.getPrintStream();
					PrintWriter write = sri.getOutputWriter();
					//String results =  sri.getResponse();
					*//**
					 * KeyWord <-> ArrayList<Urls> 
					 *//*
					HashMap<String, ArrayList<String>> keyUrlMap = sri.getKeyWordUrlMap();
					  
					
					*//**
					 * Url <-> Posting Map
					 *//*
					HashMap<String, Posting> urlPostingMap = sri.getUrlPostingMap();
					
					for(Map.Entry mEntry : urlPostingMap.entrySet())
					{
						Posting p = (Posting) mEntry.getValue();
						System.out.println("uRl :"+ mEntry.getKey()+ " Title: "+p.getTitle());
					}
					
					
					//System.out.println("Results: "+results);
					//sendData(out, results);
					
					*//**
					 * HashMap to Store the url to number of keywords its appearing in mapping
					 *//*
					HashMap<String, Long> urlRelevanceMap = new HashMap<String, Long>();
					
					*//**
					 * Populating the relevance map for the urls
					 *//*
					for(Map.Entry entry : keyUrlMap.entrySet())
					{
						//System.out.println(" Key: "+entry.getKey()+" Urls: "+entry.getValue());
						for(String ul : (ArrayList<String>)entry.getValue())
						{
							if(urlRelevanceMap.containsKey(ul))
							{
								long count = urlRelevanceMap.get(ul);
								count++;
								urlRelevanceMap.put(ul, count);
							}
							else
							{
								long count = 1;
								urlRelevanceMap.put(ul, count);
							}
						}
					}
					
					for(Map.Entry ulRelevanceentry : urlRelevanceMap.entrySet())
					{
						System.out.println(" Url: "+ulRelevanceentry.getKey()+ " Relevance: "+ulRelevanceentry.getValue());
					}
					
					
					*//**
					 * Map for url to Rank mapping
					 *//*
					
					HashMap<String, Long> urlRankMap =  sri.getUrlRankMap();
					
					*//**
					 * Map to Store url<-> Total Score
					 *//*
					
					HashMap<String, Double> totalScoreMap = new HashMap<String, Double>();
					
					for(Map.Entry ent : sri.getUrlRankMap().entrySet())
					{
						System.out.println("Key: "+ent.getKey()+" Rank: "+ent.getValue());
					}
					
					*//**
					 * Store the mapping of url<-> TotalScore
					 *//*
					for(Map.Entry mEntry : sri.getUrlTfIdfMap().entrySet())
					{
						System.out.println("uRL: "+mEntry.getKey()+ " Toatal TfIdf Value: "+mEntry.getValue());
						String url  = (String) mEntry.getKey();
						long rank = urlRankMap.get(url);
						double tfIdf = (Double)mEntry.getValue();
						double totalScore = rank * tfIdf;
						totalScoreMap.put(url, totalScore);
					}
					*//**
					 * Printing The url<->Total Score 
					 *//*
					for(Map.Entry mapEntry : totalScoreMap.entrySet())
					{
						System.out.println(" URl: "+mapEntry.getKey()+ "  Total Score: "+mapEntry.getValue());
					}
					//HashMap<Integer, ArrayList<String>> hMap = new HashMap<Integer, ArrayList<String>>();
					
					*//**
					 * TreeMap to store the Urls in order of there Relevance
					 *//*
					TreeMap<Long, ArrayList<String>> tMap =  new TreeMap<Long,ArrayList<String>>(new CompareClass());
					
					for (Iterator i = sortByValue(urlRelevanceMap).iterator(); i.hasNext(); ) {
				            String key = (String) i.next();
				            System.out.printf("key: %s, value: %s\n", key, urlRelevanceMap.get(key));
				            
				            if(tMap.containsKey(urlRelevanceMap.get(key)))
				            {
				            	ArrayList<String> list = tMap.get(urlRelevanceMap.get(key));
				            	list.add(key);
				            	tMap.put(urlRelevanceMap.get(key), list);
				            }
				            else
				            {
				            	ArrayList<String> list = new ArrayList<String>();
				            	list.add(key);
				            	tMap.put(urlRelevanceMap.get(key), list);

				            }
				            if(hMap.containsKey(urlRelevanceMap.get(key)))
				            {
				            	ArrayList<String> list = hMap.get(urlRelevanceMap.get(key));
				            	list.add(key);
				            	hMap.put(urlRelevanceMap.get(key), list);
				            }
				            else
				            {
				            	ArrayList<String> list = new ArrayList<String>();
				            	list.add(key);
				            	hMap.put(urlRelevanceMap.get(key), list);
				            }
				        }
					
					StringBuilder responseBuilder = new StringBuilder();
					
					for(Map.Entry mEntry : tMap.entrySet())
					{
						responseBuilder.append(mEntry.getKey());
						responseBuilder.append(" ");
						responseBuilder.append(mEntry.getValue());
						responseBuilder.append("\n");
						System.out.println("Relevance: "+mEntry.getKey()+ " Urls: "+mEntry.getValue());
					}
					
					
					
					
					 HashSet for holding the Urls in sorted order of there Relevance and Ranks
					 
					LinkedHashSet<String> urlSet = new LinkedHashSet<String>();
					
					for(Map.Entry tMapentry : tMap.entrySet())
					{
						ArrayList<String> list = (ArrayList<String>) tMapentry.getValue();
						HashMap<String, Long> tempMap = new HashMap<String, Long>();
						for(String s: list)
						{
							if(urlRankMap.containsKey(s))
							{
								tempMap.put(s, urlRankMap.get(s));
							}
						}
						List<String> lst = sortByValue(tempMap);
						urlSet.addAll(lst);
					}
					
					for(String s : urlSet)
					{
						System.out.println("S: "+s);
					}
					
					
					
					write.print(responseBuilder.toString());
					write.flush();
					write.close();
					
					sendData(write, responseBuilder.toString());
					socket.close();
					continue;
				}
				else if(siObj instanceof SocketInfoNode)
				{
					SocketInfoNode si = (SocketInfoNode) siObj;
					socket = si.getSocket();
					BufferedReader in = si.getInputReader();
					PrintWriter writer = si.getOutputWriter();
					PrintStream out = si.getPrintStream();	
					
					*//**
					 * Read the request line from the socket
					 *//*
					String requestLine = in.readLine();
					System.out.println("requestLine: "+requestLine);
					
					if(requestLine == null)
					{
						sendBadRequest(out);
						socket.close();
						continue;
					}
					else
					{
						String[] requestLineParts = requestLine.trim().split(" ");
						
						if(requestLineParts.length != 3)
						{
							sendBadRequest(out);
							socket.close();
							continue;
						}
						else if(!(requestLineParts[0].equals("GET") || requestLineParts[0].equals("POST") || requestLineParts[1].contains("/videos?key=")))
						{
							sendBadRequest(out);
							socket.close();
							continue;
						}
						else
						{
							String[] keywordExtractor = requestLineParts[1].split("=");
							String keyWordString = keywordExtractor[1];
							System.out.println("Key Word: "+keyWordString);
							*//**
							 * Create a SocketInfoResponse object
							 *//*
							String[] keyWords = keyWordString.split("&~&");
							
							int numberOfKeywords = keyWords.length;
							UUID requestId = UUID.randomUUID();
							SocketInfoNodeResponse sri = new SocketInfoNodeResponse(socket, out, numberOfKeywords);//set number of keywords
							for(int j=0; j < numberOfKeywords; j++)
							{
								seEngineApp.sendKeyWord(keyWords[j],sri, requestId);//requestid also
								//keyWord = ;
							}
							
						}
						
					}
					
					
					
				}
				
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
	}



}
*/