import java.util.*;

/**
* Author: Bharat Verma
* This class will make our ParserThread , which will continuously fetch data from the RawDataBucket and 
*  parse the HTML data to extract out all the links, It then put extracted links into the LinkPoolBucket
*/
public class PageParser implements Runnable , LifeCycleHandler{
   boolean life = true;
   
   public void kill(){
      life = false;
   }
   
   /**
   * This method parses the htmlContent and extracts all the links present in the html page 
   * @param String containing HTML content
   * @return a arrayList containing links
   */
   public static ArrayList<String> parse(String htmlContent){
      ArrayList<String> listOfLinks = new ArrayList<String>();
      String linkURL = "";
	  
	  //we are using the logics of finite automata here.
	  //initially the automata will be at state 1. And we will try to find all the '"href" and "src" values
	  //using this technique.
      int state = 1;
      
	  for(int i=0; i<htmlContent.length(); i++){
	     char nextChar = htmlContent.charAt(i);
		 
		 //state transition for string "href"
		 if(state==1 && (nextChar=='h' || nextChar=='H') ){
		    state = 2;
		 } 
		 else if(state==2 && (nextChar=='r' || nextChar=='R')){
		    state = 3;
		 }
		 else if(state==3 && (nextChar=='e' || nextChar=='E')){
		    state = 4;
		 }
		 else if(state==4 && (nextChar=='f' || nextChar=='F')){
		    state = 5;
		 }
		 else if((state==5 && nextChar==' ') || (state==6 && nextChar==' ')){
		    state = 6;
		 }
		 else if((state==6 && nextChar=='=') || (state==5 && nextChar=='=') || (state==7 && nextChar==' ')){
		    state = 7;
		 }
		 else if((state==7 && (nextChar=='\'' || nextChar=='"')) || (state==8 && nextChar=='\n')){
		    state = 8;
		 }
		 else if(state==8 && (nextChar=='\'' || nextChar=='"')){
		    state = 1;
		    listOfLinks.add(linkURL);
			linkURL = "";
		 }
		 else if(state==8){
		    linkURL += nextChar;       //in this state the automata will read the linkURL
		 }
		 
		 
		 //state transition for string "src"
		 else if(state==1 && (nextChar=='s' || nextChar=='S')){
		    state = 11;
		 }
		 else if(state==11 && (nextChar=='r' || nextChar=='R')){
		    state = 12;
		 }
		 else if(state==12 && (nextChar=='c' || nextChar=='C')){
		    state = 13;
		 }
		 else if((state==13 && nextChar==' ') || (state==14 && nextChar==' ')){
		    state = 14;
		 }
		 else if((state==13 && nextChar=='=') || (state==14 && nextChar=='=' ) || (state==15 && nextChar==' ')) {
		    state = 15;
		 }
		 else if((state==15 &&(nextChar=='\'' || nextChar=='"')) || (state==16 && nextChar=='\n')){
		    state = 16;
		 }
		 else if(state==16 && (nextChar=='\'' || nextChar=='"')){
		    state = 1;
		    listOfLinks.add(linkURL);
			linkURL = "";
		 }
		 else if(state==16){
		    linkURL += nextChar;       //in this state the automata will read the linkURL
		 }
		 else{
		    state = 1;
			linkURL = "";
		 }
	  }
    
	  return listOfLinks;
   }

   
   /**
    * First method to be run when this thread starts
   */
   public void run (){
   
      while(life){
	     String rawData = CentralDataBuckets.removeFromRDB();
		
		 if(rawData!=null){
		    ArrayList<String> listOfURLs = parse(rawData);
			Iterator iterator = listOfURLs.iterator();
			while(iterator.hasNext()){
			   String url = (String)iterator.next();
			   Link link = new Link(url);
			   CentralDataBuckets.addToLPB(link);
			}
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