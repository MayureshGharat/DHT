package edu.upenn.cis.cis555.youtube;

import javax.servlet.*;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.util.*;

/**
 * @author Nick Taylor
 */
public class FakeContext implements ServletContext {
	private HashMap<String,Object> attributes;
	private HashMap<String,String> initParams;
	
	public FakeContext() {
		attributes = new HashMap<String,Object>();
		initParams = new HashMap<String,String>();
	}
	
	public Object getAttribute(String name) {
		return attributes.get(name);
	}
	
	public Enumeration getAttributeNames() {
		Set<String> keys = attributes.keySet();
		Vector<String> atts = new Vector<String>(keys);
		return atts.elements();
	}
	
	public ServletContext getContext(String name) {
		return null;
	}
	
	public String getInitParameter(String name) {
		return initParams.get(name);
	}
	
	public Enumeration getInitParameterNames() {
		Set<String> keys = initParams.keySet();
		Vector<String> atts = new Vector<String>(keys);
		return atts.elements();
	}
	
	public int getMajorVersion() {
		return 2;
	}
	
	public String getMimeType(String file) {
		return null;
	}
	
	public int getMinorVersion() {
		return 4;
	}
	
	public RequestDispatcher getNamedDispatcher(String name) {
		return null;
	}
	
	public String getRealPath(String path) {
		try 
		{
			String[] fileName = path.split("/");
			int i = fileName.length -1;
			File f = new File(fileName[i]);
			String realPath;
			try {
				realPath = f.getCanonicalPath();
			} catch (IOException e) {
				return null;
			}
			return realPath;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public RequestDispatcher getRequestDispatcher(String name) {
		return null;
	}
	
	public java.net.URL getResource(String path) {
		return null;
	}
	
	public java.io.InputStream getResourceAsStream(String path) {
		return null;
	}
	
	public java.util.Set getResourcePaths(String path) {
		return null;
	}
	
	public String getServerInfo() {
		return "Test Harness";
	}
	
	public Servlet getServlet(String name) {
		//deprecated
		return null;
	}
	
	public String getServletContextName() {
		return "Test Harness";
	}
	
	public Enumeration getServletNames() {
	//deprecated
		return null;
	}
	
	public Enumeration getServlets() {
		//deprecated
		return null;
	}
	
	public void log(Exception exception, String msg) {
		log(msg, (Throwable) exception);
	}
	
	public void log(String msg) {
		System.err.println(msg);
	}
	
	public void log(String message, Throwable throwable) {
		System.err.println(message);
		throwable.printStackTrace(System.err);
	}
	
	public void removeAttribute(String name) {
		attributes.remove(name);
	}
	
	public void setAttribute(String name, Object object) {
		attributes.put(name, object);
	}
	
	void setInitParam(String name, String value) {
		initParams.put(name, value);
	}
}

