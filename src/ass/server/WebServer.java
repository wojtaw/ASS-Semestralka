package ass.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import ass.server.multirequest.ThreadPool;
import ass.utils.ApplicationOutput;

public class WebServer {
	public int portNumber = 8080; //Default port for our webserver
	protected boolean serverOn = false;
    protected ServerSocket serverSocket = null;	
	private Thread serviceThread;   
	private ThreadPool testPool;
    
	public WebServer(int portNumber) {
		ApplicationOutput.printLog("Server instance created");
		this.portNumber = portNumber;
		createThreadPool();
		startServer();
	} 
	
	public WebServer() {
		ApplicationOutput.printLog("Server instance created");
		createThreadPool();
		startServer();
	} 	
	
	private void createThreadPool() {
		testPool = new ThreadPool(80);
	}

	public void startServer() {
		ApplicationOutput.printLog("Server is starting");
		Runnable runnable = new ServerListener();
		serviceThread = new Thread(runnable);
		serviceThread.start();		
	}	
	
	private void processIncomingRequest(String requestData){
		testPool.requestProcessing(requestData);
	}
	
	class ServerListener implements Runnable {
        String clientSentence = "";
        
	    public void run() {
	    	try {
				serverSocket = new ServerSocket(portNumber);
				ApplicationOutput.printLog("Socket created listening on port "+portNumber);
				serverOn = true;
	         while(serverOn)
	         {
	        	ApplicationOutput.printLog("Accepting message");
	            Socket connectionSocket = serverSocket.accept();
	            BufferedReader inFromClient =
	               new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
	            
	            String tmpStr = inFromClient.readLine();
	            while(tmpStr != null){
	            	clientSentence += (tmpStr + "\r\n");
	            	tmpStr = inFromClient.readLine();
	            }	            
	            
	            processIncomingRequest(clientSentence);
	            
	            ApplicationOutput.printLog("Something recieved");
	         }	    	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	    	
	    }
	}	
}
