import java.util.*;

/**
* Author: Bharat Verma
* This class contains the configurations of our web-crawler
* Its a singleton class , and its loaded only once.
* At first it tries to load the configurations from the config file 
* and if it fails to do that then it will load the default configurations
*/
public class ConfigManager {

    static ConfigManager configManager;
    public HashMap<String,Object> configs;     //map of configs, which specify the functionality of crawler

	/**
	* Constructor of a SingleTone class
	*/
	private ConfigManager(String fileFullPath){
		//load from config file specified
	    if(!loadConfigs(fileFullPath)){
		   configs = new HashMap<String,Object>();
		   //loading of configurations from config-file failed, so load default configs
		   loadDefaultConfigs();
		}
	}

	/**
	* This method will return ConfigManager's singleton object 
	* @param fileFullPath : path to the config file along with file name
	* @return configManager object 
	*/
	public static ConfigManager getConfigManager(String fileFullPath){
		if(configManager==null){
			configManager = new ConfigManager(fileFullPath);
		}
		return configManager;
	}

	/**
	* Loads a config file into the memory
	* @param fileFullPath : path to the config file along with file name
	* @return true if config successfully uploaded
    *               false otherwise
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
    */	
	private void loadDefaultConfigs(){
	   configs.put("downloadHtmlPageContent", false);
	   
	   LinkedList<String> blockedLinkTypes = new LinkedList<String>();
	   blockedLinkTypes.add("png");
	   blockedLinkTypes.add("jpeg");
	   blockedLinkTypes.add("jpg");
	   blockedLinkTypes.add("mp3");
	   blockedLinkTypes.add("css");
	   configs.put("blockedLinkTypes", blockedLinkTypes);
	   
	   configs.put("crawlDepth", 4);
	   
	   configs.put("politenessDelay", 300);
	   
	   configs.put("maxLinksToCrawl", 5);
	}

}