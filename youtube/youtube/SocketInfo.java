package edu.upenn.cis.cis555.youtube;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketInfo {

	private Socket socket;
	private int portNo;
	private BufferedReader in;
	private PrintWriter writer;
	//private BufferedOutputStream bos;
	private PrintStream ps;
	private InputStreamReader isr;
	
	
	public SocketInfo() {
		// TODO Auto-generated constructor stub
		socket = null;
		portNo = 0;
		in = null;
		writer = null;
		ps =null;
		isr =null;
	}
	
	public SocketInfo(Socket s, int portNo) {
		// TODO Auto-generated constructor stub
		this.socket = s;
		this.portNo = portNo;
		try
		{
			this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			this.writer = new PrintWriter(this.socket.getOutputStream(),true);	
			
			//this.bos = new BufferedOutputStream(this.socket.getOutputStream());
			this.ps = new PrintStream(this.socket.getOutputStream(), true);
			this.isr = new InputStreamReader(this.socket.getInputStream());
		}
		catch(IOException ioe)
		{
			System.err.println("IOException in SocketInfo(socket, portNo): "+ioe.getMessage());
			System.out.println("Could not open Buffers for socket "+s);
			ioe.printStackTrace();
		}
		
	}
	
	
	public Socket getSocket(){
		return socket;
	}
	
	public int getPortNum(){
		return portNo;
	}
	
	public BufferedReader getInputReader(){
		return in;
	}
	
	public PrintWriter getOutputWriter(){
		return writer;
	}
	
	public InputStreamReader getInputStreamReader()
	{
		return isr;
	}
	
//	public BufferedOutputStream getBufferedOutputStream()
//	{
//		return bos;
//	}
	
	public PrintStream getPrintStream()
	{
		return ps;
	}

}
