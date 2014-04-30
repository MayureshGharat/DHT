package edu.upenn.cis.cis555.youtube;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;

public class StoreNodeAccessor {

	/*
	 * Primary and Secondary Handlers for StoreCache Class
	 */
	PrimaryIndex<String, StoreCache> pCache;
	PrimaryIndex<String, UrlInfo> pUrlInfo;
	PrimaryIndex<String, IndexEntry> pIndexEntry;
	
	/*
	 * open the indices 
	 */
	StoreNodeAccessor(EntityStore store)throws DatabaseException 
	{
		 pCache = store.getPrimaryIndex(String.class, StoreCache.class);
		 pUrlInfo = store.getPrimaryIndex(String.class, UrlInfo.class);
		 pIndexEntry = store.getPrimaryIndex(String.class, IndexEntry.class);
	}
	
}
