package ass.server;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.junit.Ignore;
import org.junit.Test;

public class WebServerTest {
	
	public WebServerTest(){
		runWebserverOn8080();
		sendHTTPToServer();
		//parserTest();
	}
	
	@Test
	@Ignore
	public void parserTest(){
		//String request = "GET /testImage.jpg HTTP/1.1\r\n  Host: localhost\r\n \r\n";
		WebServer webServer= new WebServer(8080);
		//Client client = new Client();
		//webServer.parseRequestValues(request);
		assertEquals("GET", webServer.getMehod());
		assertEquals("/index.html", webServer.getPath());
		assertEquals("cs.wikipedia.org", webServer.getHost());
	}
	
	@Test
	@Ignore	
	public void runWebserverOn8080(){
		WebServer webServer= new WebServer(8080);	

		while(!webServer.serverOn){
			
		}
		
		try {
			webServer.terminateServer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertFalse(webServer.serverOn);		

	}
	
	@Test
	@Ignore
	public void sendHTTPToServer(){
		WebServer webServer= new WebServer(8080);	

		while(!webServer.serverOn){
			System.out.print("Wait");
		}
		try {		
			Thread.sleep(500);
			System.out.print("Sending to server");
			String myHTTPRequest = "GET /testImage.jpg HTTP/1.1\r\n  Host: localhost\r\n \r\n";
			Socket clientSocket = new Socket("localhost", 8080);
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			outToServer.writeBytes(myHTTPRequest);
			
			System.out.print("Request sent");
			String response = inFromServer.readLine();
			System.out.println("FROM SERVER: " + response);
			clientSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}	

	}

}
