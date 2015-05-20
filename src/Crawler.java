import java.util.*;

/**
* Author: Bharat Verma
* This is our main Crawler class which runs a CrawlerThread
* This CrawlerThread will keep on fetching unvisited links from LinkPoolBucket and after checking the link for various constraints
* It will pass the link to the PageFetcherThread.
* Constraints can be :=>
*    link already visited or not
*    link is of blocked type or not
*    link's depth is smaller than the allowed depth or not
*    count of visited links till now is still smaller than the max Links allowed to visit or not
*/
public class Crawler implements Runnable , LifeCycleHandler {
   HashMap<String,Object> configs;                 //map of config properties
   LinkedList<Link>  messagingQueue;             //this will serve as a mode of communication between CrawlerThread & PageFetcherThread
   boolean life = true;
   
   public Crawler(HashMap<String, Object> configs,  LinkedList<Link> messagingQueue){
	  this.configs = configs;
	  this.messagingQueue = messagingQueue;
   }

   public void kill(){
      life = false;
   }   
   
   /**
   * This method will check whether a particular type of link is allowed or not
   * LinkType means :=> whether its a link to a css , or js or html or a jpeg etc.
   * the method will verify it from the config property
   * @param type of link to be checked
   * @return true if the link is allowed  , otherwise false
   */
   public boolean isAllowedLinkType(Link link){
      String linkType = getLinkType(link.getURL());
      List blockedTypes = (LinkedList)configs.get("blockedLinkTypes");
	  
	  if(blockedTypes.contains(linkType)){
	     return false;     //type not allowed
	  }
	  
      return true;       //allowed
   }
   
   /**
   * This method will return the link type of the url. Right now this method is not very intelligent
   * suppose if the link is to some css file , then this method will return "css" , 
   * if its a javascript file then it will return "js" and so on. At first it will try to 
   * find a match from the known types , otherwise it will return the type "html"
   * @param String url : type of which to be find
   * @return String describing the type of url or link
   */
   public String getLinkType(String url){
      //array of known link types
      String knownTypes[] = {"js","css","html","htm", "jpeg", "jpg", "png", "mp3","pdf"};
	  
      String type = url.substring(url.lastIndexOf('.')+1 , url.length());
	  
	  //try to find the type of the link from the known types
	  for(int i=0; i<knownTypes.length; i++){
	     //if we find a match then return the corresponding type
	     if(type.equalsIgnoreCase(knownTypes[i])){
		    return type.toLowerCase();
		 }
	  }
	  
	  //here we are assuming that the link is for an html page
	  type = "html";
	  return type;
   }
   
   /**
   * Check whether given link is already visited or not
   */
   public boolean alreadyVisitedLink(Link link){
       return CentralDataBuckets.isVisited(link);
   }

   /**
   * This method will check for the depth of the link to be visited
   * @param Link to be visited
   * @return true if depth is less than or equals to crawlDepth otherwise false
   */
   public boolean checkLinkDepth(Link link){
      int depth = link.getDepth();
      int crawlDepth = (Integer)configs.get("crawlDepth");
	  if(depth>=crawlDepth){
	     return false;
	  }
	  return true;
   }   
   
   public boolean isStackOverflowed(){
      Logger.info("inside isStackOverflowed method");
      Integer maxLinksToCrawl = (Integer)configs.get("maxLinksToCrawl");
	  //System.out.println(maxLinksToCrawl);
	  if(maxLinksToCrawl==null){
	     maxLinksToCrawl = 100;
	  }
	  int currentCountOfVisitedLinks = CentralDataBuckets.visitedLinkBucket.size();
	  if(currentCountOfVisitedLinks>=maxLinksToCrawl){
	     Logger.info("inside isStackOverflowed method returning true");
	     return true;
	  }
	  Logger.info("inside isStackOverflowed method returning false:=> " + currentCountOfVisitedLinks + " | " + maxLinksToCrawl);
	  return false;
   }
   
   /**
   * Test all the constraints , and return is the link passes all the constraints
   */
   public boolean testConstraints(Link link){
      if(isAllowedLinkType(link) && !alreadyVisitedLink(link) && checkLinkDepth(link) && !isStackOverflowed()){
	    Logger.info("inside testConstraints method returning true");
	     return true;
	  }
	  Logger.info("inside testConstraints method returning false");
	  return false;
   }
   
   /**
   * This method will make the CrawlerThread sleep for some time
   * So that it will not overload the server with large number of requests
   */
   public void politenessDelay(){
      try{
	     Integer delay = (Integer)configs.get("politenessDelay");
		 if(delay==null){
		    delay = 2000;
	     }
	     Thread.sleep(delay);
	  }
	  catch(Exception ex){
	     System.out.println(ex);
	  }
   }
   
   /**
   * First method to be run when this thread starts
   */
   public void run(){
   
      while(life){
	  
	     //fetch next link from LinkPoolBucket
	     Link link = CentralDataBuckets.removeFromLPB();
         Logger.info("From crawler thread");
		 //check if its not null
	     if(link != null){
		    if(testConstraints(link)){
			   //link passed the constraints, so we will dispatch it to PageFetcherThread via this messagingQueue
		       messagingQueue.addLast(link);
			}
		 }
		
		 //this delay will help the crawler not to block the server from which it is fetching the data
		 //because  if we keep on fetching the data from server at a fast rate , then the server either 
		 //will block us or it will degrade server's performance in serving actual users, 
		 politenessDelay();
	  }
	  
   }
   
}