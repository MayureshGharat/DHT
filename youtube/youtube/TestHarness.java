package edu.upenn.cis.cis555.youtube;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServlet;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.tools.ant.taskdefs.PathConvert.MapEntry;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Todd J. Green, modified by Nick Taylor
 */
public class TestHarness {	
	public TestWorker callingTestWorker;
	public HttpServer callingServer;
	public LoadServlet initializeServlet;
	public HashMap<String, ArrayList<String>> m_params = new HashMap<String, ArrayList<String>>(); 

	TestHarness(TestWorker t, HttpServer s, LoadServlet ls)
	{
		callingTestWorker = t;
		callingServer = s;
		initializeServlet = ls;
	}
	
	private static void usage() {
		System.err.println("usage: java TestHarness <path to web.xml> " 
				+ "[<GET|POST> <servlet?params> ...]");
	}

	
	public void callServlet(String[] args) throws Exception {
		if (args.length < 3 || args.length % 2 == 0) {
			usage();
			System.exit(-1);
		}

		String[] firstLine = callingTestWorker.reqArray.firstElement().split(" ");
		boolean queryflag = false;
		if(firstLine[1].contains("?") || callingTestWorker.httpRequest.containsKey("Post Body"))
		{
			String queryString = null;
			System.out.println("Query: "+firstLine[1]);
			if(firstLine[1].contains("?"))
			{
				queryflag = true;
				String[] url = firstLine[1].split("\\?");
				queryString = url[1];
			}
			else if(callingTestWorker.httpRequest.containsKey("Post Body"))
			{
				if(callingTestWorker.httpRequest.get("Post Body").contains("="))
				{
					queryflag = true;
					queryString = callingTestWorker.httpRequest.get("Post Body").trim();
					System.out.println("Query String: "+callingTestWorker.httpRequest.get("Post Body"));
				}
			}
			if(queryflag == true)
			{
				ArrayList <String> strList= new ArrayList<String>(); 
				//String[] strArray = new String[queryString.length()];
				if(queryString.contains("&"))
				{
					String[] strArray = queryString.split("&");
					for(String s : strArray)
					{
						strList.add(s);
					}
				}
				else
				{
					strList.add(queryString);
				}
				for(String s :strList)
				{
					System.out.println("s: "+s);
					if(s.equals("") || s == (null))
					{
						break;
					}
					String[] nameValue = s.split("=");
					String name = nameValue[0];
					/*
					 * HEEELLLOOOOOOO
					 */
					//String value1 = nameValue[1];
					String value;
					if(nameValue.length > 1)
					{
						 value = URLDecoder.decode(nameValue[1],"UTF-8");
					}
					else
					{
						value = "";
					}
					if(m_params.containsKey(name))
					{
						ArrayList<String> stringList = m_params.get(name);
						stringList.add(value);
						m_params.put(name, stringList);
					}
					else
					{
						ArrayList<String> list = new ArrayList<String>();
						list.add(value);
						m_params.put(name, list);
					}
				}
			}
		}
		
		FakeSession fs = null;
		if(callingTestWorker.httpRequest.containsKey("Cookie"))
		{
			String str = callingTestWorker.httpRequest.get("Cookie"); 
			if(str.contains("SessionID"))
			{
				String[] sessionArray = str.split("SessionID=");
				String sessionId;
				if(sessionArray[1].contains(";"))
				{
					String[] sessionIdvalue = sessionArray[1].split(";");
					sessionId = sessionIdvalue[0];
				}
				else
				{
					sessionId = sessionArray[1];
				}
				if(callingServer.getSessionID(sessionId) != null)
				{
					fs = callingServer.getSessionID(sessionArray[1]);
					fs.lastAccessedTime= System.currentTimeMillis();
				}
			}
		}
		
		for (int i = 1; i < args.length - 1; i += 2) {
			String[] strings = args[i+1].split("\\?|&|=");
			//System.out.println("strings[0]: "+strings[0]);
			FakeConfig fc =  new FakeConfig(strings[0],initializeServlet.context);
			FakeRequest request = new FakeRequest(fs, callingTestWorker,callingServer,fc,m_params);
			FakeResponse response = new FakeResponse(callingTestWorker);
			String[] url = callingTestWorker.reqArray.firstElement().split(" ");
			String[] exactUrl = new String[1];
			String[] sName = new String[1];
			if(url[1].contains("?"))
			{
				exactUrl = url[1].split("\\?");
				System.out.println("Exact UrL ?: "+ exactUrl[0]);
				sName = exactUrl[0].split("/");
			}
			else
			{
				System.out.println("URL[1]: "+url[1]);
				exactUrl[0] = url[1];
				sName = exactUrl[0].split("/");
				System.out.println("Exact UrL : "+ sName[1]);
			}
			
			
			HttpServlet servlet = initializeServlet.servlets.get(strings[0]);
			if (servlet == null) {
				HttpServer.logger.error("error: cannot find mapping for servlet \n"+ strings[0]);
				System.err.println("error: cannot find mapping for servlet " + strings[0]);
				//boolean variable
				callingTestWorker.isServlet = false;
				return;
				//System.exit(-1);
				//return;
			}
			else
			{
				for (int j = 1; j < strings.length - 1; j += 2) {
					request.setParameter(strings[j], strings[j+1]);
				}
				if (args[i].compareTo("GET") == 0 || args[i].compareTo("POST") == 0) {
					request.setMethod(args[i]);
					servlet.service(request, response);
				} else {
					HttpServer.logger.error("error: expecting 'GET' or 'POST', not '" + args[i] + "'");
					System.err.println("error: expecting 'GET' or 'POST', not '" + args[i] + "'");
					usage();
					System.exit(-1);
				}
				
				fs = (FakeSession) request.getSession(false);
			}
		}
	}
}
 
