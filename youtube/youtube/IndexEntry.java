package edu.upenn.cis.cis555.youtube;

import java.util.ArrayList;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class IndexEntry {
	
	@PrimaryKey
	String token;
	ArrayList<Posting> postingList;
	
	double idf;
	
	public IndexEntry(){}
	
	public IndexEntry(String token, ArrayList<Posting> postingList, double idf){
		this.token = token;
		this.postingList = postingList;
		this.idf = idf;
	}
	
	public String getToken(){
		return token;
	}
	
	public ArrayList<Posting> getPostingList(){
		return postingList;
	}
	
	public double getIdf()
	{
		return idf;
	}
}
