package ass.server.cache;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class CacheManager {
	private int cacheCapacity = 10*1000000; //Capacity in MBytes
	//private ArrayList<CacheObject> cacheObjects = new ArrayList<CacheObject>();
	//private HashMap<CacheObject, long> cacheObjects = new HashMap();
	private HashMap cacheObjects = new HashMap();
	
	public CacheManager(){
		
	}
	
	public CacheManager(int cacheCapacity){
		this.cacheCapacity = cacheCapacity;
	}
	
	public boolean isFileCached(File file){
		
		return false;
	}
	
	public boolean cacheNewFile(File file){
		if(isFileCached(file)) return false;
		cacheObjects.put(file, file.length());
		return true;
	}
	
	private boolean removeFiles(long neededSize){
		int gainedSpace = 0;
		while(gainedSpace < neededSize){
			
		}
		return true;
	}
	
	

}
