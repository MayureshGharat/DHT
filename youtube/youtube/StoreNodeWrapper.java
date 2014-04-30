package edu.upenn.cis.cis555.youtube;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.StoreConfig;


public class StoreNodeWrapper {
	
/*
 * Envoirnment and Store Variables 
 */
	 private Environment envmnt;
	 private EntityStore store;
	 private StoreNodeAccessor accessor;
	 public String BDBpath;
	 public String storeNumber;

/*
 * Constructor
 */
	 public StoreNodeWrapper(String BDBpath, String storeNumber) {
		this.BDBpath = BDBpath;
		this.storeNumber = storeNumber;
	}

/*
*  Setup the environment and Store    
*/
	 public void setup()throws DatabaseException
	 {   	
	 	 String currentDir = System.getProperty(BDBpath);///home/cis555/hw2");///home/cis555/hw2/src/edu/upenn/cis/cis555");
	 	 File envHome = new File(currentDir,"DataStore"+storeNumber);
	 	 envHome.mkdirs();
	 	 EnvironmentConfig envConfig = new EnvironmentConfig();
	 	 StoreConfig storeConfig = new StoreConfig();
	 	 envConfig.setAllowCreate(true);
	 	 storeConfig.setAllowCreate(true);
	 	 //************** Open the environment and entity store**************************
	 	 envmnt = new Environment(envHome, envConfig);
	 	 store = new EntityStore(envmnt, "EntityStore", storeConfig);
	 	// shutdown();
	 }
	 
	 
/*
 * put in to the Store 	    
 */
	 
	 
	 //************Populate IndexEntry********************************************************
	 public void putIndexEntry(String keyWord, ArrayList<Posting> pList, double idf)
	 {
		 setup();
		 
		 accessor = new StoreNodeAccessor(store);
		 
		IndexEntry iEntry = new IndexEntry(keyWord, pList, idf);
		accessor.pIndexEntry.put(iEntry);
		shutdown();
	 }
	 
	//*******************************Put in to the Cache**************************************
	 public void putStoreCache(String keyWord, String url, long l) throws DatabaseException
	 {
		 StoreCache stCache = getCacheEntry(keyWord);
		 setup();
		    	
		//*************** Open the data accessor. This is used to store persistent objects.***********
		 accessor = new StoreNodeAccessor(store);
		        
		 // **************Instantiate and store some User classes**********************
		 
		
		 if(stCache != null)
		 {
			 
			 HashSet<String> urlSet = stCache.getUrls();
			 urlSet.add(url);
			 HashMap<String, Long> urlTfIdfMap = stCache.getTfIdfMap();
			 urlTfIdfMap.put(url, l);
			 StoreCache cache = new StoreCache();
			 cache.setKeyword(keyWord);
			 cache.setUrls(urlSet);
			 cache.setTfIdf(urlTfIdfMap);
			 
			 accessor.pCache.put(cache);
		 }
		 else
		 {
			 StoreCache cache = new StoreCache();
			 HashSet<String> urlSet = new HashSet<String>();
			 cache.setKeyword(keyWord);
			 urlSet.add(url);
			 cache.setUrls(urlSet);
			 HashMap<String, Long> urlTfIdfMap = new HashMap<String, Long>();
			 urlTfIdfMap.put(url, l);
			 cache.setTfIdf(urlTfIdfMap);
			 accessor.pCache.put(cache);
		 }
		//***********Set the Primary Index****************************************
	//	  accessor.pCache.put(cache);
		        
		  shutdown();
	 }
	 
	 public void putUrLInfo(String url, long PageRank) throws DatabaseException
	 {
		 setup();
		    	
		//*************** Open the data accessor. This is used to store persistent objects.***********
		 accessor = new StoreNodeAccessor(store);
		        
		 // **************Instantiate and store some User classes**********************
		 
		 UrlInfo ulInfo = new UrlInfo();
		 ulInfo.setUrl(url);
		 ulInfo.setPageRank(PageRank);
		 accessor.pUrlInfo.put(ulInfo);
		//***********Set the Primary Index****************************************
	//	  accessor.pCache.put(cache);
		        
		  shutdown();
	 }
	 
	 
	 
	 
	
/*
 * Get from the Store	    
 */
	 
	 
	 public IndexEntry getIndexEntry(String keyword)
	 {
		 setup();
		 
		 accessor = new StoreNodeAccessor(store);
		 
		 IndexEntry iEntry = accessor.pIndexEntry.get(keyword);
		 
		 shutdown();
		 
		 return iEntry;
	 }
	 
	 
	//************************Get from the cache******************************************
	 public StoreCache getCacheEntry(String keyWord)
	 {
		 setup();
		 //************** Open the data accessor. This is used to store persistent objects.**************
		 accessor = new StoreNodeAccessor(store);
		      
		//*************Get cacheEntry with the corresponding keyWord********************************
		 StoreCache cacheEntry = accessor.pCache.get(keyWord);
		     
		 shutdown();
		 return cacheEntry; 
	 }
		    
		    
	 //**************************Get All CacheEntries*********************************
	/* public Vector<StoreCache> getCacheEntries()
	 {
		 setup();
		    	
		 Vector<StoreCache> cacheVector = new Vector<StoreCache>(); 
		 //************** Open the data accessor. This is used to store persistent objects.**************
		  accessor = new StoreNodeAccessor(store);
		    	
		  EntityCursor<StoreCache> pi_cursor = accessor.pCache.entities();
		  for (StoreCache cache : pi_cursor) 
		  {
		   	cacheVector.add(cache);
		  }
		  
		  pi_cursor.close();
		  shutdown();
		  return cacheVector;
	}*/
	 
	 
	 public UrlInfo getUrlInfo(String url)
	 {
		 setup();
		 //************** Open the data accessor. This is used to store persistent objects.**************
		 accessor = new StoreNodeAccessor(store);
		      
		//*************Get cacheEntry with the corresponding keyWord********************************
		 //StoreCache cacheEntry = accessor.pCache.get(keyWord);
		 UrlInfo ulInfo = accessor.pUrlInfo.get(url);
		   
		 shutdown();
		 return ulInfo; 

	 }
	
 /*
  * Delete from the dataStore
  */
	 //**************Delete keyWord********************************************************
	/* public void deletekeyWord(String keyWord)
	 {
		 setup();
		 accessor = new StoreNodeAccessor(store);
		 accessor.pCache.delete(keyWord);
		 shutdown();
	 }		    
*/
/*
 * Close our environment and store.
 */
	 public void shutdown()throws DatabaseException 
	 {
		  store.close();
		  envmnt.close();
	 }   
		
}
