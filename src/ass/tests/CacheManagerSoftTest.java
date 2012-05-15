package ass.tests;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Ignore;
import org.junit.Test;

import ass.server.cache.CacheManager;
import ass.server.cache.CacheSoftReference;
import ass.server.cache.CacheWeakReference;

import java.lang.reflect.*;

public class CacheManagerSoftTest {
	
	private File testFile = new File("testFiles/meta-web-create-channel.jpg");	
	private String testPath1 = "testFiles/meta-web-create-channel.jpg";
	private String testPath2 = "testFiles/meta-web-channel-menu.jpg";
	private String testPath3 = "testFiles/meta-web-facelift2a.jpg";
	
	public CacheManagerSoftTest(){

	}
	@Test		
	public void buildCacheWithDefaultCapacity(){
		CacheSoftReference myManager = new CacheSoftReference();
		assertEquals(10*1000000, myManager.getCapacity());	
		
		myManager.stopCacheManager();
	}
	
	@Test		
	public void buildCacheWithCustomCapacity(){
		CacheSoftReference myManager = new CacheSoftReference(5*1000000);
		assertEquals(5*1000000, myManager.getCapacity());	
		
		myManager.stopCacheManager();
	}
	
	@Test		
	public void cacheOneFileAndTestPresence(){
		CacheSoftReference myManager = new CacheSoftReference();
		assertTrue(myManager.cacheNewObject(testPath1));
		assertTrue(myManager.isObjectCached(testPath1));
		
		myManager.stopCacheManager();
	}
	
	@Test		
	public void cacheFileAndAttemptToAddDuplicate(){
		CacheSoftReference myManager = new CacheSoftReference();
		assertTrue(myManager.cacheNewObject(testPath1));
		assertTrue(myManager.isObjectCached(testPath1));		
		assertFalse(myManager.cacheNewObject(testPath1));
		
		myManager.stopCacheManager();
	}
	
	@Test		
	public void testIsFreeSpaceFunctionality() throws Exception{
		CacheSoftReference myManager = new CacheSoftReference(1500000);
		//Test file1 is large 665658, test file 2 758905
		myManager.cacheNewObject(testPath1);
		myManager.cacheNewObject(testPath2);		
		
		
		Method myMethod = CacheSoftReference.class.getDeclaredMethod("isFreeSpaceFor", new Class[]{long.class});
		myMethod.setAccessible(true);
		
		Boolean returnedValue = (Boolean)myMethod.invoke(myManager, 10101);
		assertTrue(returnedValue);
		
		returnedValue = (Boolean)myMethod.invoke(myManager, 610101);	
		assertFalse(returnedValue);		
		
		myMethod.setAccessible(false);
		
		myManager.stopCacheManager();
	}
	
	@Test
	public void testCleanSpaceFuncitonality(){
		CacheSoftReference myManager = new CacheSoftReference(1500000);
		//Test file1 is large 665658, 
		//test file 2 758905
		//test file 3 1165658
		myManager.cacheNewObject(testPath1);
		myManager.cacheNewObject(testPath2);
		assertTrue(myManager.isObjectCached(testPath1));
		assertTrue(myManager.isObjectCached(testPath2));
		
		myManager.cacheNewObject(testPath3);
		
		assertFalse(myManager.isObjectCached(testPath1));
		assertFalse(myManager.isObjectCached(testPath2));
		assertTrue(myManager.isObjectCached(testPath3));
		
		myManager.stopCacheManager();
	}
	
	
	
	@Test
	public void testCacheCleaner(){
		CacheSoftReference myManager = new CacheSoftReference(1500000,3000);
		myManager.cacheNewObject(testPath1);
		myManager.cacheNewObject(testPath2);	
		assertTrue(myManager.isObjectCached(testPath1));
		assertTrue(myManager.isObjectCached(testPath2));
		//And after 4 seconds, files should be deleted from cache
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		assertFalse(myManager.isObjectCached(testPath1));
		assertFalse(myManager.isObjectCached(testPath2));		
		
		myManager.stopCacheManager();
	}
	
}
