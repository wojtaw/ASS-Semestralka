package ass.server.cache;

import java.io.File;
import java.io.FileInputStream;
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
	}
	
	public CacheObject(String path){
		File cachedFile = new File(path);
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
	
	/*
	private createByteArrayFromFile(File fileToRead) throws IOException{
		byte answerContent[];
		//Read the file
		InputStream in = new FileInputStream(fileToRead);
		byte[] buff = new byte[clientSocketToAnswer.getSendBufferSize()];
		int bytesRead = 0;
		
		//Create byte array
		fileBytes = new byte[(int)fileToRead.length()];		
		in.read(fileBytes);
		return fileBytes;
	}	
	*/

}
