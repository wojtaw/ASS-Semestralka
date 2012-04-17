package ass.tests;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;

import org.junit.Test;

import ass.server.cache.CacheObject;
import static org.junit.Assert.*;

public class CacheObjectTest {
	
	private File testFile = new File("testFiles/meta-web-create-channel.jpg");
	
	public CacheObjectTest(){
		
	}
	
	@Test
	public void createSingleCacheObject(){
		assertTrue(testFile.exists());
		CacheObject testCacheObject = new CacheObject(testFile);
		assertTrue(testCacheObject.getCachedFile().exists());
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Date date= new java.util.Date();
		Timestamp myTestTimestamp = new Timestamp(date.getTime()); 		
		assertTrue(testCacheObject.getLastAccessTime().before(myTestTimestamp));
	}
	
	@Test
	public void testTimestampIsBeforeCurrent(){
		CacheObject testCacheObject = new CacheObject(testFile);
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Date date= new java.util.Date();
		Timestamp myTestTimestamp = new Timestamp(date.getTime()); 		
		assertTrue(testCacheObject.getLastAccessTime().before(myTestTimestamp));		
	}

}
