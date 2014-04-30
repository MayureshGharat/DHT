package edu.upenn.cis.cis555.youtube;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.servlet.http.HttpServlet;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;


public class LoadServlet {
	Handler h;
	FakeContext context;
	HashMap<String,HttpServlet> servlets;
	HashMap<String, String> urlPatternMap;
	HashMap<String,String> statusMap;
	
	LoadServlet(String xmlPath) 
	{
		h = parseWebdotxml(xmlPath);
		context = createContext(h);
		servlets = createServlets(h, context);
		urlPatternMap = getUrlPatternMap(h);
		statusMap = loadStatusMap();
	}
	
	public HashMap<String, String> loadStatusMap()
	{
		HashMap<String,String> map = new HashMap<String, String>();
		map.put("101","Switching Protocols");
		map.put("200","OK");
		map.put("201","Created");
		map.put("202","Accepted");
		map.put("203","Non-Authoritative Information");
		map.put("204","No Content");
		map.put("205","Reset Content");
		map.put("206","Partial Content");
		map.put("300","Multiple Choices");
		map.put("301","Moved Permanently");
		map.put("302","Found");
		map.put("303","See Other");
		map.put("304","Not Modified");
		map.put("305","Use Proxy");
		map.put("400","Bad Request");
		map.put("401","Unauthorized");
		map.put("402","Payment Required");
		map.put("403","Forbidden");
		map.put("404","Not Found");
		map.put("405","Method Not Allowed");
		map.put("406","Not Acceptable");
		map.put("407","Proxy Authentication Required");
		map.put("408","Request Time-out");
		map.put("409","Conflict");
		map.put("410","Gone");
		map.put("411","Length Required");
		map.put("412","Precondition Failed");
		map.put("413","Request Entity Too Large");
		map.put("414","Request-URI Too Large");
		map.put("415","Unsupported Media Type");
		map.put("416","Requested range not satisfiable");
		map.put("417","Expectation Failed");
		map.put("500","Internal Server Error");
		map.put("501","Not Implemented");
		map.put("502","Bad Gateway");
		map.put("503","Service Unavailable");
		map.put("504","Gateway Time-out");
		map.put("505","HTTP Version not supported");

		return map;
	}
	
