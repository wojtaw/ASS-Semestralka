package ass.server.cache;

import java.util.ArrayList;

public class CacheManager {
	private int cacheCapacity = 10*1000000; //Capacity in MBytes
	private ArrayList<CacheObject> cacheObjects = new ArrayList<CacheObject>();
	
	public CacheManager(){
		
	}
	
	public CacheManager(int cacheCapacity){
		this.cacheCapacity = cacheCapacity;
	}
	
	public boolean isFileCached(String filePath){
		return false;
	}
	
	

}
