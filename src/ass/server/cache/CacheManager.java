package ass.server.cache;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import ass.utils.ApplicationOutput;

public class CacheManager {
	private long cacheCapacity = 10*1000000; //Capacity in MBytes
	//private ArrayList<CacheObject> cacheObjects = new ArrayList<CacheObject>();
	private HashMap<String, CacheObject> cacheObjects = new HashMap();
	private long cachedSpace = 0;
	
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
		if(isFreeSpaceFor(tmpCacheObject.getObjectSize())){
			cacheObjects.put(path,tmpCacheObject);
			cachedSpace += tmpCacheObject.getObjectSize();
		} else {
			cleanSpace(tmpCacheObject.getObjectSize());
			cacheObjects.put(path,tmpCacheObject);
			cachedSpace += tmpCacheObject.getObjectSize();
		}
		return true;
	}
	
	
	
	private void cleanSpace(long objectSize) {
		//Keep removing least accessed files until there will be enough free space
		while(objectSize > (cacheCapacity - cachedSpace)){
			Date date = new Date();		
			//Let's set timestamp for some point in future, just for sure that all files are older
			Timestamp leastAccessed = new Timestamp(date.getTime()+210000);
			String leastAccessedKey ="";
			for (Entry<String, CacheObject> entry : cacheObjects.entrySet())
			{
				if(leastAccessed.after(entry.getValue().getLastAccessTime()))
					leastAccessedKey = entry.getKey();
			}
			removeFromCache(leastAccessedKey);
		}
		
	}
	
	private void removeFromCache(String path) {
		CacheObject removedObject = cacheObjects.remove(path);
		cachedSpace -= removedObject.getObjectSize();
		ApplicationOutput.printLog("Object "+removedObject.getCachedFile().getAbsolutePath()+" was removed");
	}
	

	private boolean isFreeSpaceFor(long objectSize) {
		ApplicationOutput.printLog("Asking for space "+objectSize);
		ApplicationOutput.printLog("Free space: "+(cacheCapacity - cachedSpace));
		if((cacheCapacity - cachedSpace) >= objectSize) return true;
		else return false;
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
