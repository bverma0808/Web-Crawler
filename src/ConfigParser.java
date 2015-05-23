import java.util.*;
import java.io.*;

/**
* This class provides methods which parse the configurations file content and will store them in key value pairs in a HashMap
* and provide that HashMap to the crawler environment
*/
public class ConfigParser {

	/**
	* This method parses a configuration file and load the configurations into system
	* the contents of the configuration file should be key value pairs separated by equals sign "=". 
	* Comments should start with '#'
	* @param fileFullPath : name of configuration file along with absolute path
	* @return HashMap of loaded configurations
    *                  or null is returned if configurations file not found
	*/
	public static HashMap<String,Object> parse(String fileFullPath){
	    //we will read configs into this
	    HashMap<String,Object> configs = new HashMap<String, Object>();
		
		//read the config file
		BufferedReader br = null;
		try {
			
			String currentLine;
			br = new BufferedReader(new FileReader(fileFullPath));
			
			//read the file line by line
			while ((currentLine = br.readLine()) != null) {
			   currentLine = currentLine.trim();
			   
			   //check if the line starts with '#' symbol because in that case it would be a comment
			   //and we should not parse that line
		       if(!currentLine.startsWith("#") && !currentLine.equals("")){
			   
			      //tokenize the string in key value pair on the basis of equals sign
			      StringTokenizer stk = new StringTokenizer(currentLine, "=");
				  
				  String key = stk.nextToken();
				  String value = stk.nextToken();
				  
				  //convert the value in appropriate data-type
				  Object data = convertToValidDataType(value.trim());
				  
				  //put the key value pair in the configs
				  configs.put(key.trim(), data);
			   }
			}
			
		} 
		catch (Exception e) {
		    System.out.println("It seems there are some problems in loading data from the configuration file : " + fileFullPath);
			System.out.println("So we are loading default configurations in the system...");
			e.printStackTrace();
			//failed to load configs from file
			return null;
		} 
		finally {
			try {
			    //close the stream
				if (br != null)
				    br.close();
			} 
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	    
		System.out.println("Configurations for the crawler loaded successfully......");
		//successfully loaded configs from file
		return configs;
	}
	
	
	/**
	* This method will convert a string to its corresponding data-type
	* e.g. "true" or "false" will be converted to Boolean type
	* numeric values will be converted in Integer types
	* and [ 'abc',  'xyz' ] type strings will be converted to a linked list of strings 
	* @param : value :  a string which needs to be converted to its corresponding data-type based on its nature
	* @return :  Object representing the value 
	*
	*  // TODO: This method needs to be more intelligent to identify a broader set of data types
	*/
	public static Object convertToValidDataType(String value) throws Exception {
	
	   //check for boolean value
	   if(value.equals("true") || value.equals("false")){
	      return Boolean.parseBoolean(value);
	   }
	   
	   //check whether its a list 
	   else if(value.startsWith("[") && value.endsWith("]")){
	      StringTokenizer tokenizer = new StringTokenizer(value, "[,']\"");
		  LinkedList<String> list = new LinkedList<String>();
		  while(tokenizer.hasMoreTokens()){
		     String token = tokenizer.nextToken();
			 list.addLast(token);
		  }
		  return list;
	   }
	   
	   //check if its a number
	   else{
	        try{
	            Integer number = Integer.parseInt(value);
				//returning as a Integer Object
				return number;
			}
			catch(Exception e){
			    //returning as a String Object
				StringTokenizer tokenizer = new StringTokenizer(value, "\"'");
				if(tokenizer.hasMoreTokens())
				   return tokenizer.nextToken();
			    return value;
			}
	   }
	}

}