package ass.server.cache;

import java.util.ArrayList;

public class CacheManager {
	private int cacheCapacity = 10; //Capacity in MBytes
	private ArrayList<CacheObject> cacheObjects = new ArrayList<CacheObject>();
	
	public CacheManager(){
		
	}
	
	public boolean isFileCached(String filePath){
		return false;
	}
	
	

}
