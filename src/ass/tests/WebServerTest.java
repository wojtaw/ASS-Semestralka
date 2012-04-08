package ass.tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Ignore;
import org.junit.Test;

import ass.server.WebServer;
import ass.utils.ApplicationOutput;

public class WebServerTest {
	private Socket clientSocket;
	
	private String advancedReq = "GET /hovno.html HTTP/1.1\r\n" +
	"Host: localhost:8080\r\n" +
	"Connection: keep-alive\r\n" +
	"Cache-Control: max-age=0\r\n" +
	"User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.151 Safari/535.19\r\n" +
	"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n" +
	"Accept-Encoding: gzip,deflate,sdch\r\n" +
	"Accept-Language: cs-CZ,cs;q=0.8\r\n" +
	"Accept-Charset: windows-1250,utf-8;q=0.7,*;q=0.3\r\n";	
	
	@Ignore
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
	
	@Ignore
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
	

	@Ignore
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
	
	@Test
	@Ignore
	public void testPreparedThreadsNumber() throws Exception{
		WebServer webServer= new WebServer(8080);	

		Thread.sleep(1000);
		assertEquals(webServer.getTestThreadPool().getCapacity(), webServer.getTestThreadPool().getTestFreeThreads());
		
		webServer.terminateServer();
		Thread.sleep(200);
		assertFalse(webServer.serverOn);			
	}
	
	@Test
	@Ignore
	public void runWebServerAndSendSingleRequest() throws Exception{
		WebServer webServer= new WebServer(8080);	

		Thread.sleep(1000);
		assertEquals(webServer.getTestThreadPool().getCapacity(), webServer.getTestThreadPool().getTestFreeThreads());
		
		startClientConnection();

		Thread.sleep(300);	
		sendRequest(advancedReq);
		Thread.sleep(100);		

		assertEquals(webServer.getTestThreadPool().getCapacity(), webServer.getTestThreadPool().getTestFreeThreads());		
		
		endClientConnection();
		Thread.sleep(200);
		webServer.terminateServer();
		Thread.sleep(200);
		assertFalse(webServer.serverOn);		
	}	
	
	@Test
	public void runWebServerAndSendMultipleRequest() throws Exception{
		WebServer webServer= new WebServer(8080);	

		Thread.sleep(1000);
		assertEquals(webServer.getTestThreadPool().getCapacity(), webServer.getTestThreadPool().getTestFreeThreads());		


		Thread.sleep(300);	
		
		for (int i = 0; i < 50; i++) {
			startClientConnection();
			sendRequest(advancedReq);
			endClientConnection();			
			Thread.sleep(50);	
		}		
		Thread.sleep(200);	
		assertTrue((webServer.getTestThreadPool().getCapacity() <= webServer.getTestThreadPool().getTestFreeThreads()));
		
		ApplicationOutput.printLog("Remaining free threads: "+webServer.getTestThreadPool().getTestFreeThreads());

		webServer.terminateServer();
		Thread.sleep(200);
		assertFalse(webServer.serverOn);		
	}	
		
	
	
	//Utilities for test
	private void startClientConnection(){
		try {
			clientSocket = new Socket("localhost", 8080);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//Utilities for test
	private void endClientConnection(){
		try {
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Utilities for test
	public void sendRequest(String requestMessage){
		try {						
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			outToServer.writeBytes(requestMessage);
		} catch (Exception e) {
			System.out.println("Fail");
			e.printStackTrace();
		}		
	}

}
