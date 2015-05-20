import java.util.*;

/**
* Author:  Bharat Verma
* This thread will receive inputs from user
* and will work according to those inputs 
* It will present a list of possible options to the User and prompts the user to choose one of them
* The user can select a option based on the type of work he wants to do
*/
public class UserInputReader implements Runnable, LifeCycleHandler {
   boolean life = true;
   
   public void kill(){
      life = false;
   }
   
   public void run(){
      System.out.println("Hello from UserInputReader");
      Scanner sc  = new Scanner(System.in);
	  
	  while(life){
	     displayOptionsMenu();
	     try{
	        String input = sc.next();
	        int option = Integer.parseInt(input);
	        switch(option){
			   case 1: Main.stopCrawler();
			               break;
						   
			   case 2: Main.displayVisitedLinksCount();
			               break;
						   
			   case 3: Main.toggleLogging(true);
			               break;
						   
			   case 4: Main.toggleLogging(false);
			               break;
						   
			   default: System.out.println("Please enter a in range numeric value.");
			}
	     }
	     catch(Exception ex){
		    System.out.println("Please enter a valid numeric option");
	     }
	  }
	  
   }
   
   public void displayOptionsMenu(){
      System.out.println("<==List of Possible Options==>");
	  System.out.println("     1. Stop Crawler");
	  System.out.println("     2. Display total number of links visited till now");
	  System.out.println("     3. Start displaying crawler logs");
	  System.out.print("Enter your choice:=> ");
   }
   
}