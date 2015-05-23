import java.util.*;
import java.io.*;
import java.net.*;

/**
* This class provides methods for fetching a web-page's html text from server
*/
public class PageFetcher {
	
   /**
   * This method will fetch the html text of a webpage whose URL is passed  to it as argument
   * @param pageURL : URL of the web-page , the content of which needs to be fetched
   * @return String  which is the complete text of the html content of the webpage
                    if the URL is malformed then it will return null
   */
   public String fetch(String pageURL){
   
      String htmlContent = "";
      try{
		 //establishing the connection and setting up streams
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
   
}