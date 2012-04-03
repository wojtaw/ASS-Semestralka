package ass.tests;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import ass.server.WebServer;

public class WebServerTest {
	
	@Test
	public void testServerLaunch(){
		WebServer webServer= new WebServer();	
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(webServer.serverOn);
		webServer.terminateServer();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertFalse(webServer.serverOn);		
	}		
	
	@Test
	public void runWebServerAndTestDefaultPort(){
		WebServer webServer= new WebServer();	

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(8080, webServer.portNumber);
		
		webServer.terminateServer();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertFalse(webServer.serverOn);		
	}	
	

	@Test
	public void runWebServerAndTestCustomPort(){
		WebServer webServer= new WebServer(9000);	

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(9000, webServer.portNumber);
		
		webServer.terminateServer();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertFalse(webServer.serverOn);		
	}		

}
