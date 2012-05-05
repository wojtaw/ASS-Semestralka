package ass.tests;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;

import ass.server.auth.BasicAuthentification;
import ass.server.cache.CacheManager;
import ass.server.cache.CacheObject;
import static org.junit.Assert.*;

import java.lang.reflect.*;

public class BasicAuthentificationTest {
	
	public BasicAuthentificationTest(){
		
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
	}

	@Test
	public void testFileProtection(){
		BasicAuthentification authentification = new BasicAuthentification();
		assertFalse(authentification.isProtected("testFiles/unprotectedFolder/"));
		assertTrue(authentification.isProtected("testFiles/protectedFolder/"));
		
		assertFalse(authentification.isProtected(new File("testFiles/unprotectedFolder/protection.jpg")));
		assertTrue(authentification.isProtected(new File("testFiles/protectedFolder/protection.jpg")));		
	}
	
	//dXNlcjpwYXNzd29yZA==
	//user:user
	//password:password
	@Test
	public void testBase64Decoder() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		BasicAuthentification authentification = new BasicAuthentification();	
		
		Method myMethod = BasicAuthentification.class.getDeclaredMethod("decodeBase64", new Class[]{String.class});
		myMethod.setAccessible(true);
		
		//Test presence of htaccess files
		String returnedValue = (String)myMethod.invoke(authentification, "dXNlcjpwYXNzd29yZA==");
		assertEquals("user:password", returnedValue);
		myMethod.setAccessible(false);		
	}
	
	@Test
	public void testAuthorizeParsing() throws Exception{
		BasicAuthentification authentification = new BasicAuthentification();
		authentification.authorizeCode("basic dXNlcjpwYXNzd29yZA==");
		Thread.sleep(100);
		assertEquals("user", authentification.getUsername());
		assertEquals("password", authentification.getPassword());
		
	}
}
