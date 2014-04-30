package edu.upenn.cis.cis555.youtube;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//import edu.upenn.cis.cis555.Indexer.Stemmer;



public class SearchResponse extends HttpServlet {

	 
	@Override
	public void init(ServletConfig config) throws ServletException {
	super.init(config);
	}
	 
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	//protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {	
		String keyWord = request.getParameter("keyWord");
		String host =request.getParameter("CacheServer");
		String port =request.getParameter("CacheServerPort");
		
		
		
		
		System.out.println("Keyword in Search Response: "+keyWord);
	
		PrintWriter out = response.getWriter();
		if(keyWord.length() == 0)
		{
			
			out.println("<HTML><HEAD><TITLE>BingleSearch</TITLE>");
			out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"bluebliss.css\" /></HEAD><BODY>");
			out.println("<div id=\"mainContentArea\">");
			out.println("<div id=\"contentBox\">");
			out.println("<div id=\"title\">BingleSearch Inc.</div>");
			out.println("<div id=\"blueBox\">"); 
	        out.println("<div id=\"header\"></div>");
	        out.println("<div class=\"contentTitle\">Welcome to BingleSearch Inc.</div>");
	        out.println("<div class=\"pageContent\">");
	        /*    <p>This template was created for those needing a really simple site, without much content.</p>
	            <p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Integer mi. Vivamus sit amet neque vitae sapien bibendum sodales. Curabitur elementum. Duis imperdiet. Donec eleifend porttitor sapien. Praesent leo. Quisque auctor velit sed tellus. Suspendisse potenti. Aenean laoreet imperdiet nunc. Donec commodo suscipit dolor. Aenean nibh. Sed id odio. Aliquam lobortis risus ut felis. Sed vehicula pellentesque quam.</p>
	            <p>Vestibulum augue quam, interdum id, congue semper, convallis non, velit. Quisque augue tortor, tristique ac, scelerisque eget, aliquam id, sem. Aenean lorem. Fusce velit nibh, dapibus quis, laoreet nec, porta a, dui. Nullam ac urna. Proin eget elit. Nunc scelerisque venenatis urna. Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Fusce congue, turpis ut commodo mattis, pede erat fringilla tellus, pulvinar suscipit odio lorem sed pede.</p>
	      */
	        out.println("<form method =\"POST\" action=\"SearchResponse\">");
	        out.println("<label for=\"keyWord\">KEYWORD:</label>");
	        out.println("<input type=\"text\" id=\"keyWord\" name=\"keyWord\"/>");
	    	out.println("<input type=\"Hidden\" id=\"CacheServer\" name=\"CacheServer\" value=\""+host+"\"/>");
	    	out.println("<input type=\"Hidden\" id=\"CacheServerPort\" name=\"CacheServerPort\" value = \""+port+"\"/>");          
	    	out.println("<br><br>");
			out.println("<input type = \"submit\" value = \"Submit Form\"/>");
			out.println("<input type = \"reset\" value = \"Reset Form\"/>");
			out.println("</form>");
	    	out.println("</div>");
	        out.println("<div id=\"footer\">web development by BingleSearch</div>");
	        out.println("</div></div></div></BODY></HTML>");
			
			
			
			//YouTubeSearch ytSearch = new YouTubeSearch();
			//ytSearch.doGet(request, response);
			/*out.println("<html><body>");
			out.println("<form method =\"POST\" action=\"SearchResponse\">");
	        out.println("<label for=\"keyWord\">Search for:</label>");
	        out.println("<input type=\"text\" id=\"keyWord\" name=\"keyWord\"/>");
	    	out.println("<input type=\"Hidden\" id=\"CacheServer\" name=\"CacheServer\" value=\""+host+"\"/>");
	    	out.println("<input type=\"Hidden\" id=\"CacheServerPort\" name=\"CacheServerPort\" value = \""+port+"\"/>");          
			out.println("<input type = \"submit\" value = \"Search\"/>");
			//out.println("<input type = \"reset\" value = \"Reset\"/>");
			out.println("</form>");
			*/
			
			
			
