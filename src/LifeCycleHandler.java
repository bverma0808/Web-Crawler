/**
* Author: Bharat Verma
* Each of our three threads Crawler , PageFetcher and PageParser implements this interface 
* So that they can have a kill() method in them, which will kill those threads
*/
public interface LifeCycleHandler{
   void kill();
}