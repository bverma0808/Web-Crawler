import java.util.*;

/**
*<pre>
* This class provides data structures to be used as a central data storage for links repositories. There are two types of links repositories in the system:
*    1. pool of unvisited links 
*    2. pool of unique visited links
*</pre>
*/
public class DataCenter{
   int capacityOfVisitedLinkPool;        
   LinkedList<Link> unVisitedLinkPool;                                 //contains the links still to visit
   HashMap<String, Link>  visitedLinkPool;                          //contains the visited links
   
   /**
   * Initialise link pools 
   * @param capacity of visited-link-pool
   */
   DataCenter(int capacity){
      capacityOfVisitedLinkPool = capacity;
	  unVisitedLinkPool = new LinkedList<Link>();
	  visitedLinkPool = new HashMap<String, Link>();
   }
   
   /**
   * This method will store a link to the unvisited-link-pool. It will first check the presence of the link in visited-link-pool
   * and if it found it there then it just increases the frequency of the link instead of putting it in unvisited-link-pool
   * @param link to be added
   * @return 
   */
   public void putLink(Link link){
      Link linkFound = (Link)visitedLinkPool.get(link.getURL());
      if(linkFound==null){
          unVisitedLinkPool.addLast(link);
      }
	  else{
	      linkFound.increaseFrequency();
	  }
   }
   
   /**
   * This method will return next unvisited link from unvisited-link-pool
   * @param
   * @return Link  the next unvisited link
   */
   public Link getNextUnvisitedLink(){
      return unVisitedLinkPool.removeFirst();
   }
   
   /**
   * This will add the link to the visited-link-pool, so that we don't visit it again
   * @param link which is just visited
   * @return boolean true if added successfully otherwise returns false
   */
   public boolean markVisited(Link link){
      int currentSizeOfVisitedLinkPool = visitedLinkPool.size();
	  
      if(currentSizeOfVisitedLinkPool < capacityOfVisitedLinkPool){
         String key = link.getURL();
         visitedLinkPool.put(key, link);
		 return true;
	  }
	 
	  return false;
   }

   /**
   * This method checks if the visited-link-pool is out of capacity or not
   * @param
   * @return boolean true if link pool reached to its capacity , otherwise false
   */
   public boolean hasCapacityReached(){
       return capacityOfVisitedLinkPool==visitedLinkPool.size();
   }
   
   /**
   * This method checks if there are any link still to visit in the unvisited-link-pool
   * @param
   * @return boolean true if unvisited-link-pool has links to visit, otherwise false
   */
   public boolean hasMoreUnvisitedLinks(){
      return unVisitedLinkPool.size()>0;
   }
}