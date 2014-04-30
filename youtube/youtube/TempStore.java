package edu.upenn.cis.cis555.youtube;

import java.io.File;
import java.util.ArrayList;
import java.util.Vector;


public class TempStore {

	public static void main(String args[])
	{
		StoreNodeWrapper swrapper  = new StoreNodeWrapper("/home/cis555/SearchEngine","9001");
/*		Vector<StoreCache> cacheVector = swrapper.getCacheEntries();
		int  i =0;
		for(StoreCache cacheEntry : cacheVector)
		{
			//if(cacheEntry.getKeyWord().equals("Tendulkar"))
			{
				//storedFlag = true;
				System.out.println(" Keyword found: "+cacheEntry.getKeyWord());
				break;
			}
		}
*/
		
	    Posting p1 = new Posting("www.SachinTendulkar.com", "SachinTendulkar", "SachT", "Sachin",10);
	    Posting p2 = new Posting("www.BCCI.com", "BCCIsachin", "BCCIsac", "Sachin",20);
	    Posting p3 = new Posting("www.sehwag.com", "Viru", "DareDevil", "Sehwag", 10);
	    Posting p4 = new Posting("www.BCCI.com", "BCCIsehwag", "BCCIseh", "Sehwag",20);
	    Posting p5 = new Posting("www.rahulDravid.com", "RahulDravid", "Wall", "Rahul",10);
	    Posting p6 = new Posting("www.BCCI.com", "BCCIrahul", "BCCIrah", "RDravid", 20);
	    Posting p7 = new Posting("www.VVS.com", "VVS Laxman", "VVS", "Laxman",10);
	    Posting p8 = new Posting("www.hyderabadCricket.com", "HydVVS", "HydLaxman", "Laxman", 20);
	    Posting p9 = new Posting("www.Laxman281.com", "Kolkata Test", "Laxman Innings", "Laxman", 30);
	    Posting p10 = new Posting("www.SRTistheBest.com", "SRT", "SRT rocks", "Sachin",30);
	    
	    ArrayList<Posting> sachinList = new ArrayList<Posting>();
	    sachinList.add(p1);
	    sachinList.add(p2);
	    sachinList.add(p10);
	    
	    ArrayList<Posting> sehwagList = new ArrayList<Posting>();
	    sehwagList.add(p3);
	    sehwagList.add(p4);
	    
	    
	    ArrayList<Posting> rahulList  = new ArrayList<Posting>();
	    rahulList.add(p5);
	    
	    ArrayList<Posting> rdravidList =  new ArrayList<Posting>();
	    rdravidList.add(p6);
	    
	    ArrayList<Posting> laxmanList = new ArrayList<Posting>();
	    laxmanList.add(p7);
	    laxmanList.add(p8);
	    laxmanList.add(p9);
	   
	    
	    swrapper.putIndexEntry("Sachin",sachinList, 10);
	    swrapper.putIndexEntry("Sehwag",sehwagList, 20);
	    swrapper.putIndexEntry("Rahul",rahulList, 30);
	    swrapper.putIndexEntry("RDravid",rdravidList,40);
	    swrapper.putIndexEntry("Laxman",laxmanList, 50);
	    
	    
		/*swrapper.putStoreCache("Sachin", "www.SachinTendulkar.com", 20);
		swrapper.putStoreCache("Sachin", "www.BCCI.com", 30);
		swrapper.putStoreCache("Sehwag", "www.sehwag.com", 40);
		swrapper.putStoreCache("Sehwag", "www.BCCI.com",50);
		swrapper.putStoreCache("Rahul", "www.rahulDravid.com", 60);
		swrapper.putStoreCache("RDravid", "www.BCCI.com", 70);
		swrapper.putStoreCache("Laxman", "www.VVS.com", 80);
		swrapper.putStoreCache("Laxman", "www.hyderabadCricket.com", 90);
		swrapper.putStoreCache("Laxman", "www.Laxman284.com", 10);
		swrapper.putStoreCache("Sachin", "www.SRTistheBest.com",15);
*/		
		swrapper.putUrLInfo("www.SachinTendulkar.com", 10);
		swrapper.putUrLInfo("www.BCCI.com",9);
		swrapper.putUrLInfo("www.sehwag.com",8);
		swrapper.putUrLInfo("www.rahulDravid.com",7);
		swrapper.putUrLInfo("www.VVS.com",6);
		swrapper.putUrLInfo("www.hyderabadCricket.com",5);
		swrapper.putUrLInfo("www.Laxman284.com",4);
		swrapper.putUrLInfo("www.SRTistheBest.com",3);
		
		
		
		
		/*swrapper.putStoreCache("Ganguly", "www.BengalCricket.com");
		swrapper.putStoreCache("Ganguly", "www.Dada.com");
		swrapper.putStoreCache("Dhoni", "www.CSK.com");
		swrapper.putStoreCache("Kohli", "www.kohli.com");
		swrapper.putStoreCache("MSD", "www.CSK.com");
		swrapper.putStoreCache("Gautam", "www.KKR.com");
		swrapper.putStoreCache("Gautam", "www.GautamGambhir.com");
		swrapper.putStoreCache("Rahane", "www.MCA.com");
		swrapper.putStoreCache("Ajinkya", "www.MCA.com");
		swrapper.putStoreCache("Ajinkya", "www.SRTisthebest.com");*/
		
		//StoreCache cacheEntry = swrapper.getCacheEntry("Sachin");
		//System.out.println("key: "+cacheEntry.getKeyWord()+" urlList: "+cacheEntry.getUrls());
//		ArrayList<String> urlList = swrapper.getCacheEntry("Sachin").getUrls();
//		System.out.println("Url List: "+urlList);
	}
	
}