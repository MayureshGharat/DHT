/*package edu.upenn.cis.cis555.youtube;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import com.google.gdata.client.Query;
import com.google.gdata.client.Service;
import com.google.gdata.client.youtube.YouTubeQuery;
import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.Category;
import com.google.gdata.data.extensions.FeedLink;
import com.google.gdata.data.media.mediarss.MediaThumbnail;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.VideoFeed;
import com.google.gdata.data.youtube.YouTubeMediaContent;
import com.google.gdata.data.youtube.YouTubeMediaGroup;
import com.google.gdata.data.youtube.YouTubeNamespace;
import com.google.gdata.util.ServiceException;

public class YouTubeClient {

	MyApp callingMyApp;
	StoreNodeWrapper storeWrapper;
	String BDBpath;
	int maxStorageSize;
	int maxStorageTime;
	int entryCount = 0;
	int chSize = 0;
	
	public YouTubeClient() {}
	
	YouTubeClient(String path)
	{
		this.BDBpath = path;
	}
	
	public void initializeParam(MyApp app, String storeNumber,int maxStorageSize, int maxStorageTime)//StoreNodeWrapper storeWrapper)
	{
		callingMyApp = app;
		//this.storeWrapper = storeWrapper;
		storeWrapper = new StoreNodeWrapper(BDBpath, storeNumber);
		this.maxStorageSize = maxStorageSize;
		this.maxStorageTime = maxStorageTime;
		System.out.println("maxStorageSize: "+maxStorageSize+ " maxStorageTime: "+maxStorageTime);
	}
	
	public String getLRUkey()
	{
		String delKey = null;
		Vector<StoreCache> cacheVector = storeWrapper.getCacheEntries();
		long min = 0;
		if(cacheVector.size() > 0)
		{
			min = cacheVector.get(0).getLRUtime();
			delKey = cacheVector.get(0).getKeyWord();
		}
		for(StoreCache cacheEntry : cacheVector)
		{
			if(cacheEntry.getLRUtime() < min)
			{
				min = cacheEntry.getLRUtime();
				delKey = cacheEntry.getKeyWord();
			}
		}
		return delKey;
	}
	public int getCacheSize()
	{
		Vector<StoreCache> cacheVector = storeWrapper.getCacheEntries();
		int cacheSize = 0;
		for(StoreCache cacheEntry : cacheVector)
		{
			cacheSize = cacheEntry.getContent().getBytes().length + cacheEntry.getKeyWord().getBytes().length;
		}
		
		return cacheSize;
	}
	
	public String callYouTubeClient(String keyword)
	{
		YouTubeQuery query;
		YouTubeService service;
		VideoFeed videoFeed = null;
		//try
		try
		{
			query= new YouTubeQuery(new URL("http://gdata.youtube.com/feeds/api/videos"));
			service = new YouTubeService("cis555YoutubeClient", "AI39si7xaa65ImAnQOAxjiHyo7PQKLHSkgdNpOzAFlKdoGGh4NLLXybJJlXHUtqgbA1FO67fFqgfwCrOkN1b5F9Ig-99z3qqJQ");
			query.setFullTextQuery(keyword);
			query.setSafeSearch(YouTubeQuery.SafeSearch.NONE);
			videoFeed= service.query(query, VideoFeed.class);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version = \"1.0\"?>");
		sb.append("<?xml-stylesheet type=\"text/xsl\" href=\"youtube.xsl\"?>");
		sb.append("<documentcollection>");
		for(VideoEntry vEntry: videoFeed.getEntries())
		{
			 YouTubeMediaGroup mediaGroup = vEntry.getMediaGroup();
			sb.append("<document>");
			sb.append("<title>"+vEntry.getTitle().getPlainText()+"</title>");
			String link = mediaGroup.getPlayer().getUrl();getThumbnails().get(1).getUrl();//getYouTubeContents().get(0).getUrl();
			sb.append("<link>"+link+"</link>");
			sb.append("<description>"+mediaGroup.getDescription().getPlainTextContent()+"</description>");
			sb.append("</document>"); 
		}
		sb.append("</documentcollection>");
		System.out.println("\n");
		System.out.println(sb.toString());
		String str = sb.toString().replaceAll("&", "&amp;");
		//callingMyApp.DHT.put(keyword, sb.toString());
		long maxTime = System.currentTimeMillis()+maxStorageTime*1000;
		int currEntrySize = keyword.getBytes().length + str.getBytes().length + 8 ;
		System.err.println("Curr Size: "+currEntrySize);
		if(entryCount > 0)
		{
			chSize = getCacheSize();
			while(chSize + currEntrySize > maxStorageSize)
			{
				String deleteKey = getLRUkey();
				System.err.println("CacheSize before: "+chSize+ "Delete Keyword:before "+deleteKey);
				storeWrapper.deletekeyWord(deleteKey);
				System.err.println("CacheSize: "+chSize+ "Delete Keyword: "+deleteKey);
				if(callingMyApp.hashCache.contains(keyword))
				{
					callingMyApp.hashCache.remove(keyword);
				}
				--entryCount;
				if(entryCount == 0)
				{
					break;
				}
				chSize = getCacheSize();
			}
		}
		storeWrapper.putStoreCache(keyword,str,maxTime,System.currentTimeMillis());// sb.toString());
		//return str;//sb.toString();
		return keyword+"~"+str;
	}
	public String searchVideos(String keyword) //throws IOException, ServiceException
	{
		
		 * Search in the local cache first
		 * if not found then print the err message
		 * and get the data from youtube and store it in the cache.
		 
		boolean storedFlag = false;
		Vector<StoreCache> cacheVector = storeWrapper.getCacheEntries();
		//int entryCount = 0;
		//int chSize = 0;
		String deleteKey = null;
		long min = 0;
		if(cacheVector.size() > 0)
		{
			min = cacheVector.get(0).getLRUtime();
			deleteKey = cacheVector.get(0).getKeyWord();
		}
		for(StoreCache cacheEntry : cacheVector)
		{
			entryCount++;
			
			if(cacheEntry.getKeyWord().equals(keyword))
			{
				storedFlag = true;
				break;
			}
			if(cacheEntry.getLRUtime() < min)
			{
				min = cacheEntry.getLRUtime();
				deleteKey = cacheEntry.getKeyWord();
			}
		}
	
		//if(callingMyApp.DHT.containsKey(keyword))
		if(storedFlag == true)
		{
			
			long storeTime = storeWrapper.getCacheEntry(keyword).getStoreTime();
			if(System.currentTimeMillis() > storeTime)
			{
				System.err.println("Query for the "+keyword+" resulted in a HIT but the cache was expired");
				return callYouTubeClient(keyword);
			}
			else
			{
				System.err.println("Query for the "+keyword+" resulted in a HIT");
				StoreCache cache = storeWrapper.getCacheEntry(keyword);
				String str = storeWrapper.getCacheEntry(keyword)cache.getContent();//callingMyApp.DHT.get(keyword);
				long maxStorageTime = cache.getStoreTime();
				storeWrapper.putStoreCache(keyword, str, maxStorageTime, System.currentTimeMillis());
				return keyword+"~"+str;
			}
		}
		else
		{
			System.err.println("Query for the "+keyword+" resulted in a MISS");
			return callYouTubeClient(keyword); 
		}		
	}
	
	public static void main(String args[])
	{
		YouTubeClient yClient = new YouTubeClient("");
		String s = yClient.searchVideos("Rock");
	}
}
*/