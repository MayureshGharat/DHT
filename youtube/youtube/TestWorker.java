package edu.upenn.cis.cis555.youtube;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URLConnection;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TimeZone;
import java.util.Vector;
import java.util.Map.Entry;

import javax.imageio.ImageIO;



public class TestWorker implements Runnable{

	HttpServer callingServer;
	SocketInfo clientSocketInfo;
	Socket clientSocket;
	Thread t;
	int port_num;
	PrintWriter writer;
	BufferedReader in;
	int serverPort;
	String host;
	PrintStream ps;
	InputStreamReader isr;
	String httpVersion = null;
	BufferedReader in1;
	
	String contentType;
	long contentLength;
	String connection;
	String lastLine = "";
	String date;
	String [] ifModifiedTime;
	public HashMap<String,String> httpResponse = new HashMap<String, String>();
	HashMap<String, String> httpRequest = new HashMap<String, String>(); 
	boolean isServlet;
	ByteArrayOutputStream byteArray;
	public Vector <String> reqArray ;
	LoadServlet initializeServlet;
	boolean connectionFlag;
	HashMap<String, String> statusMap = new HashMap<String, String>();
	
	public TestWorker(){}
	
	public TestWorker(HttpServer s, LoadServlet ls) {
		
		callingServer = s;
		initializeServlet = ls;
	}

