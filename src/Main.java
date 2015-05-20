import java.util.*;

//  Author : Bharat Verma

//**************************************************************
//***** MAIN STARTING POINT OF THE APPLICATION *****
//**************************************************************

public class Main{
    
	static HashMap<String, LifeCycleHandler> resources;

    public static void main(String []args){
	   resources = new HashMap<String, LifeCycleHandler>();
	   LinkedList<Link> messagingQueue = new LinkedList<Link>();
	   
	   //we are expecting here some seed urls in the command line args
	   if(args.length==0){
	       //if seed URL is not there then we can't proceed further
	       printMessageAndExit(1);
	   }
	   
	   //put the seeds in the LinkPoolBucket which is one of the CentralDataBuckets
	   putSeed(args);
	   
	   //loads the configurations in the system from an external file
	   ConfigManager configManager = ConfigManager.getConfigManager("crawler-config.properties");
	
	   //starting the crawlerThread
       Crawler crawler = new Crawler(configManager.configs, messagingQueue);
	   resources.put("CrawlerThread", crawler);
	   Thread crawlerThread = new Thread(crawler);
	   crawlerThread.start();
	  
	   //starting the PageFetcherThread 
	   PageFetcher pageFetcher = new PageFetcher(messagingQueue);
	   resources.put("PageFetcherThread", pageFetcher);
	   Thread pageFetcherThread = new Thread(pageFetcher);
	   pageFetcherThread.start();
	   
	   //starting the PageParserThread
	   PageParser pageParser = new PageParser();
	   resources.put("PageParserThread", pageParser);
	   Thread pageParserThread = new Thread(pageParser);
	   pageParserThread.start();
	   
	   //starting the UserInputReader Thread for getting inputs from the user
	   UserInputReader userInputReader = new UserInputReader();
	   resources.put("userInputReaderThread", userInputReader);
	   Thread userInputReaderThread = new Thread(userInputReader);
	   userInputReaderThread.start();
	   
	   //starting the Logger Thread for printing logs if enabled
	   Logger logger = new Logger();
	   resources.put("loggerThread", logger);
	   Thread loggerThread = new Thread(logger);
	   loggerThread.start();
	}

	/**
	* This method will add all the seed links to the LinkPoolBucket (LPB)
	*/
	static void putSeed(String []seeds){
	   for(int i=0; i<seeds.length; i++){
	      System.out.println("Putting seed:=> "+ seeds[i]);
	      CentralDataBuckets.addToLPB(new Link(seeds[i]));
	   }
	}
 
    /**
	* This method will stop all threads and thus stops the crawler
    */
    static void stopCrawler() {
	   Logger.info("Going to stop the crawler");
       Set threadSet = resources.entrySet();
	   Iterator threadPool = threadSet.iterator();
	   while(threadPool.hasNext()){
	      Map.Entry element = (Map.Entry)threadPool.next();
		  LifeCycleHandler threadHandle = (LifeCycleHandler)element.getValue();
		  threadHandle.kill();
		  Logger.info("Thread = " + element.getKey() + " Killed Successfully");
	   }
	   printMessageAndExit(2);
	}
	
	/**
	* This method will return the count of visitedLinks so far
	* Its just the size of VisitedLinkBucket of CentralDataBuckets
	*/
	static void displayVisitedLinksCount(){
	   System.out.println("************************************************************");
	   System.out.println("Total Number of links visited till now are:=> "+ CentralDataBuckets.visitedLinkBucket.size());
	   System.out.println("************************************************************");
	}
	
	
	/**
	* This method enables crawler logging
	*/
	static void toggleLogging(boolean flag){
	    Logger.toggleLogging(flag);
	}


    /**
	* This method will print messages and terminate the program
    */	
    static void printMessageAndExit(int type){
	
	   switch(type){
	         case 1: System.out.println("**************************************** Message From Crawler ****************************************************");
	                     System.out.println("       Atleast one URL is needed as a seed for the Crawler");
	                     System.out.println("       Example Command:=>   java Main www.abcxyz.com");
	                     System.out.println("       or you can enter more than 1 seed as a space separated list like this:=>   java Main www.abcxyz.com  www.jhk.com ");
	                     System.out.println("**********************************************************************************************************************");
	                     break;
						 
			 case 2: System.out.println("............Crawler stopped successfully.........");
			             break;
	   }
	   System.exit(0);
	}
}