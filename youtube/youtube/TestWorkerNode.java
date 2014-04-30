/*package edu.upenn.cis.cis555.youtube;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;


import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.IdFactory;


public class TestWorkerNode implements Runnable{

	SocketInfoNode clientSocketInfoNode;
	Socket clientSocket;
	Thread t;
	int port_num;
	PrintWriter writer;
	BufferedReader in;
	int serverPort;

	P2PCache callingP2PCache;
	NodeSetup callingNdSetup;	
	MyApp callingMyApp;
	YouTubeClient callingYtClient;
	YouTubeApp callingYouTubeApp;
	
	public TestWorkerNode(P2PCache cache, NodeSetup ndSetup, YouTubeClient ytClient) {
		callingP2PCache = cache;
		callingNdSetup = ndSetup;
		callingMyApp = callingNdSetup.getApp();
		callingYtClient = ytClient;
		callingYouTubeApp = callingNdSetup.getYouTubeApp();
	}
	
	public String getDateGMT()
	{
		String date = null;
		Calendar calendarInstance = Calendar.getInstance();
	    SimpleDateFormat s = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss");
	    s.setTimeZone(TimeZone.getTimeZone("GMT"));
		date = s.format(calendarInstance.getTime());
		return (date+" GMT");
	}
	
	public String Response_200(String date)
	{		
		String connection;
		String response_200;
		{
			response_200 = 	 "HTTP/1.0 200 OK\r\n"+
							"Date: "+date+ "\r\n" +
							"Server: HTTP Server\r\n"+
							"Content-Type: text/xml\r\n";//\r\n";
		}
	
		System.out.println(response_200);
		return response_200;
	}
	
    public String Response_400(String date)
    {
    	String response;
		{
    		response = 	"HTTP/1.0 400\r\n" +
						"Date: "+date+ "\r\n" +
						"Server: HTTP Server\r\n"+
						"Content-Type: text/html\r\n\r\n" +
						"400 Bad Request";
		}
    	return response;
      }
    //********************************************************************************************
    *//**
     * New Change
     *//*
    public void sendXML(PrintStream out , String results){
		String response = "HTTP/1.1 200 OK\r\n";
		response += "Content-Type:text/xml\r\n";
		response += "Content-Length:"+results.getBytes().length + "\r\n";
		response += "Connection:close\r\n";
		response += "\r\n" + results;
		
		out.print(response);
	}
    
    public void sendBadRequest(PrintStream out){
		String body = "<html><body><h2>Bad Request</h2></body></html>\n";
		String response = "HTTP/1.1 400 Bad Request\r\n";
		response += "Content-Type:text/html\r\n";
		response += "Content-Length:"+body.getBytes().length + "\r\n";
		response += "Connection:close\r\n";
		response += "\r\n"+body;
		
		out.print(response);
	}
    
    //************************************************************************************************
    
	@Override
	public void run() {
		serverPort = callingP2PCache.daemonPort;
		
		while(true)
		{
			*//**
			 * New Change
			 *//*
			
			Socket socket = null;
			try
			{
				Object siObj = callingP2PCache.getSocket();
		
				if(siObj instanceof SocketInfoResponse){
					//System.out.println("RESTWorker retrieved a SocketResponseInfo object");
					SocketInfoResponse sri = (SocketInfoResponse)siObj;
					socket = sri.getSocket();
					PrintStream out = sri.getPrintStream();					
					String results = sri.getResponse();
					
					sendXML(out,results);
					socket.close();
					continue;
				}
				else if(siObj instanceof SocketInfoNode)
				{
					SocketInfoNode si = (SocketInfoNode)siObj;
					socket = si.getSocket();
					//BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					PrintStream out = new PrintStream(socket.getOutputStream(),true);
					port_num = si.getPortNum();
					in = si.getInputReader();
					writer = si.getOutputWriter();	
					String lastLine = "";
					lastLine = in.readLine();
					String[] strArray = lastLine.split(" ");
					//String date = getDateGMT();
					
					if(strArray.length != 3 || !strArray[0].equals("GET") || !strArray[1].startsWith("/videos?key="))
					{
						//response = Response_400(date);
						sendBadRequest(out);
						socket.close();
						continue;
					}
					else
					{
						String[] keyExtractor = strArray[1].split("=");
						String keyWord = keyExtractor[1];
						*//**
						 * INVOKE YOU TUBE APP WITH KEYWORD - PASS SOCKET INFO 
						 *//*
						SocketInfoResponse sri = new SocketInfoResponse(socket,out);
						callingYouTubeApp.requestVideoSearch(keyWord, sri);
					
					}
					
				}
				
				
				
				
				//*************************************************************************************************************
				clientSocketInfoNode = callingP2PCache.getSocket();
				
				clientSocket = clientSocketInfoNode.getSocket();
				port_num = clientSocketInfoNode.getPortNum();
				in = clientSocketInfoNode.getInputReader();
				writer = clientSocketInfoNode.getOutputWriter();
				System.out.println("KeyWord: In TestWorkerNode:"+clientSocket.getLocalPort());
				String response="",lastLine="";
				if(port_num == serverPort)
				{
					
					 * do the processing
					 * Read the Keyword from the Socket
					 * Route it to proper node
					 * Get the feedback from that node
					 * Send it to the servlet
					 
					//System.out.println("in.reaadY: " +in.ready());
					lastLine = in.readLine();
					System.out.println("lASTlINE: "+lastLine);
					
					//Extract the Keyword
					
					 * String Keyword = "...";
					 
					
					String keyWord="";
					
					String[] strArray = lastLine.split(" ");
					String date = getDateGMT();
					if(!strArray[0].equals("GET") || !strArray[1].startsWith("/videos?key="))
					{
						response = Response_400(date);
					}
					else
					{
						response = Response_200(date);
						String[] strArray1 = strArray[1].split("=");
						keyWord = strArray1[1];
						byte[] byteArray = keyWord.getBytes();
						IdFactory idFact = callingMyApp.node.getIdFactory();
						Id nodeId = idFact.buildId(keyWord); 
						callingMyApp.sendMessage(nodeId, keyWord);
						
						 * Change
						 
						//Thread.sleep(8000);
						String xmlResponse = callingMyApp.hashCache.get(keyWord);
						System.out.println("xmlRespose: "+xmlResponse);
						response = response +"Content-Length: "+xmlResponse.length()+"\r\n\r\n";
						response = response+xmlResponse;
						
					}
					writer.print(response);
					writer.flush();
					writer.close();
					clientSocket.close();

				}
			}
			catch(Exception e)
			{
				
			}
		}
	}
}
*/