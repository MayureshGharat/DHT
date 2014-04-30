package edu.upenn.cis.cis555.youtube;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

//import ebay.URLReader;

/**
 *
 * @author rajeev jha (jha.rajeev@gmail.com)
 *
 */
public class EbayApi {

	 public final static int REQUEST_DELAY = 10;
	 public HashMap<String, String> titleUrlMap = new HashMap<String, String>();
	 // public int maxResults;
	
	 public String fetchResponse(String reuestString) throws Exception{

	        URL url = new URL(reuestString);
	        URLConnection connection = url.openConnection();
	       // connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.8) Gecko/20100722 Firefox/3.6.8");

	        String line;
	        String response;
	        long totalBytes = 0  ;

	        StringBuilder builder = new StringBuilder();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        while ((line = reader.readLine()) != null) {
	            builder.append(line);
	            totalBytes += line.getBytes("UTF-8").length ;
	            //System.out.println("Total bytes read ::  " + totalBytes);
	        }

	        response = builder.toString();

	        return response ;
	    }

	 
	public void callEbay(String keyWord, int maxResults) throws InterruptedException
	{
	
		String requestString = "http://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME="+
						       "findItemsByKeywords&SERVICE-VERSION=1.0.0&SECURITY-APPNAME="+
						       "Student06-8d69-4e41-a7be-58d8f7cd19f&GLOBAL-ID=EBAY-US&keywords="+ keyWord+
						       "&paginationInput.entriesPerPage="+maxResults;
		
		String response;
		try {
		response = this.fetchResponse(requestString);
		extractData(response);
		Thread.sleep(REQUEST_DELAY);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		  Thread.sleep(REQUEST_DELAY);
	}
	
	
	
	/**
	 * Adapted from Stack OverFlow.....
	 * @param response
	 * @throws Exception
	 */
	public void extractData(String response) throws Exception
	{
		System.out.println("Ebay Response: "+response);
		 XPath xpath = XPathFactory.newInstance().newXPath();
	     InputStream is = new ByteArrayInputStream(response.getBytes("UTF-8"));
	     DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
	     DocumentBuilder builder = domFactory.newDocumentBuilder();
	     Document doc = builder.parse(is);
	     XPathExpression ackExpression = xpath.compile("//findItemsByKeywordsResponse/ack");
	     XPathExpression itemExpression = xpath.compile("//findItemsByKeywordsResponse/searchResult/item");

	    String ackToken = (String) ackExpression.evaluate(doc, XPathConstants.STRING);
	    print("ACK from ebay API :: ", ackToken);
	    if (!ackToken.equals("Success")) {
	            throw new Exception(" service returned an error");
	        }

	   NodeList nodes = (NodeList) itemExpression.evaluate(doc, XPathConstants.NODESET);

	   for (int i = 0; i < nodes.getLength(); i++) {

	            Node node = nodes.item(i);

	            String itemId = (String) xpath.evaluate("itemId", node, XPathConstants.STRING);
	            String title = (String) xpath.evaluate("title", node, XPathConstants.STRING);
	            String itemUrl = (String) xpath.evaluate("viewItemURL", node, XPathConstants.STRING);
	            String galleryUrl = (String) xpath.evaluate("galleryURL", node, XPathConstants.STRING);

	            String currentPrice = (String) xpath.evaluate("sellingStatus/currentPrice", node, XPathConstants.STRING);

	            print("currentPrice", currentPrice);
	            print("itemId", itemId);
	            print("title", title);
	            print("galleryUrl", galleryUrl);
	            titleUrlMap.put(title, galleryUrl);

	        }

	        is.close();

	}
	
   /* public final static String EBAY_APP_ID = "Student06-8d69-4e41-a7be-58d8f7cd19f";
    public final static String EBAY_FINDING_SERVICE_URI = "http://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME="
            + "{operation}&SERVICE-VERSION={version}&SECURITY-APPNAME="
            + "{applicationId}&GLOBAL-ID={globalId}&keywords={keywords}"
            + "&paginationInput.entriesPerPage={maxresults}";
    public static final String SERVICE_VERSION = "1.0.0";
    public static final String OPERATION_NAME = "findItemsByKeywords";
    public static final String GLOBAL_ID = "EBAY-US";
    public final static int REQUEST_DELAY = 3000;
    public final static int MAX_RESULTS = 50;
    private int maxResults;

    public EbayApi() {
        this.maxResults = MAX_RESULTS;

    }

    public EbayApi(int maxResults) {
        this.maxResults = maxResults;
    }*/

   /* public String getName() {
        return IDriver.EBAY_DRIVER;
    }*/
/*
    public void run(String tag) throws Exception {

        String address = createAddress(tag);
        print("sending request to :: ", address);
        String response = URLReader.read(address);
        print("response :: ", response);
        //process xml dump returned from EBAY
        processResponse(response);
        //Honor rate limits - wait between results
        Thread.sleep(REQUEST_DELAY);


    }

    private String createAddress(String tag) {

        //substitute token
        String address = EbayApi.EBAY_FINDING_SERVICE_URI;
        address = address.replace("{version}", EbayApi.SERVICE_VERSION);
        address = address.replace("{operation}", EbayApi.OPERATION_NAME);
        address = address.replace("{globalId}", EbayApi.GLOBAL_ID);
        address = address.replace("{applicationId}", EbayApi.EBAY_APP_ID);
        address = address.replace("{keywords}", tag);
        address = address.replace("{maxresults}", "" + this.maxResults);

        return address;

    }

    private void processResponse(String response) throws Exception {


        XPath xpath = XPathFactory.newInstance().newXPath();
        InputStream is = new ByteArrayInputStream(response.getBytes("UTF-8"));
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = domFactory.newDocumentBuilder();


        Document doc = builder.parse(is);
        XPathExpression ackExpression = xpath.compile("//findItemsByKeywordsResponse/ack");
        XPathExpression itemExpression = xpath.compile("//findItemsByKeywordsResponse/searchResult/item");

        String ackToken = (String) ackExpression.evaluate(doc, XPathConstants.STRING);
        print("ACK from ebay API :: ", ackToken);
        if (!ackToken.equals("Success")) {
            throw new Exception(" service returned an error");
        }

        NodeList nodes = (NodeList) itemExpression.evaluate(doc, XPathConstants.NODESET);

        for (int i = 0; i < nodes.getLength(); i++) {

            Node node = nodes.item(i);

            String itemId = (String) xpath.evaluate("itemId", node, XPathConstants.STRING);
            String title = (String) xpath.evaluate("title", node, XPathConstants.STRING);
            String itemUrl = (String) xpath.evaluate("viewItemURL", node, XPathConstants.STRING);
            String galleryUrl = (String) xpath.evaluate("galleryURL", node, XPathConstants.STRING);

            String currentPrice = (String) xpath.evaluate("sellingStatus/currentPrice", node, XPathConstants.STRING);

            print("currentPrice", currentPrice);
            print("itemId", itemId);
            print("title", title);
            print("galleryUrl", galleryUrl);

        }

        is.close();

    }*/

    private void print(String name, String value) {
        System.out.println(name + "::" + value);
    }

    public static void main(String[] args) throws Exception {
        EbayApi ebay = new EbayApi();
        String keyWord = "Baseball bat";
        ebay.callEbay(java.net.URLEncoder.encode(keyWord, "UTF-8"), 3);

    }
}

