package ass.tests;

import org.junit.Test;
import static org.junit.Assert.*;

import ass.server.ServerConnectionProcessing;

public class ServerConnectionTest {
	
	private String sampleReq = "GET /testImage.jpg HTTP/1.1\r\n  Host: localhost\r\n \r\n";
	
	@Test
	public void SingleRequestTest(){
		ServerConnectionProcessing myProcessing = new ServerConnectionProcessing(null);
		myProcessing.setInFromClient(sampleReq);
		myProcessing.start();
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals("localhost", myProcessing.getReqHost());
	}
	
}
