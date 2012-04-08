package ass.tests;

import org.junit.Test;
import static org.junit.Assert.*;

import ass.server.ServerConnectionProcessing;

public class ServerConnectionTest {
	
	private String sampleReq = "GET /about-us.jpg HTTP/1.1\r\nHost: localhost\r\n \r\n";
	private String advancedReq = "GET /about-us.jpg HTTP/1.1\r\n" +
			"Host: localhost:8080\r\n" +
			"Connection: keep-alive\r\n" +
			"Cache-Control: max-age=0\r\n" +
			"User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.151 Safari/535.19\r\n" +
			"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n" +
			"Accept-Encoding: gzip,deflate,sdch\r\n" +
			"Accept-Language: cs-CZ,cs;q=0.8\r\n" +
			"Accept-Charset: windows-1250,utf-8;q=0.7,*;q=0.3\r\n";
	
	@Test
	public void SingleSimpleRequestTest(){
		ServerConnectionProcessing myProcessing = new ServerConnectionProcessing(null);
		myProcessing.setInFromClient(sampleReq, null);
		myProcessing.start();
		
		try {
			Thread.sleep(500);
			myProcessing.printOutRecievedValues();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals("localhost", myProcessing.getReqHost());
	}
	
	@Test
	public void SingleAdvancedRequestTest(){
		ServerConnectionProcessing myProcessing = new ServerConnectionProcessing(null);
		myProcessing.setInFromClient(advancedReq, null);
		myProcessing.start();
		
		try {
			Thread.sleep(500);
			myProcessing.printOutRecievedValues();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals("keep-alive", myProcessing.getReqConnection());
	}	
	 
	
}
