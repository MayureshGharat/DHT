package edu.upenn.cis.cis555.youtube;

import java.util.Enumeration;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

/**
 * @author Todd J. Green
 */
public class FakeSession implements HttpSession {

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getCreationTime()
	 */
	public TestWorker callingTestWorker;
	public HttpServer callingServer;
	public String sessionid;
	long creationTime;
	long lastAccessedTime;
	int maxInactiveInterval;
	FakeContext fc;
	
	
	public FakeSession() {
	}
	
	FakeSession(TestWorker objTestWorker, HttpServer server,FakeContext fc)
	{
		callingTestWorker = objTestWorker;
		callingServer = server;
		this.creationTime = System.currentTimeMillis();
		this.lastAccessedTime = System.currentTimeMillis(); 
		this.fc =fc;
		putSession();
	}
	
	public void putSession()
	{
		sessionid = UUID.randomUUID().toString();
		callingServer.putSessionID(sessionid, this);
		callingTestWorker.httpResponse.put("Set-Cookie-sessionId","SessionID="+sessionid);
	}
	
	
	
	public long getCreationTime() {
		if(this.m_valid == true)
		{
			return this.creationTime;
		}
		else
		{
			HttpServer.logger.error(new java.lang.IllegalStateException().getCause());
			throw new java.lang.IllegalStateException();
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getId()
	 */
	public String getId() {
		if(this.m_valid == true)
		{
			return this.sessionid;
		}
		else
		{
			HttpServer.logger.error(new java.lang.IllegalStateException().getCause());
			throw new java.lang.IllegalStateException();
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getLastAccessedTime()
	 */
	public long getLastAccessedTime() {
		if(this.m_valid == true)
		{
			return lastAccessedTime;
		}
		else
		{
			HttpServer.logger.error(new java.lang.IllegalStateException().getCause());
			throw new java.lang.IllegalStateException();
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getServletContext()
	 */
	public ServletContext getServletContext() {
		return fc;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#setMaxInactiveInterval(int)
	 */
	public void setMaxInactiveInterval(int inactiveInterval) {
		this.maxInactiveInterval = inactiveInterval;
		if(inactiveInterval > 0)
		{
			Timer t = new Timer();
            t.schedule(new TimerTask() {
                      public void run() {
                              invalidate();
                      }
                    }, inactiveInterval*1000);
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getMaxInactiveInterval()
	 */
	public int getMaxInactiveInterval() {
		return this.maxInactiveInterval;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getSessionContext()
	 */
	public HttpSessionContext getSessionContext() {
		// TODO Auto-generated method stub
		//deprecated
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String arg0) {
		if(this.m_valid == true)
		{
			return m_props.get(arg0);
		}
		else
		{
			HttpServer.logger.error(new java.lang.IllegalStateException().getCause());
			throw new java.lang.IllegalStateException();
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getValue(java.lang.String)
	 */
	public Object getValue(String arg0) {
		// TODO Auto-generated method stub
		if(this.m_valid == true)
		{
			return m_props.get(arg0);
		}
		else
		{
			HttpServer.logger.error(new java.lang.IllegalStateException().getCause());
			throw new java.lang.IllegalStateException();
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getAttributeNames()
	 */
	public Enumeration getAttributeNames() {
		// TODO Auto-generated method stub
		if(this.m_valid == true)
		{
			return m_props.keys();
		}
		else
		{
			HttpServer.logger.error(new java.lang.IllegalStateException().getCause());
			throw new java.lang.IllegalStateException();
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getValueNames()
	 */
	public String[] getValueNames() {
		// TODO Auto-generated method stub
		//deprecated
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#setAttribute(java.lang.String, java.lang.Object)
	 */
	public void setAttribute(String arg0, Object arg1) {
		if(this.m_valid == true)
		{
			m_props.put(arg0, arg1);
		}
		else
		{
			HttpServer.logger.error(new java.lang.IllegalStateException().getCause());
			throw new java.lang.IllegalStateException();
		}
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#putValue(java.lang.String, java.lang.Object)
	 */
	public void putValue(String arg0, Object arg1) {
		if(this.m_valid == true)
		{
			m_props.put(arg0, arg1);
		}
		else
		{
			HttpServer.logger.error(new java.lang.IllegalStateException().getCause());
			throw new java.lang.IllegalStateException();
		}
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#removeAttribute(java.lang.String)
	 */
	public void removeAttribute(String arg0) {
		if(this.m_valid == true)
		{
			m_props.remove(arg0);
		}
		else
		{
			HttpServer.logger.error(new java.lang.IllegalStateException().getCause());
			throw new java.lang.IllegalStateException();
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#removeValue(java.lang.String)
	 */
	public void removeValue(String arg0) {
		if(this.m_valid == true)
		{
			m_props.remove(arg0);
		}
		else
		{
			throw new java.lang.IllegalStateException();
		}
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#invalidate()
	 */
	public void invalidate() {
		try
		{
			callingServer.removeSession(this.sessionid);
			m_valid = false;
		}
		catch(Exception e)
		{
			HttpServer.logger.error(new java.lang.IllegalStateException().getCause());
			throw new java.lang.IllegalStateException();
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#isNew()
	 */
	public boolean isNew() {
		// TODO Auto-generated method stub
		if(this.m_valid == true)
		{
			if(callingServer.getSessionID(sessionid) != null )
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			HttpServer.logger.error(new java.lang.IllegalStateException().getCause());
			throw new java.lang.IllegalStateException();
		}
	}

	boolean isValid() {
		return m_valid;
	}
	
	private Properties m_props = new Properties();
	private boolean m_valid = true;
}