			/*out.println("The query youinserted was blank...Please click here to go back");
			out.println("<form method =\"GET\" action=\"YouTubeSearch\">");*/
			//out.println("<label for=\"keyWord\">KEYWORD:</label>");
			//out.println("<input type=\"text\" id=\"keyWord\" name=\"keyWord\"/>");
		//	out.println("<input type=\"Hidden\" id=\"CacheServer\" name=\"CacheServer\" value=\""+CacheServer+"\"/>");
			//out.println("<input type=\"Hidden\" id=\"CacheServerPort\" name=\"CacheServerPort\" value = \""+CacheServerPort+"\"/>");
			/*out.println("<br><br>");
			out.println("<input type = \"submit\" value = \"go Back!!!\"/>");
			//out.println("<input type = \"reset\" value = \"Reset Form\"/>");
			out.println("</body></html>");
*/			
		}
		else
		{
			SpellChecker spChecker = new SpellChecker("big.txt");
			String correctWord = spChecker.correct(keyWord);
			System.out.println("CORRECT WORD: "+correctWord);
			
			/**
			 * Stemming process
			 */
			ArrayList<String> keywordList = new ArrayList<String>();
			keywordList.add(keyWord);
			keywordList = normalizeString(keywordList);
			keywordList = applyPorterStemmer(keywordList);
			
			String[] keyExtractor = new String[keywordList.size()];
			for(int i=0; i<keyExtractor.length; i++)
				keyExtractor[i] = keywordList.get(i);
			
			
		//	String[] keyExtractor = keyWord.split(" ");
			StringBuilder keyBuilder = new StringBuilder();
			
			/**
			 * Do Spell Check here
			 */
			String[] spellCorrect = new String[keyExtractor.length];
			
			if(keyExtractor.length > 1)
			{
				int i = 0;
				for(; i < keyExtractor.length-1 ; i++)
				{
					spellCorrect[i] = spChecker.correct(keyExtractor[i]);
					keyBuilder.append(keyExtractor[i]);
					keyBuilder.append("&~&");
					System.out.println("keyExtractor ["+i+"]: " +keyExtractor[i]);
				}
				spellCorrect[i] = spChecker.correct(keyExtractor[i]);
				keyBuilder.append(keyExtractor[i]);
			}
			else
			{
				spellCorrect[0] = spChecker.correct(keyExtractor[0]);
				keyBuilder.append(keyExtractor[0]);
				keyBuilder.append("&~&");
			}
			
			System.out.println("keyBuilder: "+keyBuilder.toString());
			response.setContentType("text/xml");
			System.out.println("KEYWORD::::::::"+keyWord);
			
			/**
			 * Forming the correct word
			 */
			StringBuilder spellBuilder = new StringBuilder();
			if(spellCorrect.length > 1)
			{
				int i = 0;
				for(; i < spellCorrect.length-1; i++)
				{
					spellBuilder.append(spellCorrect[i]);
					spellBuilder.append(" ");
				}
				spellBuilder.append(spellCorrect[i]);
			}
			else
			{
				spellBuilder.append(spellCorrect[0]);
			}
//			String host =request.getParameter("CacheServer");// "cis555-vm";
			System.out.println("CacheServer: "+host);
//			String port =request.getParameter("CacheServerPort");
			System.out.println("CacheServerPort: "+port);
			int daemonPort = Integer.parseInt(port);//5000;
			BufferedReader reader;
			PrintWriter writer;
			int pageNumber = 0;
			Socket s = new Socket(host,daemonPort);
			writer = new PrintWriter(s.getOutputStream(), true);
	//	    reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
		    String requestString  = "GET /videos?key="+keyBuilder.toString()+";"+pageNumber+" HTTP/1.0\r\n\r\n";
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

		    if(firstLine.contains("400"))
		    {
		    	response.setContentType("text/html");
		    	out.println("<html><body> 400 Bad Request </body></html>");
		    }
		    else if(firstLine.contains("404"))
		    {
		    	response.setContentType("text/html");
		    	//out.println("<html><body>");
		    	out.println("<HTML><HEAD><TITLE>BingleSearch</TITLE>");
		    	
				out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"bluebliss.css\" /></HEAD><BODY>");
				out.println("<div id=\"mainContentArea\">");
				out.println("<h1>"+str+"</h1>");
				
				
				out.print("<form name=\"search\" method =\"POST\" action=\"SearchResponse\">");
		    	
			    //	out.println("<br/>");
					//out.println("<input type = \"submit\" value = \"Next>>\"/>");
				out.print("<h1> Did you mean: </h1>");
				out.print("<a href=\"javascript:document.search.submit()\">"+spellBuilder.toString()+"</a>");
				//out.println("<input type=\"Hidden\" id=\"PageNumber\" name=\"PageNumber\" value=\""+PageNumber+"\"/>");
			//	keyWord = spellBuilder.toString();
				out.print("<input type=\"Hidden\" id=\"keyWord\" name=\"keyWord\" value=\""+spellBuilder.toString()+"\"/>");
				out.print("<input type=\"Hidden\" id=\"CacheServer\" name=\"CacheServer\" value=\""+host+"\"/>");
				out.print("<input type=\"Hidden\" id=\"CacheServerPort\" name=\"CacheServerPort\" value=\""+port+"\"/>");
				out.print("</form>");
				
				
				
				
				out.println("<div id=\"contentBox\">");
				out.println("<div id=\"title\">BingleSearch Inc.</div>");
				out.println("<div id=\"blueBox\">"); 
		        out.println("<div id=\"header\"></div>");
		        out.println("<div class=\"contentTitle\">Welcome to BingleSearch Inc.</div>");
		        out.println("<div class=\"pageContent\">");
		        /*    <p>This template was created for those needing a really simple site, without much content.</p>
		            <p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Integer mi. Vivamus sit amet neque vitae sapien bibendum sodales. Curabitur elementum. Duis imperdiet. Donec eleifend porttitor sapien. Praesent leo. Quisque auctor velit sed tellus. Suspendisse potenti. Aenean laoreet imperdiet nunc. Donec commodo suscipit dolor. Aenean nibh. Sed id odio. Aliquam lobortis risus ut felis. Sed vehicula pellentesque quam.</p>
		            <p>Vestibulum augue quam, interdum id, congue semper, convallis non, velit. Quisque augue tortor, tristique ac, scelerisque eget, aliquam id, sem. Aenean lorem. Fusce velit nibh, dapibus quis, laoreet nec, porta a, dui. Nullam ac urna. Proin eget elit. Nunc scelerisque venenatis urna. Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Fusce congue, turpis ut commodo mattis, pede erat fringilla tellus, pulvinar suscipit odio lorem sed pede.</p>
		      */
		        out.println("<form method =\"POST\" action=\"SearchResponse\">");
		        out.println("<label for=\"keyWord\">KEYWORD:</label>");
		        out.println("<input type=\"text\" id=\"keyWord\" name=\"keyWord\"/>");
		    	out.println("<input type=\"Hidden\" id=\"CacheServer\" name=\"CacheServer\" value=\""+host+"\"/>");
		    	out.println("<input type=\"Hidden\" id=\"CacheServerPort\" name=\"CacheServerPort\" value = \""+port+"\"/>");          
		    	out.println("<br><br>");
				out.println("<input type = \"submit\" value = \"Submit Form\"/>");
				out.println("<input type = \"reset\" value = \"Reset Form\"/>");
				out.println("</form>");
		    	out.println("</div>");
		        out.println("<div id=\"footer\">web development by BingleSearch</div>");
		        out.println("</div></div></div></BODY></HTML>");
		    	
		    	
		   /* 	
		    	"</body></html>");*/
		    }
		    else
		    {
		    	int PageNumber = 0;
		    	response.setContentType("text/html");
		    	/*
		    	 * NEW CSS
		    	 * 
		    	 */
		    	
		    
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
				out.println("<input type = \"submit\" value = \"Search\"/>");
				//out.println("<input type = \"reset\" value = \"Reset\"/>");
				out.println("</form>");
				out.println("</td></tr>"/*</table>"*/);
 
				if(!keyWord.equalsIgnoreCase(spellBuilder.toString()))
				{
					out.print("<tr><td><form name=\"search\" method =\"POST\" action=\"SearchResponse\">");
			    	
				    //	out.println("<br/>");
						//out.println("<input type = \"submit\" value = \"Next>>\"/>");
					out.print("<h5> Did you mean: </h5>");
					out.print("<a href=\"javascript:document.search.submit()\">"+spellBuilder.toString()+"</a>");
					//out.println("<input type=\"Hidden\" id=\"PageNumber\" name=\"PageNumber\" value=\""+PageNumber+"\"/>");
				//	keyWord = spellBuilder.toString();
					out.print("<input type=\"Hidden\" id=\"keyWord\" name=\"keyWord\" value=\""+spellBuilder.toString()+"\"/>");
					out.print("<input type=\"Hidden\" id=\"CacheServer\" name=\"CacheServer\" value=\""+host+"\"/>");
					out.print("<input type=\"Hidden\" id=\"CacheServerPort\" name=\"CacheServerPort\" value=\""+port+"\"/>");
					out.print("</form></td></tr></table>");
				}
				else
				{
					out.println("</table>");
				}
				
				
				out.println("<div id = \"header\">");
		    	//out.println("<img src=\"Search.jpg\"/>");
		    	
		    	out.println("</div>");
		    	out.println("</div>");
		    	
		    	out.println("<div id = \"content\">");
		    	out.println("<div id = \"container\"");
		    	
		    	out.println("<div id =\"main\">");
		    	out.println("<div id = \"text\"");	
		    	
		    	/*out.println("<html><body>");
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
		    	*/
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
							out.println("<b>tfIdf</b>: " + tfIdf+ "&nbsp;&nbsp;");
				    	out.println("<br/><br/>");
				    }
				    out.println("</div>");
				    out.println("</div></div>");
				    out.println("<div class=\"clear\"></div>");
				    out.println("<div id=\"footer\"> <p> BingleSearch Inc.</p>");
				    out.println("</div>");
				    out.println("<table align = \"center\"><tr><td align = \"right\">");
			    	//*********
			    	out.println("<form name=\"next\" method =\"GET\" action=\"NextSearch\">");
			    	
			    //	out.println("<br/>");
					//out.println("<input type = \"submit\" value = \"Next>>\"/>");
					out.println("<a href=\"javascript:document.next.submit()\">Next</a>");
					out.println("<input type=\"Hidden\" id=\"PageNumber\" name=\"PageNumber\" value=\""+PageNumber+"\"/>");
					out.println("<input type=\"Hidden\" id=\"Keyword\" name=\"Keyword\" value=\""+keyWord+"\"/>");
					out.println("<input type=\"Hidden\" id=\"CacheServer\" name=\"CacheServer\" value=\""+host+"\"/>");
					out.println("<input type=\"Hidden\" id=\"CacheServerPort\" name=\"CacheServerPort\" value=\""+port+"\"/>");
					out.println("</form>");
					out.println("</td></tr></table>");
					out.println("</div>");	
					
				    out.println("</body></html>");
		    	}
		    	catch(Exception e)
		    	{
		    		e.printStackTrace();
		    	}
		    }
		}
	}
	
	/**
	 * Function to treat each token obtained after parsing HTML document
	 * - Converts to lower case
	 * - Replaces non-alphabet characters with spaces
	 * @param tokens : ArrayList of token lists to be normalized
	 * @return normalized token list
	 */
	public ArrayList<String> normalizeString(ArrayList<String> tokens){
		ArrayList<String> normalizedTokens = new ArrayList<String>(tokens.size());
		for(String token:tokens){
			char[] tokenChars = token.toCharArray();
			for(int i=0;i<tokenChars.length;i++){
				tokenChars[i] = Character.toLowerCase(tokenChars[i]);
				if(!Character.isLetterOrDigit(tokenChars[i]))
					tokenChars[i] = ' ';
			}
			String newToken = new String(tokenChars,0,tokenChars.length);
			String[] splitArray = newToken.split(" ");
			for(String s:splitArray)
				if(s.length() > 0)
					normalizedTokens.add(s);
		}
		return normalizedTokens;
	}
	
	/**
	 * Applies the Porter Stemmer Algorithm to each token
	 * @param tokens : ArrayList of tokens to stem
	 * @return ArrayList of token Strings which have been stemmed
	 */
	public ArrayList<String> applyPorterStemmer(ArrayList<String> tokens){
		ArrayList<String> stemmedTokens = new ArrayList<String>();
		Stemmer stemmer = new Stemmer();
		for(String token:tokens){
			char[] tokenChars = token.toCharArray();
			stemmer.add(tokenChars , tokenChars.length);
			stemmer.stem();
			String stemmedToken = stemmer.toString();
			if(stemmedToken.length() > 0)
				stemmedTokens.add(stemmedToken);
		}
		return stemmedTokens;
	}
}
