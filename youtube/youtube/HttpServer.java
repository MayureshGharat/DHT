package edu.upenn.cis.cis555.youtube;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import javax.servlet.http.Cookie;

public class HttpServer 
{
	
	public final int NO_OF_THREADS = 5;
	public final int MAXQUEUE = 50000;
	
	public TestWorker[] worker = new TestWorker[NO_OF_THREADS];
	public Vector<SocketInfo> enqueue = new Vector<SocketInfo>();
	public Vector<Thread> Threads = new Vector<Thread>();
	public boolean threadFlag;
	//Vector<String> threadUrl = new Vector<String>();
	HashMap <Thread,String> threadToUrl = new HashMap<Thread,String>();
	HashMap <String, FakeSession> sessionMap = new HashMap<String,FakeSession>();
	public HashMap <String, String> urlPatternMap = new HashMap<String, String>();
	HashMap<String, String> statusMap = new HashMap<String,String>();
	
	static Logger logger = Logger.getLogger(HttpServer.class);
	
	public int portNo;
	public String hostName;
	String rootDir;
	ListeningThread listenThread;
	String xmlPath;
	public int connectionFlag;
	
	LoadServlet initializeServlet;
	
	public HttpServer() {}

	
	HttpServer(int portNo, String rootDir, boolean flag, String xmlPath,int connectionFlag)
	{
		
		this.portNo = portNo;
		this.rootDir = rootDir;
		this.threadFlag = flag;
		this.xmlPath = xmlPath;
		this.initializeServlet = new LoadServlet(xmlPath);
		this.urlPatternMap = this.initializeServlet.getPatternMap();
		this.connectionFlag = connectionFlag;
		this.statusMap = this.initializeServlet.statusMap;
//		InetAddress addr;
//		try
//		{
//			addr = InetAddress.getLocalHost();
//			this.hostName = addr.getHostName().toString();
//			System.out.println("HostName: "+hostName);
//			System.out.println("IP: "+addr.getHostAddress());
//			
//		}
//		catch(UnknownHostException exp)
//		{
//			System.err.println("InetAddress exception in HttpServer(): "+exp.getMessage());
//			exp.printStackTrace();
//		}
		
		for(int i = 0; i < NO_OF_THREADS; i++)
		{
			worker[i] = new TestWorker(this,initializeServlet); 
			worker[i].t = new Thread(worker[i]);
			Threads.add(worker[i].t);
			worker[i].t.start();
		}
		run();
	}
	
	
	public void run()
	{
		
		listenThread = new ListeningThread(this);
		listenThread.t = new Thread(listenThread);
		listenThread.t.setName("Listening Thread");
		listenThread.t.start();
		
		
		try
		{
			listenThread.t.join();
		}
		catch(InterruptedException ie)
		{
			ie.printStackTrace();
		}
	}
	
	
	
	public synchronized void putSocket(Socket s, int portNo) throws InterruptedException 
	{
		SocketInfo si= new SocketInfo(s,portNo);
		enqueue.add(si);
		notifyAll();
	}
	
	public synchronized SocketInfo getSocket() //throws InterruptedException 
	{
		notifyAll();
		SocketInfo si= new SocketInfo();
		try
		{
			while (enqueue.size() == 0)
			{
				wait();
			}
			si =  enqueue.firstElement();
			enqueue.removeElement(si);
			return si;
		}
		catch(InterruptedException excp)
		{
			return null;	
		}
		
	}
	
	public synchronized void reinsertSocket(SocketInfo si){
		enqueue.add(si);
		notifyAll();
	}
	
	public synchronized void emptyEnqueue()
	{
		//System.out.println("In emptyEnqueue:");
		enqueue.removeAllElements();
	}
	
	
	public synchronized void putThreadUrl(Thread t,String value)
	{
		threadToUrl.put(t, value);
	}
	
	public synchronized HashMap<Thread, String> getThreadUrl()
	{
		for (Thread t : this.getThreadList())
		{
			String s = t.getState().toString();
		  
			if(s.equals("BLOCKED"))
			{
				this.threadToUrl.put(t, "BLOCKED");
			}
			else if(s.equals("WAITING"))
			{
				this.threadToUrl.put(t, "WAITING");
			}
		}
		return threadToUrl;
	}
	
	public synchronized Vector<Thread> getThreadList()
	{
		return Threads;
	}
	public boolean getThreadFlag()
	{
		return threadFlag;
	}
	public synchronized void stopServer()
	{
		this.threadFlag = false;
	}
	

	public synchronized FakeSession getSessionID(String sessionId)
	{
		if(sessionMap.containsKey(sessionId))
		{
			return sessionMap.get(sessionId);
		}
		else
		{
			return null;
		}
	}
	
	public synchronized void putSessionID(String sessionId, FakeSession fs)
	{
		sessionMap.put(sessionId, fs);
	}
	
	public synchronized void removeSession(String sessionId)
	{
		sessionMap.remove(sessionId);
	}
	
	public static void main(String[] args)
	{
		PropertyConfigurator.configure("log4j.properties");
		Vector<String> cmdLineArgs = new Vector<String> ();
		int portNo = 0;
		String rootDir = "";
		String xmlPath ="";
		int connectionFlag;
		
		if(args.length == 0)
		{
			System.out.println("Mayuresh R. Gharat");
			System.out.println("SEAS LOGIN: mayuresh");
		}
		else
		{
			for(String temp:args)
			{
				cmdLineArgs.add(temp);
			}
		
			portNo = Integer.parseInt(cmdLineArgs.remove(0));
			rootDir = cmdLineArgs.remove(0);
			xmlPath = cmdLineArgs.remove(0);
			connectionFlag = Integer.parseInt(cmdLineArgs.remove(0));
			new HttpServer(portNo, rootDir, true, xmlPath, connectionFlag);
		}
	}
}