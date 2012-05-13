package ass.server.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Date;

import ass.utils.ApplicationOutput;

public class CacheObject {
	
	private File cachedFile;
	private Timestamp lastAccessTimestamp;
	private Date date;
	private byte[] fileBytes;
	
	public CacheObject(File cachedFile){
		this.cachedFile = cachedFile;
		date = new Date();
		lastAccessTimestamp = new Timestamp(date.getTime()); 
		createByteArrayFromFile();		
	}
	
	public CacheObject(String path){
		File cachedFile = new File(path);
		this.cachedFile = cachedFile;
		date = new Date();
		lastAccessTimestamp = new Timestamp(date.getTime()); 
		createByteArrayFromFile();
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
	
	public byte[] getBytes(){
		return fileBytes;
	}
	
	public synchronized void objectAccessed(){
		date = new Date();
		lastAccessTimestamp = new Timestamp(date.getTime()); 
	}
	
	//Create byte array from file
	private void createByteArrayFromFile(){
		InputStream in;
		try {
			in = new FileInputStream(cachedFile);
			fileBytes = new byte[(int)cachedFile.length()];
			in.read(fileBytes);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	

}
