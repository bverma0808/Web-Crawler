/**
* An Object of a class Link represent one url link in memory
*/
public class Link {
   String url;                  //url path of link
   int depth;                  //depth of the link  e.g. Page A-> B -> C   , here depth of links A, B and C are 0, 1 and 2 respectively
   int frequency;           //frequency of the link found, just for statistics
   
   /**
   * @param url path to webpage
   * @param depth of link 
   */
   Link(String url, int depth){
      this.url = url;
	  this.depth = depth;
	  this.frequency = 1;     //initial frequency when the link is formed
   }
   
   
   //---------------getter methods-----------------
   public String getURL(){
      return url;
   }
   
   public int getDepth(){
      return depth;
   }
   
   public int getFrequency(){
      return frequency;
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