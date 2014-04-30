package edu.upenn.cis.cis555.youtube;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class StoreCache {

	@PrimaryKey
	String keyWord;
	
	//ArrayList<String> urls;
	HashSet<String> urls;
	
	/**
	 * map of url to tf*idf values
	 */
	HashMap<String, Long> urlTfIdf ;//= new HashMap<String, Long>();
	
	public void setKeyword(String keyWord)
	{
		this.keyWord = keyWord;
	}
	
	public void setUrls(HashSet<String> urlSet)
	{
		this.urls = urlSet;
	}

	public String getKeyWord()
	{
		return keyWord;
	}
	
	public HashSet<String> getUrls()
	{
		return this.urls;
	}
	
	public void setTfIdf(HashMap<String, Long> hMap)//String url, long tfIdf)
	{
		//urlTfIdf.put(url, tfIdf);
		
		this.urlTfIdf = hMap;
	}
	
	public HashMap<String, Long> getTfIdfMap()
	{
		return urlTfIdf;
	}
}
