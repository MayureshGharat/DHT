package edu.upenn.cis.cis555.youtube;

import java.util.ArrayList;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class UrlInfo{

	@PrimaryKey
	String url;
	
	long PageRank;
	
	public void setUrl(String url)
	{
		this.url = url;
	}
	
	public void setPageRank(long pageRank)
	{
		this.PageRank = pageRank;
	}

	public String getUrl()
	{
		return url;
	}
	
	public long getPageRank()
	{
		return this.PageRank;
	}
	
}

