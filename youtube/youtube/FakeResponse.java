package edu.upenn.cis.cis555.youtube;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author tjgreen
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FakeResponse implements HttpServletResponse {
	
	public TestWorker callingTestWorker;
	boolean isCommitedFlag;
	boolean localeFlag;
	Locale locale  = Locale.US;

	FakeResponse(){}
	
	public FakeResponse(TestWorker testWorker) {
		callingTestWorker = testWorker;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponse#addCookie(javax.servlet.http.Cookie)
	 */
	public void addCookie(Cookie cookie) {
		Cookie c = cookie;
		String name = c.getName();
		String value = c.getValue();
		String Domain = c.getDomain();
		int maxAge = c.getMaxAge();
		String path = c.getPath();
		boolean secure = c.getSecure();
		String date = null ;
		
		StringBuilder sb1 = new StringBuilder();
		if(maxAge != -1)
		{
			long expireDate = System.currentTimeMillis() +1000*maxAge;
			SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss");
			dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
			Date d = new Date(expireDate);
			date= dateFormat.format(d.getTime());
			date +=" GMT";
		}
		sb1.append(name+"="+value+"; expires="+date);
		
		if(path!=null && path!="")
		{
			sb1.append("; path="+path);
		}
		
		
		if(Domain != null && Domain != "")
		{
			sb1.append("; Domain="+Domain);
		}
		String mapValue = sb1.toString();
				
		if(callingTestWorker.httpResponse.containsKey("Set-Cookie"))
		{
			if(callingTestWorker.httpResponse.get("Set-Cookie").contains("<>"))
			{
				String[] cookieStrings = callingTestWorker.httpResponse.get("Set-Cookie").split("<>");
				int i=0;
				for(String s : cookieStrings)
				{
					if(s.contains(name))
					{
						if(s.contains("path"))
						{
							String[] str = s.split(";");
							String[] strPathValue = str[2].split("=");
							String[] strNameValue = str[0].split("=");
							if(strPathValue[1].equals(path) && strNameValue[0].equals(name))
							{
								cookieStrings[i] = mapValue;
							}
							else if(strNameValue[0].equals(name) && !strNameValue[1].equals(value))
							{
								StringBuilder sb = new StringBuilder();
								sb.append(cookieStrings[i]);
								sb.append("<>");
								sb.append(mapValue);
								cookieStrings[i] = sb.toString();
							}
							i++;
						}
						else if(!s.contains("path"))
						{
							String[] str = s.split(";");
							String[] strNameValue = str[0].split("=");
							if(strNameValue[0].equals(name) && !strNameValue[1].equals(value))
							{
								StringBuilder sb = new StringBuilder();
								sb.append(cookieStrings[i]);
								sb.append("<>");
								sb.append(mapValue);
								cookieStrings[i] = sb.toString();
							}
							i++;
						}
					}
					else
					{
						StringBuilder sb = new StringBuilder();
						sb.append(cookieStrings[i]);
						sb.append("<>");
						sb.append(mapValue);
						cookieStrings[i] = sb.toString();
						i++;
					}
				}
				StringBuilder sb_1 = new StringBuilder();
				int l=0;
				for(String s1 : cookieStrings)
				{
					sb_1.append(s1);
					if(l != cookieStrings.length-1)
					{
						sb_1.append("<>");
					}
					l++;
				}
				callingTestWorker.httpResponse.put("Set-Cookie", sb_1.toString());
			}
			else
			{
				String s = callingTestWorker.httpResponse.get("Set-Cookie");
				if(s.contains(name))
				{
					if(s.contains("path"))
					{
						String[] str = s.split(";");
						String[] strPathValue = str[2].split("=");
						String[] strNameValue = str[0].split("=");
						if(strPathValue[1].equals(path) && strNameValue[0].equals(name))
						{
							callingTestWorker.httpResponse.put("Set-Cookie", mapValue);
						}
						else if(strNameValue[0].equals(name) && !strNameValue[1].equals(value))
						{
							StringBuilder sb  = new StringBuilder();
							sb.append(callingTestWorker.httpResponse.get("Set-Cookie"));
							sb.append("<>");
							sb.append(mapValue);
							callingTestWorker.httpResponse.put("Set-Cookie", sb.toString());
						}
					}
					else if(!s.contains("path"))
					{
						String[] str = s.split(";");
						String[] strNameValue = str[0].split("=");
						if(strNameValue[0].equals(name) && !strNameValue[1].equals(value))
						{
							StringBuilder sb = new StringBuilder();
							sb.append(callingTestWorker.httpResponse.get("Set-Cookie"));
							sb.append("<>");
							sb.append(mapValue);
							callingTestWorker.httpResponse.put("Set-Cookie", sb.toString());
						}
					}
				}
				else
				{
					StringBuilder sb = new StringBuilder();
					sb.append(callingTestWorker.httpResponse.get("Set-Cookie"));
					sb.append("<>");
					sb.append(mapValue);
					callingTestWorker.httpResponse.put("Set-Cookie", sb.toString());
				}
			}
		}
		else
		{
			System.out.println("C Name: " +mapValue);
			callingTestWorker.httpResponse.put("Set-Cookie", mapValue);
		}
			
	}			
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponse#containsHeader(java.lang.String)
	 */
	public boolean containsHeader(String header) {
		if(callingTestWorker.httpResponse.containsKey(header) && callingTestWorker.httpResponse.get(header) != null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponse#encodeURL(java.lang.String)
	 */
	public String encodeURL(String arg0) {
		return arg0;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponse#encodeRedirectURL(java.lang.String)
	 */
	public String encodeRedirectURL(String arg0) {
		return arg0;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponse#encodeUrl(java.lang.String)
	 */
	public String encodeUrl(String arg0) {
		return arg0;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponse#encodeRedirectUrl(java.lang.String)
	 */
	public String encodeRedirectUrl(String arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponse#sendError(int, java.lang.String)
	 */
	public void sendError(int errorCode, String message){
		try
		{
			if(isCommitedFlag == false)
			{
				String code = Integer.toString(errorCode);
				callingTestWorker.httpResponse.put("Status Code",code);
			    callingTestWorker.httpResponse.put("Content-Type", "text/html");
			    
				PrintWriter out = getWriter();
				out.println("<HTML><HEAD></HEAD><BODY>");
				out.println("<P>"+message+"</P>");
				out.println("</BODY></HTML>");
				isCommitedFlag =true;
			}
			else if(isCommitedFlag == true)
			{
				HttpServer.logger.error(new IllegalStateException().getCause());
				throw new IllegalStateException();
			}
		}
		catch(Exception ioe)
		{
			HttpServer.logger.error(ioe.getCause());
			sendError(500,"Internal Server Error");
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponse#sendError(int)
	 */
	public void sendError(int code) {
		try
		{
			if(isCommitedFlag == false)
			{
				callingTestWorker.httpRequest.put("Status Code", Integer.toString(code));
				PrintWriter out = getWriter();
				out.println("<HTML><HEAD></HEAD><BODY>");
				out.println("<P>ERROR</P>");
				out.println("</BODY></HTML>");
				isCommitedFlag =true;
			}
			else
			{
				HttpServer.logger.error(new IllegalStateException().getCause());
				sendError(500,"Internal Server Error");
				throw new IllegalStateException();
			}
		}
		catch(Exception ioe)
		{
			HttpServer.logger.error(ioe.getCause());
			sendError(500,"Internal Server Error");
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponse#sendRedirect(java.lang.String)
	 */
	public void sendRedirect(String arg0)  {
		try
		{
			System.out.println("HELLO");
			sendError(302, "Redirect");
			System.out.println("[DEBUG] redirect to " + arg0 + " requested");
			System.out.println("[DEBUG] stack trace: ");
			Exception e = new Exception();
			StackTraceElement[] frames = e.getStackTrace();
			for (int i = 0; i < frames.length; i++) {
				System.out.print("[DEBUG]   ");
				System.out.println(frames[i].toString());
			}
		}
		catch(Exception ioe)
		{
			HttpServer.logger.error(ioe.getCause());
			sendError(500,"Internal Server Error");
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponse#setDateHeader(java.lang.String, long)
	 */
	public void setDateHeader(String header, long date) {
	SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss");
	dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	Date d = new Date(date);
	String dateString = dateFormat.format(d.getTime());
	dateString +=" GMT";
	
	callingTestWorker.httpResponse.put(header, dateString);

	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponse#addDateHeader(java.lang.String, long)
	 */
	public void addDateHeader(String header, long date) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		Date d = new Date(date);
		String dateString = dateFormat.format(d.getTime());
		dateString +=" GMT";
		
		StringBuilder sb = new StringBuilder();  
		if(callingTestWorker.httpResponse.containsKey(header))
		{
			sb.append(callingTestWorker.httpResponse.get(header));
			sb.append(",");
			sb.append(dateString);
			callingTestWorker.httpResponse.put(header,sb.toString());
		}
		else
		{
			callingTestWorker.httpResponse.put(header, dateString);
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponse#setHeader(java.lang.String, java.lang.String)
	 */
	public void setHeader(String header, String value) {
		callingTestWorker.httpResponse.put(header, value);

	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponse#addHeader(java.lang.String, java.lang.String)
	 */
	public void addHeader(String header, String value) {
		StringBuilder sb = new StringBuilder();
		if(callingTestWorker.httpResponse.containsKey(header))
		{
			sb.append(callingTestWorker.httpResponse.get(header));
			sb.append(",");
			sb.append(value);
			callingTestWorker.httpResponse.put(header, sb.toString());
		}
		else
		{
			callingTestWorker.httpResponse.put(header, value);
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponse#setIntHeader(java.lang.String, int)
	 */
	public void setIntHeader(String header, int value) {
		callingTestWorker.httpResponse.put(header, Integer.toString(value));
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponse#addIntHeader(java.lang.String, int)
	 */
	public void addIntHeader(String header, int value) {
		StringBuilder sb =  new StringBuilder();
		if(callingTestWorker.httpResponse.containsKey(header))
		{
			sb.append(callingTestWorker.httpResponse.get(header));
			sb.append(",");
			sb.append(Integer.toString(value));
			callingTestWorker.httpResponse.put(header,sb.toString());
		}
		else
		{
			callingTestWorker.httpResponse.put(header, Integer.toString(value));
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponse#setStatus(int)
	 */
	public void setStatus(int status) {
		callingTestWorker.httpResponse.put("Status Code",Integer.toString(status));
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponse#setStatus(int, java.lang.String)
	 */
	public void setStatus(int arg0, String arg1) {
		
		//deprecated

	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#getCharacterEncoding()
	 */
	public String getCharacterEncoding() {
		return "ISO-8859-1";
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#getContentType()
	 */
	public String getContentType() {
		if(callingTestWorker.httpResponse.containsKey("Content-Type") && callingTestWorker.httpResponse.get("Content-Type") != null)
		{
			
			return callingTestWorker.httpResponse.get("Content-Type");
		}
		else
		{
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#getOutputStream()
	 */
	public ServletOutputStream getOutputStream() throws IOException {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#getWriter()
	 */
	public PrintWriter getWriter(){
		try
		{
			localeFlag = true;
			return new PrintWriter(callingTestWorker.byteArray, true);
		}
		catch(Exception e)
		{
			sendError(500,"Internal Server Error");
			HttpServer.logger.error(e.getCause());
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#setCharacterEncoding(java.lang.String)
	 */
	public void setCharacterEncoding(String characterEncoding) {
		localeFlag = true;
		callingTestWorker.httpResponse.put("Character Encoding", characterEncoding);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#setContentLength(int)
	 */
	public void setContentLength(int contentLength) {
		callingTestWorker.httpResponse.put("Content-Length",Integer.toString(contentLength));
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#setContentType(java.lang.String)
	 */
	public void setContentType(String contentType) {
		localeFlag =true;
		callingTestWorker.httpResponse.put("Content-Type", contentType);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#setBufferSize(int)
	 */
	public void setBufferSize(int arg0) {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#getBufferSize()
	 */
	public int getBufferSize() {
		return callingTestWorker.byteArray.size();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#flushBuffer()
	 */
	public void flushBuffer() {
		try
		{
			callingTestWorker.byteArray.flush();
			callingTestWorker.byteArray.reset();
			isCommitedFlag = true;
		}
		catch(Exception ioe)
		{
			HttpServer.logger.error(ioe.getCause());
			sendError(500,"Internal Server Error");
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#resetBuffer()
	 */
	public void resetBuffer() {
		
		if(isCommitedFlag == false)
		{
			callingTestWorker.byteArray.reset();
		}
		else
		{	
			HttpServer.logger.error(new IllegalStateException().getCause());
			sendError(500,"Internal Server Error");
			throw new IllegalStateException();
		}

	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#isCommitted()
	 */
	public boolean isCommitted() {
		
		return isCommitedFlag;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#reset()
	 */
	public void reset() {
		
		if(isCommitedFlag == false)
		{
			callingTestWorker.byteArray.reset();
			callingTestWorker.httpResponse.clear();
		}
		else
		{
			HttpServer.logger.error(new IllegalStateException().getCause());
			sendError(500,"Internal Server Error");
			throw new IllegalStateException();
		}

	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#setLocale(java.util.Locale)
	 */
	public void setLocale(Locale locale) {
		if(localeFlag == false && isCommitedFlag == false)
		{
			this.locale = locale; 
			callingTestWorker.httpResponse.put("Content-Language",this.locale.toString());
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#getLocale()
	 */
	public Locale getLocale() {
		return locale;
	}

}
