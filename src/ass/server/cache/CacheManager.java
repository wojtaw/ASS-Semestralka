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

public class CacheManager implements CacheInterface<String, CacheObject>{
	private long cacheCapacity = 10*1000000; //Capacity in MBytes
	protected HashMap<String, CacheObject> cacheObjects = new HashMap();
	private long cachedSpace = 0;
	protected int maximumCacheObjectAge = 3000; //In miliseconds
	protected boolean isRunning = false;
	
	
	public CacheManager(){
		startCleaner();
	}
	

	public CacheManager(long cacheCapacity){
		this.cacheCapacity = cacheCapacity;
		startCleaner();
	}	
	
	public CacheManager(long cacheCapacity, int maximumCacheObjectAge){
		this.cacheCapacity = cacheCapacity;
		this.maximumCacheObjectAge = maximumCacheObjectAge;
		startCleaner();
	}
	
	private void startCleaner() {
		isRunning = true;
		CacheCleaner cacheCleaner = new CacheCleaner();
		cacheCleaner.start();
	}
	
	public boolean isObjectCached(String path){
		return cacheObjects.containsKey(path);
	}
	
	public CacheObject getCachedObject(String path){
		if(!isObjectCached(path)) return null;
		return cacheObjects.get(path);
	}
	
	public File getCachedFile(String path){
		if(!isObjectCached(path)) return null;
		return cacheObjects.get(path).getCachedFile();
	}	
	
	public boolean cacheNewObject(String path){
		if(isObjectCached(path)) return false;
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
	
	
	
	private boolean cleanSpace(long objectSize) {
		//Check if cache contains something
		if(cacheObjects.isEmpty()){
			ApplicationOutput.printLog("Cache is empty, no more space to clean");
			return false;
		}
		//Keep removing least accessed files until there will be enough free space
		while(objectSize > (cacheCapacity - cachedSpace)){		
			Entry<String, CacheObject> leastAccessedEntry = null;
			for (Entry<String, CacheObject> entry : cacheObjects.entrySet())
			{
				if(leastAccessedEntry == null) leastAccessedEntry = entry; 
				else if(leastAccessedEntry.getValue().getLastAccessTime().after(entry.getValue().getLastAccessTime()))
					leastAccessedEntry = entry;
			}
			cacheObjects.remove(leastAccessedEntry.getKey());
			removeFromCache(leastAccessedEntry.getValue());
		}
		
		return true;
		
	}
	
	public boolean removeFromCache(CacheObject removedObject) {
		ApplicationOutput.printLog("Called remove object from cache");
		if(removedObject==null) return false;
		cachedSpace -= removedObject.getObjectSize();
		ApplicationOutput.printLog("Object "+removedObject.getCachedFile().getAbsolutePath()+" was removed");
		return true;
	}
	

	private boolean isFreeSpaceFor(long objectSize) {
		ApplicationOutput.printLog("Asking for space "+objectSize);
		ApplicationOutput.printLog("Free space: "+(cacheCapacity - cachedSpace));
		if((cacheCapacity - cachedSpace) >= objectSize) return true;
		else return false;
	}
	
	public long getCapacity(){
		return cacheCapacity;
	}
	
	public void stopCacheManager(){
		isRunning = false;
	}
	
	private class CacheCleaner extends Thread{

		@Override
		public void run() {
			while(isRunning){				
				searchForOutdatedFiles();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
			}
		}

		private void searchForOutdatedFiles() {
			Date date = new Date();		
			//Let's set timestamp for some point in future, just for sure that all files are older
			Timestamp maximumAgeTime = new Timestamp(date.getTime() - maximumCacheObjectAge);			

			Iterator<Entry<String, CacheObject>> cacheIterator = cacheObjects.entrySet().iterator();
			
			Entry<String,CacheObject> entry; 
		    while (cacheIterator.hasNext()) {		    	
		    	entry = cacheIterator.next();
		    	if(maximumAgeTime.after(entry.getValue().getLastAccessTime())){
		    		cacheIterator.remove();
		    		removeFromCache(entry.getValue());
		    	}
		    }			
			
			
		}
		
	}
	
	
	

}
