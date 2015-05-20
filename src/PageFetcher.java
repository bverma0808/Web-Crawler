import java.util.*;
import java.io.*;
import java.net.*;

/**
* Author: Bharat Verma
* This class is used for fetching a web-page's html text.
*/
public class PageFetcher implements Runnable, LifeCycleHandler {
    LinkedList<Link> messagingQueue;     //this will serve as a mode of communication between CrawlerThread & PageFetcherThread
	boolean life = true;
   
    PageFetcher(LinkedList<Link> messagingQueue){
	   this.messagingQueue = messagingQueue;
	}

    public void kill(){
      life = false;
    }
   /**
   * This method will fetch the html text of a webpage whose URL is passed  to it as argument
   * @param pageURL : URL of the web-page , the content of which needs to be fetched
   * @return a String which is the complete text of the html content of the webpage
                    if the URL is malformed then it will return null
   */
   public String fetch(String pageURL){
      String htmlContent = "";
      try{
         URL url = new URL(pageURL);
         URLConnection connection = url.openConnection();
         BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
         String line;
	  
	     //fetching the content line by line
         while ((line = reader.readLine()) != null) {
            htmlContent += line;
         } 
	  }
	  catch(Exception e){
	     htmlContent = null;
	     e.printStackTrace();
	  }
	  
	  return htmlContent;
   }
   
   /**
    * First method to be run when this thread starts
   */
   public void run(){
   
      while(life){
	     if(!messagingQueue.isEmpty()){
		    //found a new link in the messagingQueue
		    Link link = (Link)messagingQueue.removeFirst();
			String pageURL = link.getURL();
			
			//so we will fetch the html content of the corresponding web-page
			String htmlContent = fetch(pageURL);
			
			//if a URL link is malformed then fetch method will return null
			//so will have to check prior to storing the htmlContent in CentralDataBuckets
			if(htmlContent!=null){
			   //add the Raw html content to RawDataBucket (RDB) of CentralDataBuckets
			   CentralDataBuckets.addToRDB(htmlContent);
			
			   //put the link into the VisitedLinkBucket
			   CentralDataBuckets.addToVLB(link);
			}
		    
			politenessDelay();
		 }
	  }
   }
   
   public void politenessDelay(){
       try{
		 Thread.sleep(2000);
	   }
	   catch(Exception e){
	   }
   }
}