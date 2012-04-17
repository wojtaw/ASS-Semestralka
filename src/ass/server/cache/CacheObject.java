package ass.server.cache;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;

public class CacheObject {
	
	private File cachedFile;
	private Timestamp lastAccessTimestamp;
	
	public CacheObject(File cachedFile){
		this.cachedFile = cachedFile;
		Date date= new java.util.Date();
		lastAccessTimestamp = new Timestamp(date.getTime()); 
	}
	
	public File getCachedFile(){
		return cachedFile;
	}
	
	public Timestamp getLastAccessTime(){
		return lastAccessTimestamp;	
	}

}
