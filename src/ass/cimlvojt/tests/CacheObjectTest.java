package ass.cimlvojt.tests;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;

import org.junit.Test;

import ass.cimlvojt.server.cache.CacheObject;
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
	public void testFileSize(){
		CacheObject testCacheObject = new CacheObject(testFile);		
		assertEquals(testFile.length() ,testCacheObject.getObjectSize());
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
	
	@Test
	public void timestampIsUpdatingWhenAccessed(){
		CacheObject testCacheObject = new CacheObject(testFile);
		Timestamp myTestTimestamp = testCacheObject.getLastAccessTime();

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		testCacheObject.getCachedFile();
		assertTrue(myTestTimestamp.before(testCacheObject.getLastAccessTime()));
	}

}
