/**
* Author : Bharat Verma
* An Object of a class link represent one link in 
*/
public class Link {
   String url;                 //url path of link
   int frequency;           //number of times the link if found
   int depth;                  //depth of the link
   
   Link(String url){
      this.url = url;
	  frequency = 1;      //setting initial frequency to 1
   }
   
   
   //---------------getter methods-----------------
   public String getURL(){
      return url;
   }
   
   public int getFrequency(){
      return frequency;
   }
   
   public int getDepth(){
      return depth;
   }
   
   
   //---------------setter methods-----------------
   public void setURL(String url){
      this.url = url;
   }
   
   public void setDepth(int depth){
      this.depth = depth;
   }
   
   public void increaseFrequency(){
      frequency++;
   }
   
}