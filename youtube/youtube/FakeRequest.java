package edu.upenn.cis.cis555.youtube;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.Vector;

import javax.net.ssl.SSLSocketFactory;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.io.*;

/**
 * @author Todd J. Green
 */
public class FakeRequest implements HttpServletRequest {
public TestWorker callingTestWorker;
public HttpServer callingServer;
public FakeConfig fc;
HashMap <String, String> urlPatternMap = new HashMap<String, String>();
private HashMap<String, ArrayList<String>> m_params = new HashMap<String, ArrayList<String>>();
private Properties m_props = new Properties();
private FakeSession m_session = null;
private String m_method;
private String characterEncoding ="";


	FakeRequest() {
	}
	
	
	public FakeRequest(FakeSession session, TestWorker t, HttpServer server, FakeConfig fc, HashMap<String, ArrayList<String>> map) {
		m_session = session;
		callingTestWorker = t;
		callingServer = server;
		this.fc = fc;
		this.urlPatternMap = callingServer.urlPatternMap;
		this.m_params = map;
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getAuthType()
	 */
	public String getAuthType() {
		return "BASIC";
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getCookies()
	 */
	public Cookie[] getCookies() {
		if(callingTestWorker.httpRequest.containsKey("Cookie"))
		{
			String cookieString = callingTestWorker.httpRequest.get("Cookie");
			if(cookieString.contains(";"))
			{
				 String[] cookiePair = cookieString.split(";");
				 Cookie[] cookieArray = new Cookie[cookiePair.length];
				 Vector<Cookie> cookieVector = new Vector<Cookie>();
				 for(String str : cookiePair)
				 {
					 String[] cookieNameValue =  str.split("=");
					 Cookie c  = new Cookie(cookieNameValue[0].trim(),cookieNameValue[1]);
					 cookieVector.add(c);
				 }
				 int i=0;
				 for(Cookie c : cookieVector)
				 {
					 cookieArray[i] = c;
					 i++;
				 }
				 return cookieArray;
			}
			else
			{
				String[] cookieNameValue = cookieString.trim().split("=");
				System.out.println(" Cookie Name: "+cookieNameValue[0]+ " CookieValue: "+cookieNameValue[1]);
				Cookie c = new Cookie(cookieNameValue[0],cookieNameValue[1]);
				Cookie[] cookieArray = new Cookie[1];
				cookieArray[0] = c;
				return cookieArray;
			}
		}
		else
		{
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getDateHeader(java.lang.String)
	 */
	public long getDateHeader(String requestDateHeader) 
	{
		if(callingTestWorker.httpRequest.containsKey(requestDateHeader))
		{
			String dateString = callingTestWorker.httpRequest.get("If-Modified-Since");
			SimpleDateFormat dateFormat = null;
			if(dateString.contains(","))
			{
				if(dateString.contains("-"))
				{	
					
					dateFormat = new SimpleDateFormat("E, dd-MMM-yy HH:mm:ss");
					dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
				}
				else
				{
					dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
				}
			}
			else
			{
				dateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss yyyy");
				dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
			}
			Date convertedDate = null;
			try {
				convertedDate = dateFormat.parse(dateString);
			} 
			catch (ParseException e) {
				HttpServer.logger.error(e);
				System.out.println("Illegal Argument Exception");
				e.printStackTrace();
			}
			
			return convertedDate.getTime();
		}
		else
		{
			return -1;
		}
	}


	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getHeader(java.lang.String)
	 */
	public String getHeader(String header) {
		
		if(callingTestWorker.httpRequest.containsKey(header))
		{
			return callingTestWorker.httpRequest.get(header);
		}
		else
		{
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getHeaders(java.lang.String)
	 */
	public Enumeration getHeaders(String header) {
		Enumeration strEnum = null;
		ArrayList<String> stringList = new ArrayList<String>();
		String[] strArray = null;
		if(callingTestWorker.httpRequest.containsKey(header))
		{
			String s = callingTestWorker.httpRequest.get(header);
			if(s.contains(","))
			{
				strArray = s.split(",");
			
				for(String str : strArray)
				{
					stringList.add(str);
				}
				strEnum = Collections.enumeration(stringList);
			}
			else
			{
				stringList.add(s);
				strEnum = Collections.enumeration(stringList);
			}
		}	
			return strEnum;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getHeaderNames()
	 */
	public Enumeration getHeaderNames() 
	{
		 Enumeration<String> strEnum = null;
		 if(callingTestWorker.httpRequest.size() > 0)
		 {
			 HashMap<String,String> map = new HashMap<String, String>();
			 map = callingTestWorker.httpRequest;
			 if(map.containsKey("Post Body"))
			 {
				map.remove("Post Body"); 
			 }
			 
			 strEnum = Collections.enumeration(map.keySet());
		 }
		 return strEnum;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getIntHeader(java.lang.String)
	 */
	public int getIntHeader(String header) {
		int headerValue = 0;
		try
		{
			if(callingTestWorker.httpRequest.containsKey(header))
			{
				headerValue = Integer.parseInt(callingTestWorker.httpRequest.get(header));
			
			}
			else {
				headerValue = -1;
			}
		}
		catch(NumberFormatException nexcp)
		{
		   HttpServer.logger.error(nexcp);
		   nexcp.printStackTrace();
		}
		return headerValue;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getMethod()
	 */
	public String getMethod() {
		return m_method;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getPathInfo()
	 */
	public String getPathInfo() {
		String urlPattern = urlPatternMap.get(fc.getServletName());
		String[] url = callingTestWorker.reqArray.firstElement().split(" ");
		if(urlPattern.contains("/*"))
		{
			String[] exactUrl = urlPattern.split("/\\*");
			String[] s = url[1].split(exactUrl[0]);
			if(s[1].contains("?"))
			{
				String[] str = s[1].split("\\?");
				return str[0];
			}
			else 
			{
				return s[1];
			}
			
		}
		else
		{

			return null;
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getPathTranslated()
	 */
	public String getPathTranslated() {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getContextPath()
	 */
	public String getContextPath() {
		return "";
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getQueryString()
	 */
	public String getQueryString() {
		String firstLine[] = callingTestWorker.reqArray.firstElement().split(" ");
		if(firstLine[1].contains("?") && firstLine[0].equals("GET"))
		{
			   String strArray[] = firstLine[1].split("?");
			   return strArray[1];
		}
		else if(firstLine[0].contains("POST"))
		{
			if(callingTestWorker.httpRequest.containsKey("Post Body"))
			{
				return callingTestWorker.httpRequest.get("Post Body");
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
	}
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getRemoteUser()
	 */
	public String getRemoteUser() {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#isUserInRole(java.lang.String)
	 */
	public boolean isUserInRole(String arg0) {
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getUserPrincipal()
	 */
	public Principal getUserPrincipal() {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getRequestedSessionId()
	 */
	public String getRequestedSessionId() {
		if(callingTestWorker.httpRequest.containsKey("Cookie"))
		{
			String str = callingTestWorker.httpRequest.get("Cookie"); 
			if(str.contains("SessionID"))
			{
				String[] sessionArray = str.split("SessionID=");
				if(sessionArray[1].contains(";"))
				{
					String[] sessionIdvalue = sessionArray[1].split(";");
					return sessionIdvalue[0];
				}
				else
				{
					return sessionArray[1];
				}
			}
			else 
			{
				return null;
			}
		}
		else
		{
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getRequestURI()
	 */
	public String getRequestURI() {
		String[] url = callingTestWorker.reqArray.firstElement().split(" ");
		String[] strArray = url[1].split("?");
		return strArray[0];
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getRequestURL()
	 */
	public StringBuffer getRequestURL() 
	{
		String scheme = "http";
		String serverName = getServerName();
		int serverPort = getServerPort();
		String contextPath = getContextPath();
		
		String str[] = callingTestWorker.reqArray.firstElement().split(" ");
		
		String url = scheme+"://"+callingTestWorker.httpRequest.get("Host")+""+str[1];
		
		StringBuffer sb = new StringBuffer(url);
		return sb;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getServletPath()
	 */
	public String getServletPath() {
		String urlPattern = urlPatternMap.get(fc.getServletName());
		String[] url = callingTestWorker.reqArray.firstElement().split(" ");
		if(!urlPattern.equals("/*"))
		{
			String pathInfo = getPathInfo();
			String[] url_1 = callingTestWorker.reqArray.firstElement().split(" ");
			
			if(pathInfo != null)
			{
				String[] servletPath = url_1[1].split(pathInfo);
				return servletPath[0];
			}
			else
			{
				return urlPattern;
			}
		}
		else
		{
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getSession(boolean)
	 */
	public HttpSession getSession(boolean arg0) {
		if (arg0) {
			if (! hasSession()) {
				m_session = new FakeSession(callingTestWorker,callingServer,fc.context);
			}
		} else {
			if (! hasSession()) {
				m_session = null;
			}
		}
		return m_session;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getSession()
	 */
	public HttpSession getSession() {
		return getSession(true);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdValid()
	 */
	public boolean isRequestedSessionIdValid() {
		if(m_session.isValid() == false)
		{
			callingServer.removeSession(m_session.getId());
		}
		return m_session.isValid();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromCookie()
	 */
	public boolean isRequestedSessionIdFromCookie() {
		return true;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromURL()
	 */
	public boolean isRequestedSessionIdFromURL() {
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromUrl()
	 */
	public boolean isRequestedSessionIdFromUrl() {
		//deprecated
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String arg0) {
		return m_props.get(arg0);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getAttributeNames()
	 */
	public Enumeration getAttributeNames() {
		// TODO Auto-generated method stub
		return m_props.keys();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getCharacterEncoding()
	 */
	public String getCharacterEncoding() {
		if(characterEncoding != "")
		{
			return characterEncoding;
		}
		else
		{
			return "ISO-8859-1";
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#setCharacterEncoding(java.lang.String)
	 */
	public void setCharacterEncoding(String encoding)
			throws UnsupportedEncodingException {
		characterEncoding = encoding;

	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getContentLength()
	 */
	public int getContentLength() {
		if(callingTestWorker.httpRequest.containsKey("Content-Length"))
		{
		int requestLine = Integer.parseInt(callingTestWorker.httpRequest.get("Content-Length"));
		return requestLine;
		}
		else
		{
			return -1;
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getContentType()
	 */
	public String getContentType() {
		if(callingTestWorker.httpRequest.containsKey("Content-Length"))
		{
			return callingTestWorker.httpRequest.get("Content-Length");
		}
		else
		{
			return "test/html";
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getInputStream()
	 */
	public ServletInputStream getInputStream() throws IOException {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getParameter(java.lang.String)
	 */
	public String getParameter(String paramName) {
		ArrayList<String> list = new ArrayList<String>();
		list = m_params.get(paramName);
		return list.get(0);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getParameterNames()
	 */
	public Enumeration getParameterNames() {
		return (Enumeration) m_params.keySet();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getParameterValues(java.lang.String)
	 */
	public String[] getParameterValues(String key) {
		if(m_params.containsKey(key))
		{
			ArrayList<String> list = new ArrayList<String>();
			list = m_params.get(key);
			String[] str = new String[list.size()];
			int i=0;
			for(String s: list)
			{
				str[i] = s;
				i++;
			}
			return str;
		}
		else
		{
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getParameterMap()
	 */
	
	
	public Map getParameterMap() {
		HashMap<String, String[]> parameterMap = new HashMap<String, String[]>();
		for(Map.Entry mapEntry : m_params.entrySet())
		{
			String paramName = (String) mapEntry.getKey();
			ArrayList<String> list = (ArrayList<String>) mapEntry.getValue();
			int i=0;
			String[] str = new String[list.size()];
			for(String s : list)
			{
				str[i] = s;
				i++;
			}
			parameterMap.put(paramName, str);
		}
		return parameterMap;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getProtocol()
	 */
	public String getProtocol() {
		String[] str = callingTestWorker.reqArray.firstElement().split(" ");
		return str[0];
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getScheme()
	 */
	public String getScheme() {
		return "http";
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getServerName()
	 */
	public String getServerName() {
		if(callingTestWorker.httpRequest.containsKey("Host"))
		{
			String[] serverName = callingTestWorker.httpRequest.get("Host").split(":");
			return (serverName[0]);
		}
		else
		{
			return getLocalName(); 
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getServerPort()
	 */
	public int getServerPort() {
		String[] serverPort = callingTestWorker.httpRequest.get("Host").split(":");
		return (Integer.parseInt(serverPort[1]));
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getReader()
	 */
	public BufferedReader getReader() {
		
		BufferedReader br = null;
		try
		{
			String s = callingTestWorker.httpRequest.get("Post Body");
			InputStream is = new ByteArrayInputStream(s.getBytes());
			 br = new BufferedReader(new InputStreamReader(is));
			return br;
		}
		catch(Exception e)
		{
			HttpServer.logger.error(e);
			return br;
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getRemoteAddr()
	 */
	public String getRemoteAddr() {
		String clientIpAddress = callingTestWorker.clientSocket.getRemoteSocketAddress().toString();//getInetAddress().getHostAddress(); 
		return clientIpAddress;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getRemoteHost()
	 */
	public String getRemoteHost() {
		String clientHost = callingTestWorker.clientSocket.getInetAddress().getHostName();
		return clientHost;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#setAttribute(java.lang.String, java.lang.Object)
	 */
	public void setAttribute(String arg0, Object arg1) {
		m_props.put(arg0, arg1);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#removeAttribute(java.lang.String)
	 */
	public void removeAttribute(String arg0) {
		m_props.remove(arg0);

	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getLocale()
	 */
	public Locale getLocale() {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getLocales()
	 */
	public Enumeration getLocales() {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#isSecure()
	 */
	public boolean isSecure() {
		String s = getScheme();
		if(s.equalsIgnoreCase("https"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getRequestDispatcher(java.lang.String)
	 */
	public RequestDispatcher getRequestDispatcher(String arg0) {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getRealPath(java.lang.String)
	 */
	public String getRealPath(String str) {
		String realPath;
		try
		{
			String[] vPath = str.split("/");
			String fileName = vPath[vPath.length-1];
			File f = new File(fileName);
			realPath = f.getCanonicalPath();
		}
		catch(Exception e)
		{
			HttpServer.logger.error(e);
			return null;
		}
		return realPath;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getRemotePort()
	 */
	public int getRemotePort() {
		int port_num = callingTestWorker.clientSocket.getPort(); 
		return port_num;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getLocalName()
	 */
	public String getLocalName() {
		String localName = callingTestWorker.clientSocket.getInetAddress().getHostName();
		return localName;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getLocalAddr()
	 */
	public String getLocalAddr() {
		String localAddr = callingTestWorker.clientSocket.getLocalAddress().toString();
		return localAddr;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getLocalPort()
	 */
	public int getLocalPort() {
		int localPort = callingTestWorker.clientSocket.getLocalPort();
		return localPort;
	}

	void setMethod(String method) {
		m_method = method;
	}
	
	void setParameter(String key, String value) {
		if(m_params.containsKey(key))
		{
			ArrayList<String> list = new ArrayList<String>();
			list = m_params.get(key);
			list.add(value);
			m_params.put(key, list);
		}
		else
		{
			ArrayList<String> list = new ArrayList<String>();
			list.add(value);
			m_params.put(key, list);
		}
		
	}
	
	void clearParameters() {
		m_params.clear();
	}
	
	boolean hasSession() {
		return ((m_session != null) && m_session.isValid());
	}
}

