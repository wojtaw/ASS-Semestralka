package ass.server.cache;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;

public class CacheObject {
	
	private File cachedFile;
	private Timestamp lastAccessTimestamp;
	private Date date;
	
	public CacheObject(File cachedFile){
		this.cachedFile = cachedFile;
		date = new Date();
		lastAccessTimestamp = new Timestamp(date.getTime()); 
	}
	
	public File getCachedFile(){
		date = new Date();
		lastAccessTimestamp = new Timestamp(date.getTime());
		return cachedFile;
	}
	
	public Timestamp getLastAccessTime(){
		return lastAccessTimestamp;	
	}
	
	public long getObjectSize(){
		return cachedFile.length();
	}

}
