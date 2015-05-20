import java.util.*;

/**
* Author: Bharat Verma
* This class serves as a single point for data for three threads
* "Crawler_Thread" , "Fetcher_Thread" and "Content_Parser_Thread"
* It has two buckets which in actual are two Queues for storing two different types of data
*    1. RawDataBucket :-> This bucket contains chunks of raw HTML content which are still to be parsed by Parser Thread
*    2. LinkPoolBucket :-> This bucket contains pool of links which are produced by parsing the raw chunks of HTML from RawDataBucket, All the links in the LinkPoolBucket 
*       are unvisited.
* And There is also a Set of visited Links which contains unique links which are visited till now, By the word "visited" we mean here that those links are processed by "Fetcher_Thread"
*    3. visitedLinkBucket :-> Contains a unique set of visited links , its a HashMap
*/
public class CentralDataBuckets {
    static ArrayList<String>          rawDataBucket;
	static ArrayList<Link>             linkPoolBucket;
	static HashMap<String, Link>   visitedLinkBucket;
	
	static{
	   rawDataBucket = new ArrayList <String>();
	   linkPoolBucket  = new ArrayList <Link>();
	   visitedLinkBucket = new HashMap<String, Link>();
	}
	
	
	//********************************************************************************
	//**************** Methods Related to RawDataBucket **************************
	//********************************************************************************
	
	/**
	* This method will add a chunk of raw html data at the back side of RawDataBucket (RDB)
	*/
	static void addToRDB(String dataChunk){
	   rawDataBucket.add(dataChunk);
	}
	
	/**
	* This method will return the first data chunk from the front of RawDataBucket
	*/
	static String removeFromRDB(){
	    String dataChunk=null;
		if(!rawDataBucket.isEmpty()){
	       dataChunk = rawDataBucket.remove(0);
		}
		return dataChunk;
	}
	
	
	//********************************************************************************
	//**************** Methods Related to LinkPoolBucket **************************
	//********************************************************************************
	/**
	* This method will add a new link at the back side of LinkPoolBucket (LPB)
	*/
	static void addToLPB(Link link){
	   linkPoolBucket.add(link);
	}
	
	/**
	* This method returns the front Link element from the LinkPoolBucket
	* otherwise if LinkPoolBucket is empty then it returns null
	*/
	static Link removeFromLPB(){
	    Link link=null;
		if(!linkPoolBucket.isEmpty()){
	       link = linkPoolBucket.remove(0);
		}
		return link;
	}
    
	
	//********************************************************************************
	//**************** Methods Related to visitedLinkBucket ************************	
	//********************************************************************************
	
	/**
	* This method will return true if the Link is already visited otherwise return false
	* It will check the existence of link in the visitedLinkBucket
	*/
	static boolean isVisited(Link link){
	   String key = link.getURL();
	   Link foundLink = (Link)visitedLinkBucket.get(key);
	   if(foundLink==null){
	      return false;
	   }
	   return true;
	}
	
	static int getSizeOfVLB(){
	   return visitedLinkBucket.size();
	}
	
	/**
	* This method will add a new link to the VisitedLinkBucket (VLB)
	*/
	static void addToVLB(Link link){
	   String key = link.getURL();
	   visitedLinkBucket.put(key, link);
	}
	
	
}