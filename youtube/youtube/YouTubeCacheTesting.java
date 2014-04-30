/*package edu.upenn.cis.cis555.youtube;

import junit.framework.Assert;
import junit.framework.TestCase;

public class YouTubeCacheTesting extends TestCase {
	
	public void testCache()
	{
		YouTubeClient ytClient = new YouTubeClient();
		ytClient.storeWrapper = new StoreNodeWrapper("/home/cis555/hw3", "1234Testing");
		ytClient.storeWrapper.putStoreCache("Sachin", "xyz", System.currentTimeMillis()+8000, 10000);
		String str = ytClient.searchVideos("Sachin");
		String[] strArray = str.split("~", 2);
		
		Assert.assertEquals("xyz",strArray[1].trim());
	}
	
	public void testService()
	{
		YouTubeClient ytClient = new YouTubeClient();
		ytClient.callingMyApp = new MyApp();
		ytClient.storeWrapper = new StoreNodeWrapper("/home/cis555/hw3", "1234Testing1");
		ytClient.storeWrapper.putStoreCache("Rahul", "uvw", 0, 10000);
		String str = ytClient.searchVideos("Rahul");
		//	ytClient.callingMyApp = new MyApp();
		
		Assert.assertEquals(false,ytClient.callingMyApp.hashCache.contains("Rahul"));//strArray[1].trim().equals("uvw"));
	}

}
*/