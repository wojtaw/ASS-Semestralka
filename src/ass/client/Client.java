package ass.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;



public class Client {
	
	public static void main(String[] args) {
		runClient();
	}
	
	
	private static void runClient() {
	try	{			
			String requestToSend = "GET /test.jpg HTTP/1.1\r\n" +
					"Host: localhost:8080\r\n"; 
					
			String answerRecieved = "";
			
			Socket clientSocket = new Socket("localhost", 8080);
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			outToServer.writeBytes(requestToSend + '\n');
			
			
			answerRecieved = inFromServer.readLine();
			System.out.println("FROM SERVER: " + answerRecieved);
			clientSocket.close();
		} catch (Exception e) {
			
		}
	}



}
