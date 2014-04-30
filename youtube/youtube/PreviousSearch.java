package edu.upenn.cis.cis555.youtube;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PreviousSearch extends HttpServlet{

	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		int page  = Integer.parseInt(request.getParameter("PageNumber"));
		String keyWord = request.getParameter("Keyword");
	//	out.println(page);
		int PageNumber = page - 1;
		out.println(PageNumber);
	
		
		String[] keyExtractor = keyWord.split(" ");
		StringBuilder keyBuilder = new StringBuilder();
		if(keyExtractor.length > 1)
		{
			int i = 0;
			for(; i < keyExtractor.length-1 ; i++)
			{
				keyBuilder.append(keyExtractor[i]);
				keyBuilder.append("&~&");
				System.out.println("keyExtractor ["+i+"]: " +keyExtractor[i]);
			}
			keyBuilder.append(keyExtractor[i]);
		}
		else
		{
			keyBuilder.append(keyExtractor[0]);
			keyBuilder.append("&~&");
		}
		
		System.out.println("keyBuilder: "+keyBuilder.toString());
		response.setContentType("text/xml");
		System.out.println("KEYWORD::::::::"+keyWord);
	
		String host =request.getParameter("CacheServer");// "cis555-vm";
		System.out.println("CacheServer: "+host);
		String port =request.getParameter("CacheServerPort");
		System.out.println("CacheServerPort: "+port);
		int daemonPort = Integer.parseInt(port);//5000;
		BufferedReader reader;
		PrintWriter writer;
		/**
		 * Search for the earlier Page
		 */
		//int pageNumber = page - 1;
		Socket s = new Socket(host,daemonPort);
		writer = new PrintWriter(s.getOutputStream(), true);
//	    reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
	    String requestString  = "GET /videos?key="+keyBuilder.toString()+";"+PageNumber+" HTTP/1.0\r\n\r\n";
	    writer.print(requestString);
	    writer.flush();
	  //  writer.close();
	    
	    reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
	    String str = reader.readLine();
	    System.out.println("STR: "+str);
	    int contentLength = 0;
	    String firstLine = str;
	    while(!str.equals(""))
	    {
	    	str = reader.readLine();
	    	System.out.println("str: "+str);
	    	if(str.equalsIgnoreCase("content-length"))
	    	{
	    		String[] strArray = str.split(":");
				contentLength = Integer.parseInt(strArray[1].trim());
	    	}
	    }
	    
	    StringBuilder respBuilder  = new StringBuilder();
	    str = reader.readLine();
	    System.out.println("STR 3: "+str);
	    respBuilder.append(str);   
	    System.out.println("Response Final: "+respBuilder.toString());
	    writer.close();
	    reader.close();

	    
	    
	  //  int PageNumber = page - 1;
    	response.setContentType("text/html");
    	 /**
	     * New CSS
	     */
	    
	    response.setContentType("text/html");
	    out.println("<html><head>");
    	out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\" />");
    	out.println("</head><body>");
    	out.println("<div id =\"page\">");
    	out.println("<table align =\"left\"><tr></tr><tr></tr><tr><tr></tr><tr></tr><tr></tr><td align = \"left\">");
    	out.println("<form method =\"POST\" action=\"SearchResponse\">");
        out.println("<label for=\"keyWord\">Search for:</label>");
        out.println("<input type=\"text\" id=\"keyWord\" name=\"keyWord\"/>");
    	out.println("<input type=\"Hidden\" id=\"CacheServer\" name=\"CacheServer\" value=\""+host+"\"/>");
    	out.println("<input type=\"Hidden\" id=\"CacheServerPort\" name=\"CacheServerPort\" value = \""+port+"\"/>");          
    	//out.println("<br/>");
		out.println("<input type = \"submit\" value = \"Search\"/>");
		//out.println("<input type = \"reset\" value = \"Reset\"/>");
		out.println("</form>");
		out.println("</td></tr></table>");
		out.println("<div id = \"header\">");
    	//out.println("<img src=\"Search.jpg\"/>");
    	
    	out.println("</div>");
    	out.println("</div>");
    	
    	out.println("<div id = \"content\">");
    	out.println("<div id = \"container\"");
    	
    	out.println("<div id =\"main\">");
    	out.println("<div id = \"text\"");	
    	
    	
    	
    	
    	
    	
    	
    /*	out.println("<html><body>");
    	out.println("<h2> BINGLE SEARCH</h2>");
    	out.println("<form name=\"next\" method =\"GET\" action=\"NextSearch\">");
		out.println("<br/>");
		//out.println("<input type = \"submit\" value = \"Next>>\"/>");
		out.println("<a href=\"javascript:document.next.submit()\">Next</a>");
		out.println("<input type=\"Hidden\" id=\"PageNumber\" name=\"PageNumber\" value=\""+PageNumber+"\"/>");
		out.println("<input type=\"Hidden\" id=\"Keyword\" name=\"Keyword\" value=\""+keyWord+"\"/>");
		out.println("<input type=\"Hidden\" id=\"CacheServer\" name=\"CacheServer\" value=\""+host+"\"/>");
		out.println("<input type=\"Hidden\" id=\"CacheServerPort\" name=\"CacheServerPort\" value=\""+port+"\"/>");
		out.println("</form>");
		
		if(PageNumber > 0)
		{
			out.println("<form name=\"previous\" method =\"GET\" action=\"PreviousSearch\">");
			out.println("<br/>");
			//out.println("<input type = \"submit\" value = \"<< Previous\"/>");
			out.println("<a href=\"javascript:document.previous.submit()\">Previous</a>");
			out.println("<input type=\"Hidden\" id=\"PageNumber\" name=\"PageNumber\" value=\""+PageNumber+"\"/>");
			out.println("<input type=\"Hidden\" id=\"Keyword\" name=\"Keyword\" value=\""+keyWord+"\"/>");
			out.println("<input type=\"Hidden\" id=\"CacheServer\" name=\"CacheServer\" value=\""+host+"\"/>");
			out.println("<input type=\"Hidden\" id=\"CacheServerPort\" name=\"CacheServerPort\" value=\""+port+"\"/>");
			out.println("</form>");
		}*/
    	
    	DocumentBuilder builder;
    	byte[] stringAsByteArray;
    	Document resultPage;
    	try
    	{
	        builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			stringAsByteArray = respBuilder.toString().getBytes();//"UTF-8");
		    resultPage = builder.parse(new ByteArrayInputStream(stringAsByteArray));
		    NodeList documentNodes = resultPage.getElementsByTagName("document");
		    
		    for(int i = 0; i < documentNodes.getLength(); i++)
		    {
		    	Node docNode = documentNodes.item(i);
		    	
		    	String title = null;
		    	String tfIdf = null;
		    	String link = null;
		    	
		    	NodeList childNodeList = docNode.getChildNodes();
		    	for(int j = 0; j< childNodeList.getLength(); j++)
		    	{
		    		Node childNode = childNodeList.item(j);
		    		
		    		Node textNode = childNode.getFirstChild();
					String textValue = null;
					if(textNode != null && textNode.getNodeType() == Node.TEXT_NODE){
						textValue = textNode.getNodeValue();
					}
					
					String nodeName = childNode.getNodeName();	
					
					if(nodeName.equals("title"))
					{
						title = textValue;
					}
					else if(nodeName.equals("link"))
					{
						link = textValue;
					}
					else if(nodeName.equals("tfIdf"))
					{
						tfIdf = textValue;
					}
		    	}
		    	
		    	if(title != null)
					out.println("<b><a href =\"" + link + "\">" + title + "</a></b><br>");
		    	if(link != null)
					out.println("<b><a href =\"" + link + "\">" + link + "</a></b><br>");
		    	if(tfIdf!= null)
					out.println("<b>tfIdf</b>: " + tfIdf + "&nbsp;&nbsp;");
		    	
		    	out.println("<br/><br/>");
		    	
		    }
		    out.println("</div>");
		    out.println("</div></div>");
		    out.println("<div class=\"clear\"></div>");
		    out.println("<div id=\"footer\"> <p> BingleSearch Inc.</p>");
		    out.println("</div>");
		    out.println("<table align = \"center\"><tr><td align = \"right\">");
		    
		    if(PageNumber > 0)
			{
		    	out.print("<form name=\"previous\" method =\"GET\" action=\"PreviousSearch\">");
			    out.print("<a href=\"javascript:document.previous.submit()\">Previous</a>");
			    out.print("<input type=\"Hidden\" id=\"PageNumber\" name=\"PageNumber\" value=\""+PageNumber+"\"/>");
			    out.print("<input type=\"Hidden\" id=\"Keyword\" name=\"Keyword\" value=\""+keyWord+"\"/>");
			    out.print("<input type=\"Hidden\" id=\"CacheServer\" name=\"CacheServer\" value=\""+host+"\"/>");
			    out.print("<input type=\"Hidden\" id=\"CacheServerPort\" name=\"CacheServerPort\" value=\""+port+"\"/>");
			    out.print("</form>");
				out.print("</td>");
				out.print("<td>");
				
			}
		    out.print("<form name=\"next\" method =\"GET\" action=\"NextSearch\">");
	    	out.print("<a href=\"javascript:document.next.submit()\">Next</a>");out.print("<input type=\"Hidden\" id=\"PageNumber\" name=\"PageNumber\" value=\""+PageNumber+"\"/>");
		    out.print("<input type=\"Hidden\" id=\"Keyword\" name=\"Keyword\" value=\""+keyWord+"\"/>");
		    out.print("<input type=\"Hidden\" id=\"CacheServer\" name=\"CacheServer\" value=\""+host+"\"/>");
			out.print("<input type=\"Hidden\" id=\"CacheServerPort\" name=\"CacheServerPort\" value=\""+port+"\"/>");
			out.print("</form>");
			out.print("</td></tr></table>");
			out.print("</div>");	
		    
		    
		    
		    out.println("</body></html>");
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
	}

}
