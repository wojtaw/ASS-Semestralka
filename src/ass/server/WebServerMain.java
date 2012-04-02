package ass.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import ass.server.WebServer.ServerListener;
import ass.server.multirequest.ThreadPool;
import ass.utils.ApplicationOutput;

public class WebServerMain {
	public int portNumber = 8080; //Default port for our webserver
	protected boolean serverOn = false;
    protected ServerSocket serverSocket = null;	
	private Thread serviceThread;   
	private ThreadPool testPool;
    
	public WebServerMain(int portNumber) {
		ApplicationOutput.printLog("Server instance created");
		this.portNumber = portNumber;
		createThreadPool();
		startServer();
	
	} 
	
	private void createThreadPool() {
		testPool = new ThreadPool(50);
	}

	public void startServer() {
		ApplicationOutput.printLog("Server is starting");
		Runnable runnable = new ServerListener();
		serviceThread = new Thread(runnable);
		serviceThread.start();		
	}	
	
	private void processIncomingRequest(BufferedReader buffReader){
		testPool.requestProcessing(buffReader);
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
	            
	            processIncomingRequest(inFromClient);
	            
	            ApplicationOutput.printLog("Something recieved");
	         }	    	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	    	
	    }
	}	
}
