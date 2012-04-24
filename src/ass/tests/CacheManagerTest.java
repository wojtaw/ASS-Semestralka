package ass.tests;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import ass.server.cache.CacheManager;

import java.lang.reflect.*;

public class CacheManagerTest {
	
	private File testFile = new File("testFiles/meta-web-create-channel.jpg");	
	private String testPath = "testFiles/meta-web-create-channel.jpg";
	
	public CacheManagerTest(){

	}
	@Test
	public void buildCacheWithDefaultCapacity(){
		CacheManager myManager = new CacheManager();
		assertEquals(10*1000000, myManager.getCapacity());	
	}
	
	@Test
	public void buildCacheWithCustomCapacity(){
		CacheManager myManager = new CacheManager(5*1000000);
		assertEquals(5*1000000, myManager.getCapacity());			
	}
	
	@Test
	public void cacheOneFileAndTestPresence(){
		CacheManager myManager = new CacheManager();
		assertTrue(myManager.cacheNewFile(testPath));
		assertTrue(myManager.isFileCached(testPath));
	}
	
	@Test
	public void cacheFileAndAttemptToAddDuplicate(){
		CacheManager myManager = new CacheManager();
		assertTrue(myManager.cacheNewFile(testPath));
		assertTrue(myManager.isFileCached(testPath));		
		assertFalse(myManager.cacheNewFile(testPath));
	}
	
	@Test
	public void testIsFreeSpaceFunctionality() throws Exception{
		CacheManager myManager = new CacheManager();
		Method myMethod = CacheManager.class.getDeclaredMethod("isFreeSpaceFor");
		myMethod.setAccessible(true);
		myMethod.invoke(myManager);	
		myMethod.setAccessible(false);
	}
	 

}
