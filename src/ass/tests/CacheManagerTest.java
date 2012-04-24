package ass.tests;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import ass.server.cache.CacheManager;

import java.lang.reflect.*;

public class CacheManagerTest {
	
	private File testFile = new File("testFiles/meta-web-create-channel.jpg");	
	private String testPath1 = "testFiles/meta-web-create-channel.jpg";
	private String testPath2 = "testFiles/meta-web-channel-menu.jpg";
	private String testPath3 = "testFiles/meta-web-facelift2a.jpg";
	
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
		assertTrue(myManager.cacheNewFile(testPath1));
		assertTrue(myManager.isFileCached(testPath1));
	}
	
	@Test
	public void cacheFileAndAttemptToAddDuplicate(){
		CacheManager myManager = new CacheManager();
		assertTrue(myManager.cacheNewFile(testPath1));
		assertTrue(myManager.isFileCached(testPath1));		
		assertFalse(myManager.cacheNewFile(testPath1));
	}
	
	@Test
	public void testIsFreeSpaceFunctionality() throws Exception{
		CacheManager myManager = new CacheManager(1500000);
		//Test file1 is large 665658, test file 2 758905
		myManager.cacheNewFile(testPath1);
		myManager.cacheNewFile(testPath2);		
		
		
		Method myMethod = CacheManager.class.getDeclaredMethod("isFreeSpaceFor", new Class[]{long.class});
		myMethod.setAccessible(true);
		
		Boolean returnedValue = (Boolean)myMethod.invoke(myManager, 10101);
		assertTrue(returnedValue);
		
		returnedValue = (Boolean)myMethod.invoke(myManager, 610101);	
		assertFalse(returnedValue);		
		
		myMethod.setAccessible(false);
	}
	
	@Test
	public void testCleanSpaceFuncitonality(){
		CacheManager myManager = new CacheManager(1500000);
		//Test file1 is large 665658, 
		//test file 2 758905
		//test file 3 1165658
		myManager.cacheNewFile(testPath1);
		myManager.cacheNewFile(testPath2);
		assertTrue(myManager.isFileCached(testPath1));
		assertTrue(myManager.isFileCached(testPath2));
		
		myManager.cacheNewFile(testPath3);
		
		assertFalse(myManager.isFileCached(testPath1));
		assertFalse(myManager.isFileCached(testPath2));
		assertTrue(myManager.isFileCached(testPath3));		
	}
}
