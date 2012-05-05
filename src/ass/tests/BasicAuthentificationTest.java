package ass.tests;

import java.io.File;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;

import ass.server.auth.BasicAuthentification;
import ass.server.cache.CacheManager;
import ass.server.cache.CacheObject;
import static org.junit.Assert.*;

public class BasicAuthentificationTest {
	
	public BasicAuthentificationTest(){
		
	}
	
	
	@Test
	@Ignore
	public void testFileProtection(){
		BasicAuthentification authentification = new BasicAuthentification();
		
	}
	
	@Test	
	public void testHtaccessParser() throws Exception{
		BasicAuthentification authentification = new BasicAuthentification();	
		
		Method myMethod = BasicAuthentification.class.getDeclaredMethod("readHtaccessFile", new Class[]{String.class});
		myMethod.setAccessible(true);
		
		//Test presence of htaccess files
		Boolean returnedValue = (Boolean)myMethod.invoke(authentification, "testFiles/");
		assertTrue(returnedValue);
		returnedValue = (Boolean)myMethod.invoke(authentification, "testFiles/unprotectedFolder/");
		assertFalse(returnedValue);		
		myMethod.setAccessible(false);
		
		//Test parsing
			
		
		
	}

}
