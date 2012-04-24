package ass.server.cache;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class CacheManager {
	private long cacheCapacity = 10*1000000; //Capacity in MBytes
	//private ArrayList<CacheObject> cacheObjects = new ArrayList<CacheObject>();
	//private HashMap<CacheObject, long> cacheObjects = new HashMap();
	private HashMap cacheObjects = new HashMap();
	
	public CacheManager(){
		
	}
	
	public CacheManager(long cacheCapacity){
		this.cacheCapacity = cacheCapacity;
	}
	
	public boolean isFileCached(File file){
		return cacheObjects.containsValue(file.getAbsolutePath());
	}
	
	public boolean cacheNewFile(File file){
		if(isFileCached(file)) return false;
		cacheObjects.put(new CacheObject(file), file.getAbsolutePath());
		return true;
	}
	
	private boolean removeFiles(long neededSize){
		int gainedSpace = 0;
		while(gainedSpace < neededSize){
			
		}
		return true;
	}
	
	public long getCapacity(){
		return cacheCapacity;
	}
	
	

}
