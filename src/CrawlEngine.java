import java.util.*;
import java.io.*;

/**
* This class contains the main method which is the starting point of the program. 
* All the resources needed for a Crawler setup are initialised in this class 
*/
public class CrawlEngine{
   PageFetcher pageFetcher;                       //it fetches the htmlContent by visiting the page whose link is provided by crawler
   PageParser pageParser;                          //it parses the htmlContent fetched by pageFetcher
   ConfigManager configManager;               //contains all the system level configurations
   DataCenter dataCenter;                            //it will keep the records for visited links & unvisited links
   LinkValidator linkValidator;                       //it will help us validate the link prior to visiting it
   
   /**
   * Initialize engine parts
   */
   CrawlEngine(){
	  pageFetcher = new PageFetcher();
	  pageParser = new PageParser();
	  configManager = ConfigManager.getConfigManager("CrawlerConfig.txt");   //pass the name of the configuration file 
	  int capacity = (Integer)configManager.configs.get("maxLinksToCrawl");
	  dataCenter = new DataCenter(capacity);
	  linkValidator = new LinkValidator(configManager.configs);
   }
   
    /**
	* This method will put some initial links to the unvisited link pool in dataCenter
	* these links will act as seed for the crawler 
	* @param seeds   some base url ,array of strings
	* @return
	*/
	public void putSeed(String []seeds){
	   for(int i=0; i<seeds.length; i++){
	      //making a new link with 0 as depth
          Link link = new Link(seeds[i], 0);	      
	      dataCenter.putLink(link);
	   }
	}
	
   /**
   * This method runs the main loop which is: 
   *  While there are links to visit and enough number of links are not visited yet, do the below in order:
   *    1. get the next link to visit
   *    2. fetch the HTML content of the page identified by the link
   *    3. parse the HTML content to get more number of links
   * @param
   * @return 
   */
   public void runEngine() {
       System.out.println("Crawler engine started .....You can press Ctrl^C to stop it ......\n") ;
   	
	   int index = 0;             //acts as serial number for the link that we just visited , its just for printing user friendly messages
	   
	   //loop will keep on running until either of the two cases occur
	   //  i. There are no more links to visit 
	   //  ii. The crawler has reached to its capacity , so it can't store links any more 
       while(dataCenter.hasMoreUnvisitedLinks()  &&  !dataCenter.hasCapacityReached()){
	      // get next unvisited link from pool
	      Link currentLink = dataCenter.getNextUnvisitedLink();
		  
		  //check if its worth to visit it or not (well if its an image file, we won't be getting any more links out of it)
		  if(linkValidator.isVisitable(currentLink)) {
		     
			 //link is valid, so fetch the html content for the webpage identified by this link
	         String htmlContent = pageFetcher.fetch(currentLink.getURL());
			 
			 //if the link happens to be a bad link (not a http or https link), then returned htmlContent will be null, so we need to have a check for this.
			 if(htmlContent!=null){
			    
				//now we are sure that the link is good , so we will increase the serial number 
			    index++; 
				
				//print a message for user
				String message = "#Link-"+index+":=> (DEPTH : " + currentLink.getDepth() + ") and (URL : "+ currentLink.getURL() + ")\n";
			    System.out.println(message);
				
				//store the visited link into a text file 
				storeLinkInFileBase(message);
				
				//put the visited link into the visited link pool , so that we don't visit it again
				dataCenter.markVisited(currentLink);
				
				//parse the htmlContent to extract more number of links out of it
			    List<String> list = pageParser.parse(htmlContent);
				
				//store all the links one by one into the unvisited link pool
				Iterator iterator = list.iterator();
				while(iterator.hasNext()){
				   String url = (String)iterator.next();
				   //obviously the depth of these links will be greater than the depth of their parent link by 1
		           Link newLink = new Link(url, currentLink.getDepth() + 1); 
	               dataCenter.putLink(newLink);
				}
			 }
			 
			 //if we continuously keep on requesting the server , then it will increase the load on server which can result into either of the two problems
			 //   i. server will block us and we can't be able to request server any further
			 //   ii.other users won't be able to get their requests fulfilled, because server will become very unresponsive because of our crawler
			 //Thus to prevent those conditions, we will delay the process with the help of this method call
			 politenessDelay();
		  }
	   }
   }
   
   /**
    * if we continuously keep on requesting the server , then it will increase the load on server which can result into either of the two problems
    *	  i. server will block us and we can't be able to request server any further
	*    ii.other users won't be able to get their requests fulfilled, because server will become very unresponsive because of our crawler
	* Thus to prevent those conditions, we will delay the process with the help of this method call
	* @param 
	* @return 
    */
   public void politenessDelay(){
      try{
	     //get the 'delay' time from configManager
	     Integer delay = (Integer)configManager.configs.get("politenessDelay");
		 if(delay==null){
		    delay = 500;    //default delay time in ms
	     }
	     Thread.sleep(delay);
	  }
	  catch(Exception ex){
	     System.out.println(ex);
	  }
   }
   
   /**
   * This method will store the visited link in an external text file
   * @param content  to be saved to the file
   * @return
   */
   public void storeLinkInFileBase(String content){
      try {
            String fileName = (String)configManager.configs.get("linkStoreFileName");
			File file = new File(fileName);
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
            //opening the file in append mode
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
   }
   
   
   /**
   *  This is the main method of our application, which starts the crawler engine
   *  This will instantiate all the resources needed for the crawler engine to run.
   * @param args[]  pass the array of strings which are seed urls
   */
   public static void main(String []args){
   
      //exit in case if seed url are not passed as command line arguments
      if(args.length==0){
	     System.out.println("Please pass at least one URL as seed to crawler ....");
		 System.exit(0);
	  }
	
      //instantiate, seed and start Crawler engine 	
      CrawlEngine engine = new CrawlEngine();
	  engine.putSeed(args);
	  engine.runEngine();
   }
}