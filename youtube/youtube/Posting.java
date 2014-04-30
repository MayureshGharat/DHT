package edu.upenn.cis.cis555.youtube;

import java.io.Serializable;
import java.util.ArrayList;

import com.sleepycat.persist.model.Persistent;

@Persistent
public class Posting implements Serializable{
	
	private static final long serialVersionUID = 1L;
	String docUrl;
	String docTitle;
	String docSignature;	
	String token;
	
	boolean anchor;
	boolean title;
	boolean meta;
	boolean url;
	boolean header;
	
	int frequency;
	int[] positions; 
	
	double tf;
	
	String postingKey;
	
	
	public Posting(){};
	
	public Posting(String docUrl, String docTitle, String docSignature, String token, double tf){
		this.docUrl = docUrl;
		this.docTitle = docTitle;
		this.docSignature = docSignature;
		this.token = token;
		
		this.anchor = false;
		this.title = false;
		this.meta = false;
		this.url = false;
		this.header = false;
		
		this.frequency = 0;
		this.tf = tf;
		positions = null;
		
		postingKey = token + "_" + docUrl;
	}
	

	
	/** STRING SETTER METHODS */
	public void setDocUrlString(String s)  { docUrl = s; }
	public void setDocTitleString(String s){ docTitle = s; }
	public void setDocSignatureString(String s){ docSignature = s; }
	public void setTokenString(String s){ token = s; }
	
	/** BOOLEAN SETTER METHODS*/
	public void setAnchor(boolean b){ anchor = b; }
	public void setTitle(boolean b) { title = b; }
	public void setMeta(boolean b)  { meta = b; }
	public void setUrl(boolean b)   { url = b; }
	public void setHeader(boolean b){ header = b; }
	
	public void setFrequency(int f) { frequency = f; }
	public int getFrequency(){ return frequency; }
	public void incrementFreq(){ frequency++; }	
	
	public void updatePositionArray(ArrayList<Integer> positions){
		if(positions !=null){
			int arrayLength = positions.size();
			this.positions = new int[arrayLength];
			for(int i = 0; i< arrayLength; i++)
				this.positions[i] = positions.get(i);
		}
	}
	
	public void updateTfScore(int maxFreq){
		this.tf = (double)this.frequency / (double)maxFreq;
	}
	
	//*********************************************
	
	public String getTitle()
	{
		return docTitle;
	}
	public double getTf()
	{
		return tf;
	}
	
	public String getUrl()
	{
		return docUrl;
	}
	
	//**********************************************
	public String toString(){
		/*String postingString = "";
		postingString += "Token : "+token + "\n";
		postingString += "URL: "+docUrl + "\n";
		postingString += "Title : "+docTitle + "\n";
		if(anchor) postingString += "Anchor ";
		if(title) postingString += "Title ";
		if(meta) postingString += "Meta ";
		if(url) postingString += "Url ";
		if(header) postingString += "Header ";
		postingString += "Plain\n";
		postingString += "Frequency : "+frequency+ "\n";
		postingString += "Positions :";
		for(int i:positions)
			postingString += i + ",";
		postingString += "\n";*/
		String postingString = docTitle;
		return postingString;
	}
	
}
