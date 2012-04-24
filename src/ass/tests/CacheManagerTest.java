package ass.tests;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import ass.server.cache.CacheManager;

public class CacheManagerTest {
	
	private File testFile = new File("testFiles/meta-web-create-channel.jpg");	
	
	public CacheManagerTest(){
		CacheManager myManager = new CacheManager();
		assertEquals(10*1000000, myManager.getCapacity());
	}
	@Test
	public void buildCacheWithDefaultCapacity(){
		CacheManager myManager = new CacheManager(5*1000000);
		assertEquals(5*1000000, myManager.getCapacity());		
	}
	
	public void buildCacheWithCustomCapacity(){
		
	}
	
	@Test
	public void cacheOneFileAndTestPresence(){
		CacheManager myManager = new CacheManager();
		myManager.cacheNewFile(testFile);
		assertTrue(myManager.isFileCached(testFile));
		
	}
	 

}