	static class Handler extends DefaultHandler {
		public void startElement(String uri, String localName, String qName, Attributes attributes) {
			if (qName.compareTo("servlet-name") == 0) {
				m_state = (m_state == 100) ? 101 : 1;
				//System.out.println("servlet-name:"+ qName);
				//m_state = 1;
			} 
			else if(qName.compareTo("url-pattern") == 0)
			{
				if(m_state == 101)
				{
					m_state = 103;
				}
			}
			else if (qName.compareTo("servlet-class") == 0) {
				m_state = (m_state == 1) ? 2 :102;
				//System.out.println("servlet-class:"+ qName +" Servlet Name: "+m_servletName);
				//m_state = 2;
			} else if (qName.compareTo("context-param") == 0) {
				//System.out.println("context-param:"+ qName);
				m_state = 3;
			} else if (qName.compareTo("init-param") == 0) {
				//System.out.println("init-param:"+ qName);
				m_state = 4;
			} else if (qName.compareTo("param-name") == 0) {
				//System.out.println("param-name:"+ qName);
				m_state = (m_state == 3) ? 10 : 20;
			} else if (qName.compareTo("param-value") == 0) {
				//System.out.println("param-value:"+ qName);
				m_state = (m_state == 10) ? 11 : 21;
			}
			else if(qName.compareTo("servlet-mapping") == 0)
			{
				m_state =100;
				//System.out.println("m_state = 100"); 
			}
		}
		public void characters(char[] ch, int start, int length) {
			String value = new String(ch, start, length);
			//System.out.println("VALUEEEEE: "+value);
//			if(m_state == 100)
//			{
//				System.out.println("m_state = 100 :"+value);
//			//	m_state=0;
//			}
		 if ((m_state == 1 || m_state == 101) && nameFlag == false ) {
				m_servletName = value;
//				if(m_state != 101)
//				{
//					//System.out.println("m_state 101: "+value);
//					//m_state = 0;
//				}
//				else
				{
					System.out.println("In Servlet Name: "+value);
				}
			nameFlag = true;
			} else if (m_state == 2) {
				m_servlets.put(m_servletName, value);
			//	System.out.println(" Servlet Name: "+ m_servletName+" m_state =2 :" +value);
				m_state = 0;
				nameFlag = false;
			} else if (m_state == 10 || m_state == 20) {
				m_paramName = value;
				//System.out.println("m_paramName vlue: "+value);
			} else if (m_state == 11) {
				if (m_paramName == null) {
					System.err.println("Context parameter value '" + value + "' without name");
					System.exit(-1);
				}
				//System.out.println("m_param vlue: "+value);
				m_contextParams.put(m_paramName, value);
				m_paramName = null;
				m_state = 0;
			} else if (m_state == 21) {
				if (m_paramName == null) {
					System.err.println("Servlet parameter value '" + value + "' without name");
					System.exit(-1);
				}
				HashMap<String,String> p = m_servletParams.get(m_servletName);
				if (p == null) {
					p = new HashMap<String,String>();
					m_servletParams.put(m_servletName, p);
				}
				p.put(m_paramName, value);
				m_paramName = null;
				m_state = 0;
			}
			else if(m_state == 103)
			{
			//	System.out.println("hahhhaha");
				if(m_servletName == null)
				{
					System.err.println("Servlet url pattern value '" + value + "' without Servlet name");
					System.exit(-1);
				}
				urlPatternMap.put(m_servletName,value);
				m_servletName =null;
			//	System.out.println( "URL PATTERN: "+ value);
				m_state =0;
				nameFlag = false;
			}
			
		}
		private int m_state = 0;
		private boolean nameFlag;
		private String m_servletName;
		private String m_paramName;
		HashMap<String,String> m_servlets = new HashMap<String,String>();
		HashMap<String,String> m_contextParams = new HashMap<String,String>();
		HashMap<String,HashMap<String,String>> m_servletParams = new HashMap<String,HashMap<String,String>>();
		HashMap<String, String> urlPatternMap = new HashMap<String, String>();
  	}
	private static HashMap<String,HttpServlet> createServlets(Handler h, FakeContext fc){
		HashMap<String,HttpServlet> servlets = new HashMap<String,HttpServlet>();
		for (String servletName : h.m_servlets.keySet()) {
			FakeConfig config = new FakeConfig(servletName, fc);
			String className = h.m_servlets.get(servletName);
		try
		{
			Class servletClass = Class.forName(/*"edu.upenn.cis.cis555.webserver."+*/className);
			HttpServlet servlet = (HttpServlet) servletClass.newInstance();
			HashMap<String,String> servletParams = h.m_servletParams.get(servletName);
			if (servletParams != null) {
				for (String param : servletParams.keySet()) {
					config.setInitParam(param, servletParams.get(param));
				}
			}
			servlet.init(config);
			servlets.put(servletName, servlet);
		}
		catch(Exception e)
		{
			HttpServer.logger.error(e.getMessage());
			System.err.println("Exception in createServlets of LoadServlet: "+e.getMessage());
			e.printStackTrace();
		}
			
		}
		return servlets;
	}
	
	public HashMap<String, String> getUrlPatternMap (Handler h)
	{
		return h.urlPatternMap;
	}
	
	private static FakeContext createContext(Handler h) {
		FakeContext fc = new FakeContext();
		for (String param : h.m_contextParams.keySet()) {
			fc.setInitParam(param, h.m_contextParams.get(param));
		}
		return fc;
	}
	
	private static Handler parseWebdotxml(String webdotxml) {
		Handler h = new Handler();
		File file = new File(webdotxml);
		if (file.exists() == false) {
			System.err.println("error: cannot find " + file.getPath());
			System.exit(-1);
		}
		SAXParserFactory factory = SAXParserFactory.newInstance();
	//	factory.setValidating(arg0)
		factory.setValidating(false);
		try
		{
		SAXParser parser = /*SAXParserFactory.newInstance()*/factory.newSAXParser();
		parser.parse(file, h);
		}
		catch(Exception e)
		{
			HttpServer.logger.error(e.getCause());
			System.err.println("Exception in parseWebdotxml of LoadServlet : "+e.getMessage());
			e.printStackTrace();
		}
		return h;
	}

	public HashMap<String, String> getPatternMap() {
		
		return urlPatternMap;
	}
	
	
	
	public void servletShutDown()
	{
		for(Entry<String, HttpServlet> s : servlets.entrySet() )
		{
			s.getValue().destroy();
		}
	}
	
}
