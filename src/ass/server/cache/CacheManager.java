package ass.server.cache;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import ass.utils.ApplicationOutput;

public class CacheManager {
	private long cacheCapacity = 10*1000000; //Capacity in MBytes
	//private ArrayList<CacheObject> cacheObjects = new ArrayList<CacheObject>();
	private HashMap<String, CacheObject> cacheObjects = new HashMap();
	
	public CacheManager(){
		
	}
	
	public CacheManager(long cacheCapacity){
		this.cacheCapacity = cacheCapacity;
	}
	
	public boolean isFileCached(String path){
		return cacheObjects.containsKey(path);
	}
	
	public File getCachedFile(String path){
		if(!isFileCached(path)) return null;
		return cacheObjects.get(path).getCachedFile();
	}
	
	public boolean cacheNewFile(String path){
		if(isFileCached(path)) return false;
		CacheObject tmpCacheObject = new CacheObject(path);
		if(tmpCacheObject.getObjectSize() > cacheCapacity) return false;
		if(isFreeSpaceFor(tmpCacheObject.getObjectSize())) cacheObjects.put(path,new CacheObject(path));
		else {
			cleanSpace(tmpCacheObject.getObjectSize());
			cacheObjects.put(path,new CacheObject(path));
		}
		return true;
	}
	
	
	
	private void cleanSpace(long objectSize) {
		
		
	}
	

	private boolean isFreeSpaceFor(long objectSize) {
		ApplicationOutput.printLog("Asking for space "+objectSize);
		/*
		Set cacheSet = cacheObjects.entrySet();
		Iterator i = cacheSet.iterator();

		//lowestTime
	    while(i.hasNext()){
	    	System.out.println(i.next());
	    }	
	    */
		return false;
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