	public Boolean isModified(String dateString, long lastModified)
	{
		SimpleDateFormat dateFormat = null;
		if(dateString.contains(","))
		{
			if(dateString.contains("-"))
			{	
				System.out.println("In - of isModified()");
				dateFormat = new SimpleDateFormat("E, dd-MMM-yy HH:mm:ss");
				dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
			}
			else
			{
				dateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss");
				dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
			}
		}
		else
		{
			dateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss yyyy");
			dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		}
		Date d = new Date();
		
	    Date convertedDate = null;
		
	    try {
			convertedDate = dateFormat.parse(dateString);
		} catch (ParseException e) {
			System.out.println("Cannot parse date");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(d.getTime() < convertedDate.getTime())
		{
			return false;
		}
		else 
		{
			if(convertedDate.getTime() < lastModified )
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	}	
	
	

	
	public void getFileData(String  fileName) throws FileNotFoundException,IOException 
	{
			String s = "";
			s = fileName;
			File file = new File(callingServer.rootDir + s);
			FileInputStream fin = new FileInputStream(file);
			
			byte fileContent[] = new byte[2048];
			int n;
			while((n=fin.read(fileContent))>0)
			{
				ps.write(fileContent, 0, n);
			}

			ps.flush();
			fin.close();
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
	
	private String readDirectory(String p,boolean cflag) throws NullPointerException
	{
		File f = new File(callingServer.rootDir + p);
		StringBuffer sb = new StringBuffer();
		if(cflag == false)
		{
		 sb.append("HTTP/1.1 200 OK\r\n "+"Content-Length: "+f.length()+"\r\nConnection: close\r\n\r\n"); 
		}
		else
		{
			 sb.append("HTTP/1.1 200 OK\r\n "+"Content-Length: "+f.length()+"\r\n\r\n"); 
		}
		
       
        	sb.append("<html><head></head><body><p>The list of the files is</p>");
        	List<String> list = new ArrayList<String>();
    
       
        for(int i = 0; i <f.list().length; i++)
        {
                list.add(f.list()[i]);
        }
      
        for(int i = 0; i<list.size(); i++){
                sb.append("<li><a href = \"http://localhost:"+port_num+"" + p + "/" + list.get(i) + "\">" + list.get(i) + "</a></li>");
        }
        sb.append("<ul></body></html>");
        return sb.toString();   
    }
	
	public String ResponseControl(HashMap<Thread, String> threadTourl)
	{
		StringBuilder fileData = new StringBuilder();
		try {
			File f = new File("/home/cis555/hw1/report.log");
			 BufferedReader reader = new BufferedReader(new FileReader("/home/cis555/hw1/report.log"));
		      char[] buf = new char[1024];
		      
			int numRead=0;
	        while((numRead=reader.read(buf)) != -1){
	            String readData = String.valueOf(buf, 0, numRead);
	            fileData.append(readData);
	            buf = new char[1024];
	        }
	        reader.close();
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		StringBuffer sb = new StringBuffer("HTTP/1.1 200 OK\n\n <html><head><ul><b>HTTP Server</b></ul><ul><b>SEAS Login: HTTP Server</b></ul></head><body>");
		sb.append("<p><a href = \"http://localhost:"+port_num+"/shutdown\"> <button type=\"button\">SHUT DOWN</button></a></p>");
		sb.append("<p></p>");
		sb.append("<p>The list of the Threads is</p>");
		for(Thread t : callingServer.getThreadList())
		{
			sb.append("<li>"+t.getName()+"</li>");
		}
		sb.append("<p></p><p></p>");
		for(Map.Entry threadMapurl : threadTourl.entrySet())
		{
			sb.append("<li>"+((Thread)threadMapurl.getKey()).getName()+"  "+threadMapurl.getValue()+ "</li>");
		}
		sb.append("<p>ERROR LOG:</p>");
		sb.append("<p>"+fileData+"</p>");
		
		sb.append("</body></html>");
		String response  = sb.toString();
		return response;
	}
	
	public byte[] Response_200(String date, String modifiedDate, File f,String Version, boolean cflag)
	{		
		String connection;
		String response_200;
		if(cflag == false)
		{
			response_200 = 	Version+" 200 OK\r\n"+
							"Date: "+date+ "\r\n" +
							"Server: HTTP Server\r\n"+
							"Last-Modified: "+modifiedDate+"\r\n"+
							"Connection: close\r\n"+//+connectionFlag+"\r\n"+
							"Content-Type: "+URLConnection.guessContentTypeFromName(f.getName())+"\r\n"+
							"Content-Length: "+f.length()+/*+(byte)fileData.length()+*/ "\r\n\r\n";
		}
		else
		{
					response_200 = 	Version+" 200 OK\r\n"+
									"Date: "+date+ "\r\n" +
									"Server: HTTP Server\r\n"+
									"Last-Modified: "+modifiedDate+"\r\n"+
									//"Connection: close\r\n"+//+connectionFlag+"\r\n"+
									"Content-Type: "+URLConnection.guessContentTypeFromName(f.getName())+"\r\n"+
									"Content-Length: "+f.length()+/*+(byte)fileData.length()+*/ "\r\n\r\n";
		}
		System.out.println(response_200);
		
		
		
		return response_200.getBytes();
	}


    public byte[] Response_404(String date, String Version, boolean cflag) 
    {
    	String response;
    	if(cflag == true)
		{
    		response = 	Version+" 404 \r\n" +
						"Date: "+date+ "\r\n" +
						"Server: HTTP Server\r\n"+
						"Content-Type: text/html\r\n" +
						"Content-Length: 111\r\n"+"\r\n"+
						"<html><body><h2>File not found</h2></body></html>";
		}
		else
		{
					response = 	Version+" 404 \r\n" +
								"Date: "+date+ "\r\n" +
								"Server: HTTP Server\r\n"+
								"Content-Type: text/html\r\n" +
								"Content-Length: 111\r\n"+
								"Connection: close\r\n"+"\r\n"+
								"<html><body><h2>File not found</h2></body></html>";
		}
    	return response.getBytes();
    }
    
    public byte[] Response_400(String date, String Version,boolean cflag)
    {
    	String response;
    	if(cflag == false)
		{
    		response = 	Version+" 400\r\n" +
						"Date: "+date+ "\r\n" +
						"Server: HTTP Server\r\n"+
						"Content-Type: text/html\r\n" +
						"Content-Length: 111\n"+"\r\n"+
						"Connection:close\r\n"+"\r\n"+
						"<html><body><h2>400 Bad Request</h2></body></html>";
		}
		else
		{
			 response = 	Version+" 400\r\n" +
							"Date: "+date+ "\r\n" +
							"Server: HTTP Server\r\n"+
							"Content-Type: text/html\r\n" +
							"Content-Length: 111\n"+"\r\n"+
							"<html><body><h2>400 Bad Request</h2></body></html>";
		} 	
    	return response.getBytes();
      }
	
    public byte[] Response_304(String date, String Version, boolean cflag)
    {
    	
    	String response;
    	if(cflag == false)
    	{
    		response = 	Version+" 304 Not Modified\r\n"+
						"Date: "+date+ "\r\n" +
						"Server: HTTP Server\r\n"+
						"Connection:close\r\n\r\n";
    	}
    	else
    	{
    		response =  Version+" 304 Not Modified\r\n"+
						"Date: "+date+ "\r\n" +
						"Server: HTTP Server\r\n\r\n";//+
						//"Connection:close\r\n\r\n";
    	}
    	
    	return response.getBytes();
    }
    
    public byte[] Response_501(String date, String Version,boolean cflag)
    {
    	String response;
    	if(cflag == false)
    	{
    		response = 	Version+" 501 Not Implemented\r\n"+
						"Date: "+date+ "\r\n" +
						"Connection: close\r\n\r\n";
    	}
    	else
    	{
    		response = 	Version+" 501 Not Implemented\r\n"+
						"Date: "+date+ "\r\n\r\n";
    	}
    return(response.getBytes());
    }
    
    public String Response_408(String date)
    {
    	String response = 	"HTTP/1.1 408 Request Timeout\r\n" +
    						"Date: "+date+ "\r\n" +
    						"Server: HTTP Server\r\n"+
    						"Content-Type: text/html\r\n" +
    						"Connection:close\r\n"+
    						"Content-Length: 111\r\n\r\n";
    	return response;
    }
    
    public byte[] Response_412(String date, String Version,boolean cflag)
    {
    	String response;
    	if(cflag == false)
    	{		
    				response = Version +" 412 Precondition Failed\r\n"+
    						  "Date: "+date+"\r\n"+
    						  "Connection: close\r\n\r\n";
    	}
    	else
    	{
    		response = Version+" 412 Precondition Failed\r\n"+
    					"Date: "+date+"\r\n\r\n";
    	}
    	
    	return response.getBytes();
    }
    
	public void run()
	{
		serverPort = callingServer.portNo;
		
		while(true)
		{
			try
			{
				clientSocketInfo = callingServer.getSocket();
				clientSocket = clientSocketInfo.getSocket();
				port_num = clientSocketInfo.getPortNum();
				in = clientSocketInfo.getInputReader();
				in1 = clientSocketInfo.getInputReader();
				writer = clientSocketInfo.getOutputWriter();
				ps = clientSocketInfo.getPrintStream();
				isr = clientSocketInfo.getInputStreamReader();
				clientSocket.setSoTimeout(1000000);
				httpResponse.clear();
				httpRequest.clear();
				isServlet = true;
				byteArray = new ByteArrayOutputStream();
				reqArray = new Vector<String>();
				connectionFlag = true;
				statusMap = callingServer.statusMap;
//				System.out.println("Host Address: " +clientSocket.getRemoteSocketAddress().toString());//getInetAddress().getHostAddress());
//				System.out.println("Host Name: "+clientSocket.getInetAddress().getHostName());
//				System.out.println("Client port num: "+clientSocket.getLocalPort());
				
			
				byte[] response;
				
			while(true)
			{
				if(port_num == serverPort)
				{
					{
					//	if(in.ready())
						{
//							Vector <String> reqArray = new Vector<String>(); //Vector to hold the entire request.....
//							String httpVersion = null;
							char[] buffer = new char[2048];
							String str = in.readLine();
							//System.out.println("str: "+str);
							String[] protocol = str.split(" ");
							reqArray.clear();
							StringBuilder requestString = new StringBuilder();
							StringBuilder postBody = new StringBuilder();
							char[] cbuf = null;// = new char[2048];
							int length = 0;
							try
								{
									while(!str.equals("") ) // lastLine != " ")
									//while(in.read(buffer)>-1)
									{
									//  //System.out.println(buffer);
										requestString.append/*(buffer);//*/(str);
										reqArray.add/*(new String(buffer));//*/(str);
										str = in.readLine();
										//System.out.println("str: "+str);
										
										if(str.contains("Content-Length"))
										{
											String[] s1 = str.split("Content-Length:");
											//System.out.println("length: "+s1[1].trim());
											length = Integer.parseInt(s1[1].trim());
											cbuf = new char[length];
										}
										
										if(str.equals(""))
										{
											if(protocol[0].equals("POST"))
											{
											   // //System.out.println("str: "+str);
//												  str = in.readLine();
													//while(in.read(cbuf)!=-1 )//readLine();
													
													{
														in.read(cbuf, 0, length);
														postBody.append(cbuf);
														httpRequest.put("Post Body", postBody.toString());
														//break;
													}
												//System.out.println(" POST STR:" +postBody.toString());
											}
										}
										////System.out.println("STR: "+str);
									}
								}
								catch(Exception e)
								{
									//System.err.println("Exception in if(in.ready()) of run() of TestWorker: "+e.getMessage());
									e.printStackTrace();
									////System.out.println("Terminated connection");
									continue;
								}
							
//							StringBuffer sbuff = new StringBuffer();
//							String[] strArray =  requestString.toString().split("\r\n");
//							//System.out.println("strArray[1]:"+strArray[1]);
//							for (int i=0 ; i < strArray.length -1 ; i++)
//							{
//								//System.out.println("strArray[" +i+ "]: "+strArray[i]);
//								sbuff.append(strArray[i]);
//							}
//							lastLine = sbuff.toString();
							
//							boolean connectionFlag;
							if(lastLine.contains("Connection: close") || callingServer.connectionFlag == 0)
							{
								//System.out.println("CONNNENENNENENEEction");
								connectionFlag = false;
							}
							else
							{
								connectionFlag = true;
							}
							
							lastLine = requestString.toString()+postBody.toString(); 
						    String[] firstLineRequest = /*strArray[0].split(" ");//*/reqArray.get(0).split(" ");
						    date = getDateGMT() ;
						    //System.out.println("LastLine: "+lastLine);
						
							////System.out.println("URL::: "+firstLineRequest[1]+ " handled by: "+Thread.currentThread().getName());
						    String urlHandlerDisplay = "URL: "+firstLineRequest[1]+ " handled by: "+Thread.currentThread().getName();
						 
						    	callingServer.putThreadUrl(Thread.currentThread(),urlHandlerDisplay);
						    						
						    	    
									//*********************************Check for the Malformed Request*****************************************************************
									if(firstLineRequest.length == 3 && (firstLineRequest[2].equals("HTTP/1.1") || firstLineRequest[2].equals("HTTP/1.0"))&& (firstLineRequest[0].equals("GET") || firstLineRequest[0].equals("HEAD")||firstLineRequest[0].equals("POST")))
									{
										if(firstLineRequest[2].equals("HTTP/1.1"))
										{
											httpVersion = "HTTP/1.1";
										}
										else if(firstLineRequest[2].equals("HTTP/1.0"))
										{
											httpVersion = "HTTP/1.0";
										}
										//**********Host Header Present*****************************************************	
										if(!(lastLine.contains("Host:")) && lastLine.contains("HTTP/1.1"))
										{
											
											response  = Response_400(date,httpVersion,connectionFlag);
											ps.write(response);
											if(connectionFlag == false)
											{
												clientSocket.close();
												ps.close();
												writer.close();
												in.close();
												break;
											}
										}
										else
										{
										    try
										    {
												String fileName = firstLineRequest[1];
											 //*************** Secured Access******************************************************
												if(firstLineRequest[1].contains("/.."))
												{
													response = Response_404(date,httpVersion,connectionFlag);
													ps.write(response);											
													if(connectionFlag == false)
													{
														clientSocket.close();
														ps.close();
														writer.close();
														in.close();
														break;
													}
												}
											//***************************Handle SHUT DOWN here**************************************************************
												else if(firstLineRequest[1].equalsIgnoreCase("/shutdown"))
												{
													String response1 = httpVersion+" 200 OK\r\n"+
																		"Date: "+date+ "\r\n" +
																		"Server: HTTP Server\r\n"+
																		"Content-Type: text/html\r\n" +
																		"Connection: close\r\n\r\n"+
																		"<html><head><h1>Server SHUT DOWN</h1></head></html>";
													
													response = response1.getBytes();
													ps.write(response);
													clientSocket.close();
													ps.close();
													in.close();
													writer.close();
													initializeServlet.servletShutDown();
													callingServer.stopServer();
													
												}
												else if(firstLineRequest[1].equals("/control"))
												{
													String response1 = ResponseControl(callingServer.getThreadUrl());
													response = response1.getBytes();
													ps.write(response);
													clientSocket.close();
													ps.close();
													writer.close();
													in.close();
												}
											//********************************************************************************************
												else
												{												
													if(firstLineRequest[0].equals("GET") ||firstLineRequest[0].equals("POST"))
													{
															//**************************Check for If Modified:*******************************
																//System.out.println("HEEELEOOOO" +callingServer.rootDir + fileName);
																
															File f = new File(callingServer.rootDir + fileName);
															//System.out.println("File: "+f.getName());
															long lastModified = f.lastModified();											
															SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss");
															dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
															Date d = new Date(lastModified);
															String modifiedDate = dateFormat.format(d.getTime());
															modifiedDate +=" GMT";
															contentType = URLConnection.guessContentTypeFromName(f.getName());
															contentLength = f.length();
															
														if(f.isFile())
														{
																if(lastLine.contains("If-Modified-Since:"))
																{
																	String[] modifiedTime = lastLine.split("If-Modified-Since:");
																	ifModifiedTime = modifiedTime[1].split("GMT");
																	boolean flag =  isModified(ifModifiedTime[0].trim(), lastModified);
																
																	if(flag == false)
																	{
																		response = 	Response_304(date,httpVersion,connectionFlag);
																		ps.write(response);
																		if(connectionFlag == false)
																		{
																			clientSocket.close();
																			ps.close();
																			writer.close();
																			in.close();
																			break;
																		}
																		//ps.close();
																		//writer.close();
																	}
																	else
																	{
																		response = Response_200(date, modifiedDate,f,httpVersion,connectionFlag);
																		ps.write(response);
																		getFileData(fileName);
																		if(connectionFlag == false)
																		{
																			clientSocket.close();
																			ps.close();
																			writer.close();
																			in.close();
																			break;
																		}
																	}
																}
																else if(lastLine.contains("If-Unmodified-Since:"))
																{
																	String[] modifiedTime = lastLine.split("If-Unmodified-Since:");
																	String [] time = modifiedTime[1].split("GMT");
																	boolean flag = isModified(time[0].trim(), lastModified);
																	
																	if(flag == true)
																	{
																		response = 	Response_412(date,httpVersion,connectionFlag);
																		ps.write(response);
																		ps.close();
																		if(connectionFlag == false)
																		{
																			clientSocket.close();
																			ps.close();
																			writer.close();
																			in.close();
																			break;
																		}
																	}
																	else
																	{
																		response = Response_200(date, modifiedDate,f,httpVersion,connectionFlag);
																		ps.write(response);
																		getFileData(fileName);
																		if(connectionFlag == false)
																		{
																			clientSocket.close();
																			ps.close();
																			writer.close();
																			in.close();
																			break;
																		}
																	}
																}
																else
																{
																	response = Response_200(date, modifiedDate,f,httpVersion,connectionFlag);
																	ps.write(response);																
																	getFileData(fileName);
																	if(connectionFlag == false)
																	{
																		clientSocket.close();
																		ps.close();
																		writer.close();
																		in.close();
																		break;
																	}
																}
															}
															else if(f.isDirectory())
															{
																try
																{
																	String links = readDirectory(firstLineRequest[1],connectionFlag);
																	writer.print(links);
																	writer.flush();
																	//System.out.println("In Directory section");
																	if(connectionFlag == false)
																	{
																		clientSocket.close();
																		ps.close();
																		writer.close();
																		in.close();
																		break;
																	}
																	clientSocket.close();
																}
																catch(NullPointerException nullexcp)
																{
																	response = Response_404(date,httpVersion,connectionFlag);
																	ps.write(response);
																	if(connectionFlag == false)
																	{
																		clientSocket.close();
																		ps.close();
																		writer.close();
																		in.close();
																		break;
																	}
																}
															}
															else
															{
																
																{
																	//System.out.println("SERVLET REQUEST: " +lastLine);
//																
																	
																	String previousS = null;
																	for(String s : reqArray)
																	{
																		if(s.startsWith(" ")||s.startsWith("\t"))
																		{
																			String reqLine[] = previousS.split(":");
																			if(httpRequest.containsKey(reqLine[0]))
																			{
																				StringBuilder sb1 = new StringBuilder();
																				sb1.append(httpRequest.get(reqLine[0]));
																				sb1.append(",");
																				sb1.append(s);
																				httpRequest.put(reqLine[0],sb1.toString());
																			}
																		}
																		else if(s.contains("Host"))
																		{
																			String reqLine[] = s.split(":");
																			httpRequest.put(reqLine[0], reqLine[1]+":"+reqLine[2]);
																		}
																		else if(s.contains(":"))
																		{
																			String reqLine[] = s.split(":");
																			if(httpRequest.containsKey(reqLine[0]))
																			{
																				StringBuilder value = new StringBuilder();
																				value.append(httpRequest.get(reqLine[0]));
																				value.append("<>");
																				value.append(reqLine[1]);
																				httpRequest.put(reqLine[0], value.toString()); 
																			}
																			else
																			{
																				httpRequest.put(reqLine[0],reqLine[1]);
																			}
																			previousS = s;
																		}
																		
																	}
																	
																	TestHarness t  = new TestHarness(this, callingServer, initializeServlet);
																	String xmlPath = callingServer.xmlPath;                                 
																	String args[] = new String[3];
																	String servletName[] = firstLineRequest[1].split("/");
																	args[0] = xmlPath;
																	args[1] = firstLineRequest[0];
																	args[2] = servletName[1].trim();
																	try {
																		t.callServlet(args);
																		if(isServlet == false)
																		{
																			response = Response_404(date,httpVersion,connectionFlag);
																			ps.write(response);
																			if(connectionFlag == false)
																			{
																				
																				ps.close();
																				writer.close();
																				in.close();
																				clientSocket.close();
																				break;
																			}
																		}
																		else if(isServlet == true)
																		{
																			StringBuilder sbServlet = new StringBuilder();
																			String StatusLine = null;
																			if(httpResponse.containsKey("Status Code"))//.equals("200"))
																			{
																				
																				String status = httpResponse.get("Status Code");
																				String message = statusMap.get(status);
																				{
																					StatusLine = httpVersion+" "+status+" "+message+"\r\n";
																				}
																			}
																			else
																			{
																				StatusLine = httpVersion+" 200 OK\r\n";
																			}
																			if(httpResponse.get("Date") == null)
																			{
																				httpResponse.put("Date", getDateGMT());
																			}
																			for(Entry<String, String> responseEntry : httpResponse.entrySet())
																			{
																				if(!responseEntry.getKey().equals("Date") && !responseEntry.getKey().equals("Content-Length") && !responseEntry.getKey().equals("Status Code") && !responseEntry.getKey().equals("Set-Cookie") && !responseEntry.getKey().equals("Set-Cookie-sessionId"))
																				{
																					sbServlet.append(responseEntry.getKey()+": "+responseEntry.getValue()+"\r\n");
																				}
																			}
																			String response1;
																			//**********************************COOKIE*****************************************************
																			if(httpResponse.containsKey("Set-Cookie") || httpResponse.containsKey("Set-Cookie-sessionId"))
																			{
																				String cookie;
																				if(httpResponse.containsKey("Set-Cookie-sessionId") && httpResponse.containsKey("Set-Cookie"))
																				{
																					StringBuilder sb1 = new StringBuilder();
																					sb1.append(httpResponse.get("Set-Cookie"));
																					sb1.append("<>");
																					sb1.append(httpResponse.get("Set-Cookie-sessionId"));
																					httpResponse.put("Set-Cookie", sb1.toString());
																					httpResponse.remove("Set-Cookie-sessionId");
																				}
																				else if(httpResponse.containsKey("Set-Cookie-sessionId"))
																				{
																					httpResponse.put("Set-Cookie", httpResponse.get("Set-Cookie-sessionId"));
																					httpResponse.remove("Set-Cookie-sessionId");
																				}
																				StringBuilder sb = new StringBuilder();
																				if(httpResponse.get("Set-Cookie").contains("<>"))
																				{
																					String strCookie[] = httpResponse.get("Set-Cookie").split("<>");
																					int i=0;
																					for(String s: strCookie)
																					{
																						sb.append("Set-Cookie:"+s+"\r\n");
																					}
																					
																					cookie = sb.toString();
																				}
																				else
																				{
																					cookie = "Set-Cookie:"+httpResponse.get("Set-Cookie")+"\r\n";
																				}
																				if(sbServlet.length() != 0)
																				{
																					if(connectionFlag == false)
																					{
																					response1 = 	StatusLine+//httpVersion+" "+httpResponse.get("Status Code")+" OK\r\n"+
																									"Date: "+httpResponse.get("Date")+ "\r\n" +
																									"Server: HTTP Server\r\n"+
																									"Connection: close\r\n"+//+connectionFlag+"\r\n"+
																									sbServlet.toString()+
																									cookie+
																									"Content-Length: "+byteArray.size()+/*+(byte)fileData.length()+*/ "\r\n\r\n";
																					}
																					else
																					{
																						response1 = 	StatusLine+
																										"Date: "+httpResponse.get("Date")+ "\r\n" +
																										"Server: HTTP Server\r\n"+
																										sbServlet.toString()+
																										cookie+
																										"Content-Length: "+byteArray.size()+"\r\n\r\n";
																					}
																				}
																				else
																				{
																					if(connectionFlag == false)
																					{
																					response1 = 	StatusLine+
																									"Date: "+httpResponse.get("Date")+ "\r\n" +
																									"Server: HTTP Server\r\n"+
																									"Connection: close\r\n"+
																									"Content-Type: "+httpResponse.get("Content-Type")+"\r\n"+
																									cookie+
																									"Content-Length: "+byteArray.size()+"\r\n\r\n";
																					}
																					else
																					{
																						response1 = 	StatusLine+
																										"Date: "+httpResponse.get("Date")+ "\r\n" +
																										"Server: HTTP Server\r\n"+
																										"Content-Type: "+httpResponse.get("Content-Type")+"\r\n"+
																										cookie+
																										"Content-Length: "+byteArray.size()+/*+(byte)fileData.length()+*/ "\r\n\r\n";
																					}
																				}
																			}
																			else
																			{
																				if(sbServlet.length() != 0)
																				{
																					if(connectionFlag == false)
																					{
																						response1 = 	StatusLine+
																										"Date: "+httpResponse.get("Date")+ "\r\n" +
																										"Server: HTTP Server\r\n"+
																										"Connection: close\r\n"+
																										sbServlet.toString()+
																										"Content-Length: "+byteArray.size()+/*+(byte)fileData.length()+*/ "\r\n\r\n";
																						
																					}
																					else
																					{
																						response1 = 	StatusLine+
																										"Date: "+httpResponse.get("Date")+ "\r\n" +
																										"Server: HTTP Server\r\n"+
																										sbServlet.toString()+
																										"Content-Length: "+byteArray.size()+"\r\n\r\n";
																					}
																					
																				}
																				else
																				{   
																					if(connectionFlag == false)
																					{
																						response1 = 	StatusLine+
																										"Date: "+httpResponse.get("Date")+ "\r\n" +
																										"Server: HTTP Server\r\n"+
																										"Connection: close\r\n"+
																										"Content-Type: "+httpResponse.get("Content-Type")+"\r\n"+
																										"Content-Length: "+byteArray.size()+ "\r\n\r\n";
																					}
																					else
																					{
																						response1 = 	StatusLine+
																										"Date: "+httpResponse.get("Date")+ "\r\n" +
																										"Server: HTTP Server\r\n"+
																										
																										"Content-Type: "+httpResponse.get("Content-Type")+"\r\n"+
																										"Content-Length: "+byteArray.size()+ "\r\n\r\n";
																					}
																					
																				}
																			}
																			
																			//////System.out.println("Response: "+response1);
																			ps.write(response1.getBytes());
																			ps.write(byteArray.toByteArray());
																			byteArray.reset();
																			
																			if(connectionFlag == false)
																			{
																				writer.close();
																				in.close();
																				ps.close();
																				clientSocket.close();
																			}
																		}
																		
																	} catch (Exception e) {
																		// TODO Auto-generated catch block
																		e.printStackTrace();
																	}
																}											
															}	
														}
													//*******************HEAD Request********************************************
														else if(firstLineRequest[0].equals("HEAD"))
														{
															File f = new File(callingServer.rootDir + fileName);
															long lastModified = f.lastModified();// CHECK ***Last-Modified: Thu, 01 Jan 1970 00:00:00 GMT**************************
															
															SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss");
															dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
															Date d = new Date(lastModified);
															String modifiedDate = dateFormat.format(d.getTime());
															modifiedDate +=" GMT";
															String response1;
															if(connectionFlag == true)
															{
																	response1 = 	httpVersion+" 200 OK\r\n"+
																					"Date: "+date+ "\r\n" +
																					"Server: HTTP Server\r\n"+
																					"Last-Modified: "+modifiedDate+"\r\n"+
																					"Content-Type: " +URLConnection.guessContentTypeFromName(f.getName())+/*text/html*/"\r\n"+
																					"Content-Length: "+ f.length()+"\r\n\r\n";
															}
															else
															{
																	response1 = 	httpVersion+" 200 OK\r\n"+
																					"Date: "+date+ "\r\n" +
																					"Server: HTTP Server\r\n"+
																					"Last-Modified: "+modifiedDate+"\r\n"+
																					"Connection: close\r\n"+//+connectionFlag+"\r\n"+
																					"Content-Type: " +URLConnection.guessContentTypeFromName(f.getName())+/*text/html*/"\r\n"+
																					"Content-Length: "+ f.length()+"\r\n\r\n";
															}
															
															
															response = response1.getBytes();
															ps.write(response);
															if(connectionFlag == false)
															{
																clientSocket.close();
																ps.close();
																writer.close();
																in.close();
															}
														}
													//***********************************************************************************
														else
														{
															response = Response_501(date,httpVersion,connectionFlag);
															ps.write(response);
															if(connectionFlag == false)
															{
																clientSocket.close();
																ps.close();
																writer.close();
																in.close();
																break;
															}

														}
													}
											}
										    catch(SocketException excp)
											{
												System.err.println("SocketException in  lastLine.contains(\".html\"): "+excp.getMessage());
												excp.printStackTrace();
											}
										  //*********************************** 404 File Not Found ***************************************************************************
										    catch(FileNotFoundException excp)
										    {
										    	response = Response_404(date,httpVersion,connectionFlag);
												ps.write(response);
												
												if(connectionFlag == false)
												{
													clientSocket.close();
													ps.close();
													writer.close();
													in.close();
													break;
												}
												
										    }
										}   
									}
									else
									{	//************************************** 400 Malformed Request****************************************************************** 
										response =Response_400(date, "HTTP/1.1",connectionFlag );//httpVersion);
										ps.write(response);
										if(connectionFlag == false)
										{
											clientSocket.close();
											ps.close();
											writer.close();
											in.close();
											break;
										}
								
									
									}
							}
					}
				}
			}
			}
			catch(ArrayIndexOutOfBoundsException exc)
			{
				date = getDateGMT();
				byte[] response1 =Response_400(date ,"HTTP/1.1",connectionFlag );//httpVersion);
				try {
					if(connectionFlag == false)
					{
						clientSocket.close();
						ps.close();
						writer.close();
						in.close();
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//************************ 408 Request Time Out******************************************************************************** 
			catch(SocketTimeoutException soc_excp)
			{
				date = getDateGMT();
				String response = Response_408(date);
				writer.print(response);
				writer.flush();
				try {
					clientSocket.close();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
			catch(IOException ioe)
			{
			}
			catch(NullPointerException nexcp)
			{
				break;
			}

		}

	}
}
