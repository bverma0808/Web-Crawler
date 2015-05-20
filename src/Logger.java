import java.util.*;

/**
* Author: Bharat Verma
* This class will facilitate logging tasks
*/
public class Logger implements Runnable , LifeCycleHandler {
   static LinkedList<String> logs = new LinkedList<String>();
   public boolean life = true;
   static boolean logsEnabled = false;
   
   public void kill(){
      life = false;
   }
   
   public static void toggleLogging(boolean flag){
      logsEnabled = flag;
   }
   
   public static void info(String message){
      logs.addLast("INFO ::=>  "+message);
   }
   
   public static void error(String message){
      logs.addLast("ERROR ::=>  "+message);
   }
   
   public void run(){
      System.out.println("Hello from Logger");
      while(life){
	     if(!logs.isEmpty()  && logsEnabled){
		    String nextLog = (String)logs.removeFirst();
			System.out.println(nextLog);
	 	 }
	     politenessDelay();
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