package ass.server;

import java.io.*;
import java.net.*;

public class Client {
	public Client () {
		try {			
			String sentence;
			String modifiedSentence;
			BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in));
			Socket clientSocket = new Socket("localhost", 7766);
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			sentence = inFromUser.readLine();
			outToServer.writeBytes(sentence + '\n');
			
			
			modifiedSentence = inFromServer.readLine();
			System.out.println("FROM SERVER: " + modifiedSentence);
			clientSocket.close();
		} catch (Exception e) {

		}
 	}
	

}
