package edu.upenn.cis.cis555.youtube;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class YouTubeSearch extends HttpServlet{
	
	/*<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<title>Blue Bliss by Bryant Smith</title>
		<meta http-equiv="content-type" content="text/html;charset=utf-8" />
		<link rel="stylesheet" type="text/css" href="bluebliss.css" />
	</head>
	<body>
	<div id="mainContentArea">
		<div id="contentBox">
	        <div id="title">BlueBliss Inc.</div>
	        
	        <div id="linkGroup">
	            <div class="link"><a href="index.html">Home</a></div>
	            <div class="link"><a href="index.html">About</a></div>
	            <div class="link"><a href="index.html">Portfolio</a></div>
	            <div class="link"><a href="index.html">Contact</a></div>
	        </div>
	        
	        <div id="blueBox"> 
	          <div id="header"></div>
	          <div class="contentTitle">Welcome to BlueBliss Inc.</div>
	            <div class="pageContent">
	              <p>This template was created for those needing a really simple site, without much content.</p>
	              <p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Integer mi. Vivamus sit amet neque vitae sapien bibendum sodales. Curabitur elementum. Duis imperdiet. Donec eleifend porttitor sapien. Praesent leo. Quisque auctor velit sed tellus. Suspendisse potenti. Aenean laoreet imperdiet nunc. Donec commodo suscipit dolor. Aenean nibh. Sed id odio. Aliquam lobortis risus ut felis. Sed vehicula pellentesque quam.</p>
	              <p>Vestibulum augue quam, interdum id, congue semper, convallis non, velit. Quisque augue tortor, tristique ac, scelerisque eget, aliquam id, sem. Aenean lorem. Fusce velit nibh, dapibus quis, laoreet nec, porta a, dui. Nullam ac urna. Proin eget elit. Nunc scelerisque venenatis urna. Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Fusce congue, turpis ut commodo mattis, pede erat fringilla tellus, pulvinar suscipit odio lorem sed pede.</p>
	            </div>
	            <div id="footer"><a href="http://www.aszx.net">web development</a> by <a href="http://www.bryantsmith.com">bryant smith</a> | <a href="http://www.quackit.com">web tutorials</a> | <a href="http://www.htmlcodes.me">HTML code</a> | <a href="http://www.free-templates.me">free templates</a> </div>
	        </div>
		</div>
	</div>
	</body>
	</html>*/
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		String CacheServer = getServletConfig().getInitParameter("CacheServer");
		String CacheServerPort = getServletConfig().getInitParameter("CacheServerPort");
		//PrintWriter out = response.getWriter();
		PrintWriter out = response.getWriter();
		
		//***************************************************************
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
    	out.println("<input type=\"Hidden\" id=\"CacheServer\" name=\"CacheServer\" value=\""+CacheServer+"\"/>");
    	out.println("<input type=\"Hidden\" id=\"CacheServerPort\" name=\"CacheServerPort\" value = \""+CacheServerPort+"\"/>");          
    	out.println("<br><br>");
		out.println("<input type = \"submit\" value = \"Submit Form\"/>");
		out.println("<input type = \"reset\" value = \"Reset Form\"/>");
		out.println("</form>");
    	out.println("</div>");
        out.println("<div id=\"footer\">web development by BingleSearch</div>");
        out.println("</div></div></div></BODY></HTML>");
        
		
		//***************************************************************
		
		/*out.println("<HTML><HEAD><TITLE>BingleSearch</TITLE></HEAD><BODY>");	
		out.println("<h3> Mayuresh Gharat </h3>");
		out.println("<h3> mayuresh </h3>");
		out.println("<form method =\"POST\" action=\"SearchResponse\">");
		out.println("<label for=\"keyWord\">KEYWORD:</label>");
		out.println("<input type=\"text\" id=\"keyWord\" name=\"keyWord\"/>");
		out.println("<input type=\"Hidden\" id=\"CacheServer\" name=\"CacheServer\" value=\""+CacheServer+"\"/>");
		out.println("<input type=\"Hidden\" id=\"CacheServerPort\" name=\"CacheServerPort\" value = \""+CacheServerPort+"\"/>");
		out.println("<br><br>");
		out.println("<input type = \"submit\" value = \"Submit Form\"/>");
		out.println("<input type = \"reset\" value = \"Reset Form\"/>");
		out.println("</form>");
		out.println("</BODY></HTML>");*/
		
	}

}
