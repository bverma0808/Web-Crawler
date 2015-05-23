import java.util.*;

/**
* <pre>
* This class provides methods to check link validations 
* The validations include :
*      1. checking if a link is under allowed depth or not 
*      2. checking if a link is of allowed type or not
* </pre>
*/
public class LinkValidator{
    
	//contains configurations in the form of key value pairs
	HashMap<String,Object> configs;
	
	/**
	* @param configs hashmap containing configurations in the form of key value pairs
	*/
	LinkValidator(HashMap<String,Object> configs){
	   this.configs = configs;
	}
	
	/**
	* This method determines should we visit the given link or not
	* @param link to be checked
	* @return true if link is visit-able otherwise false
	*/
    public boolean isVisitable(Link link) {
	 
	   if(!isAllowedLinkType(link)){ 
	      return false;     //type not allowed
	   }
	   
	   if(isLinkVeryDeep(link)){  
	      return false;    //link is very deep
	   }
	   
	   return true;        //its ok to visit it
	}

   /**
    * This method will check whether a particular type of link is allowed or not
    * LinkType means whether its a link to a css , or js or html or a jpeg etc.
    * the method will verify it from the configurations properties
    * @param link to be verified
    * @return true if the link is allowed  , otherwise false
    */
   public boolean isAllowedLinkType(Link link){
      String url = link.getURL();
      List knownTypes = (LinkedList)configs.get("knownLinkTypes");
	  List blockedTypes = (LinkedList)configs.get("blockedLinkTypes");
      
	  String backPart = url.substring(url.lastIndexOf('.')+1 , url.length());
	  
	  String currentType = "html";       //default link type
	  
	  if(backPart!=null && knownTypes.contains(backPart)){
	     currentType = backPart;
	  }
	  
	  if(blockedTypes.contains(currentType)) {
	      return false;     //type not allowed
	  }
	  
	  return true;    //type is allowed
   }	

     /**
      * This method will check whether the link to be visited is very deep or not
      * @param Link to be visited
      * @return true if depth is less than or equals to crawlDepth otherwise false
      */
    public boolean isLinkVeryDeep(Link link){
       int linkDepth = link.getDepth();
       int crawlDepth = (Integer)configs.get("crawlDepth");
	   if(linkDepth>crawlDepth){
	      return true;
	   }
	   return false;
    }      
	
	
}