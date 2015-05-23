import java.util.*;

/**
* <pre>
* This class contains the configurations of our web-crawler
* Its a singleton class . At first it tries to load the configurations from the config file 
* and if it fails to do that then it will load the default configurations
*</pre>
*/
public class ConfigManager {

    static ConfigManager configManager;          //for containing the reference to one and only one object of ConfigManager class
    public HashMap<String,Object> configs;     //map of configurations, which specify the functionality of crawler

	/**
	* Constructor for SingleTone class
	* @param fileFullPath : name of configuration file along with absolute path
	* @return
	*/
	private ConfigManager(String fileFullPath){
		//load from config file specified
	    if(!loadConfigs(fileFullPath)){
		   //since loading of configurations from config-file is failed, so load default configurations
		   loadDefaultConfigs();
		}
	}

	/**
	* This method will return ConfigManager's singleton object 
	* @param fileFullPath : name of configuration file along with absolute path
	* @return configManager : reference to the SingleTone Object of ConfigManager
	*/
	public static ConfigManager getConfigManager(String fileFullPath){
		if(configManager==null){
			configManager = new ConfigManager(fileFullPath);
		}
		return configManager;
	}

	/**
	*  Loads a config file into the memory
	* @param fileFullPath : name of configuration file along with absolute path
	* @return true if configurations successfully loaded
    *                  false otherwise
	*/
	private boolean loadConfigs(String fileFullPath){
          configs = ConfigParser.parse(fileFullPath);
		  if(configs==null){
		     //falied to load configs
		     return false;
		  }
		  
		  //configs loaded successfully
		  return true;
	}

    /**
	*  This method will load default configurations in the system
	* @param
	* @return void
    */	
	private void loadDefaultConfigs(){
	
	   //initialising configs map
	   configs = new HashMap<String,Object>();
	
       //------- put the default values in config ---------
	   configs.put("downloadHtmlPageContent", false);
	   
	   LinkedList<String> blockedLinkTypes = new LinkedList<String>();
	   blockedLinkTypes.add("png");
	   blockedLinkTypes.add("jpeg");
	   blockedLinkTypes.add("jpg");
	   blockedLinkTypes.add("mp3");
	   blockedLinkTypes.add("css");
	   configs.put("blockedLinkTypes", blockedLinkTypes);
	   
	  
	   LinkedList<String> knownLinkTypes = new LinkedList<String>();
	   knownLinkTypes.add("js");
	   knownLinkTypes.add("css");
	   knownLinkTypes.add("html");
	   knownLinkTypes.add("htm");
	   knownLinkTypes.add("jpeg");
	   knownLinkTypes.add("jpg");
	   knownLinkTypes.add("png");
	   knownLinkTypes.add("mp3");
	   knownLinkTypes.add("pdf");
	   configs.put("knownLinkTypes", knownLinkTypes);
	   
	   configs.put("crawlDepth", 4);
	   
	   configs.put("politenessDelay", 5000);
	   
	   configs.put("maxLinksToCrawl", 5);
	}

}